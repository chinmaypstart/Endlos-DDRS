package com.endlosiot.common.notification.thread;

import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.notification.enums.TransactionStatusEnum;
import com.endlosiot.common.notification.model.SmsAccountModel;
import com.endlosiot.common.notification.model.TransactionalSmsModel;
import com.endlosiot.common.notification.service.TransactionalSmsService;
import com.endlosiot.common.util.DateUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Component
public class SmsThread {
    @Autowired
    private TransactionalSmsService transactionalSmsService;

    @Async("transactionSmsExecutor")
    public void sendTransactionSms(final TransactionalSmsModel transactionalSmsModel,
                                   final SmsAccountModel smsAccountModel) {
        try {
            transactionSms(transactionalSmsModel, smsAccountModel);
        } catch (Exception e) {
            LoggerService.exception(e);
        }
    }

    // @Async("transactionSmsRetry")
    public void retryTeSms(final TransactionalSmsModel transactionalSmsModel, final SmsAccountModel smsAccountModel) {
        try {
            transactionSms(transactionalSmsModel, smsAccountModel);
        } catch (Exception e) {
            LoggerService.exception(e);
        }
    }

    private void transactionSms(final TransactionalSmsModel transactionalSmsModel,
                                final SmsAccountModel smsAccountModel) {
        submitMessage(transactionalSmsModel, smsAccountModel);
    }

    private void submitMessage(TransactionalSmsModel transactionalSmsModel, SmsAccountModel smsAccountModel) {
        try {

            URI sendUrl = new URI("https://www.smsidea.co.in/smsstatuswithid.aspx");
            HttpHeaders headers = new HttpHeaders();

            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("mobile", smsAccountModel.getMobile());
            map.add("pass", smsAccountModel.getPassword());
            map.add("senderid", smsAccountModel.getSenderId());
            if (StringUtils.isNotBlank(smsAccountModel.getPeId())) {
                map.add("peid", smsAccountModel.getPeId());
            }
            if (StringUtils.isNotBlank(transactionalSmsModel.getTemplateId())) {
                map.add("templateid", transactionalSmsModel.getTemplateId());
            }
            map.add("to", transactionalSmsModel.getSmsTo());
            map.add("msg", transactionalSmsModel.getContent());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
                    headers);

            ResponseEntity<String> response = new RestTemplate().postForEntity(sendUrl, request, String.class);

            if (response.getBody().contains("error")) {
                transactionalSmsModel.setStatus(TransactionStatusEnum.FAILED.getId());
                transactionalSmsModel.setError(response.getBody());
                transactionalSmsService.update(transactionalSmsModel);
            } else {
                transactionalSmsModel.setStatus(TransactionStatusEnum.SENT.getId());
                transactionalSmsModel.setDateSent(DateUtility.getCurrentEpoch());
                transactionalSmsService.update(transactionalSmsModel);
            }
        } catch (Exception ex) {
            LoggerService.exception(ex);
            transactionalSmsModel.setStatus(TransactionStatusEnum.FAILED.getId());
            transactionalSmsModel.setError(ex.getMessage());
            transactionalSmsService.update(transactionalSmsModel);
        }
    }
}
