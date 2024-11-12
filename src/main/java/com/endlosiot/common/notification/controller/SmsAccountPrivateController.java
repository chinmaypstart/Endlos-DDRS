package com.endlosiot.common.notification.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.aop.Authorization;
import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.notification.operation.SmsAccountOperation;
import com.endlosiot.common.notification.view.SmsAccountView;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.validation.InputField;
import com.endlosiot.common.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This Controller Maps all Sms Account related Apis
 *
 * @author neha
 * @since 15/02/2023
 */
@RestController
@RequestMapping("/private/sms-account")
public class SmsAccountPrivateController extends AbstractController<SmsAccountView> {
    @Autowired
    private SmsAccountOperation smsAccountOperation;

    @Override
    public BaseOperation<SmsAccountView> getOperation() {
        return smsAccountOperation;
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.SMS_ACCOUNT, rights = RightsEnum.ADD)
    public ResponseEntity<Response> save(@RequestBody SmsAccountView smsAccountView) throws EndlosiotAPIException {
        if (smsAccountView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(smsAccountView);
        Response response = smsAccountOperation.doSave(smsAccountView);
        if (response instanceof ViewResponse) {
            ViewResponse viewResponse = (ViewResponse) response;
            if (ResponseCode.SAVE_SUCCESSFULLY.equals(ResponseCode.fromId(viewResponse.getCode()))) {
                smsAccountOperation.doSaveMap((SmsAccountView) viewResponse.getView());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @AccessLog
   // @Authorization(modules = ModuleEnum.SMS_ACCOUNT, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> update(@RequestBody SmsAccountView smsAccountView)
            throws EndlosiotAPIException {
        if (smsAccountView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(smsAccountView);
        Response response = smsAccountOperation.doUpdate(smsAccountView);
        if (response instanceof ViewResponse) {
            ViewResponse viewResponse = (ViewResponse) response;
            if (ResponseCode.UPDATE_SUCCESSFULLY.equals(ResponseCode.fromId(viewResponse.getCode()))) {
                smsAccountOperation.doUpdateMap((SmsAccountView) viewResponse.getView());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.SMS_ACCOUNT, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(@RequestBody SmsAccountView smsAccountView,
                                           @RequestParam(name = "start", required = true) Integer start,
                                           @RequestParam(name = "recordSize", required = true) Integer recordSize) throws EndlosiotAPIException {
        if (smsAccountView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(smsAccountOperation.doSearch(smsAccountView, start, recordSize));
    }

    @Override
    @AccessLog
   // @Authorization(modules = ModuleEnum.SMS_ACCOUNT, rights = RightsEnum.VIEW)
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(smsAccountOperation.doView(id));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.SMS_ACCOUNT, rights = RightsEnum.DELETE)
    public ResponseEntity<Response> delete(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(smsAccountOperation.doDelete(id));
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            LoggerService.exception(dataIntegrityViolationException);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_DELETE_SMS_ACCOUNT.getCode(),
                    ResponseCode.UNABLE_TO_DELETE_SMS_ACCOUNT.getMessage());
        }
    }

    @Override
    @AccessLog
    public ResponseEntity<Response> activeInActive(@RequestParam(name = "id", required = true) Long id)
            throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    public void isValidSaveData(SmsAccountView smsAccountView) throws EndlosiotAPIException {
        Validator.STRING.isValid(new InputField("SMS_ACCOUNT_MOBILE", smsAccountView.getMobile(), true, 15));
        Validator.STRING.isValid(new InputField("SMS_ACCOUNT_PASSWORD", smsAccountView.getPassword(), true, 200));
        Validator.STRING.isValid(new InputField("SMS_ACCOUNT_SENDER_ID", smsAccountView.getSenderId(), true, 6));
        Validator.STRING.isValid(new InputField("SMS_ACCOUNT_PE_ID", smsAccountView.getPeId(), false, 20));
    }

    @GetMapping(value = "/dropdown")
    @AccessLog
    public ResponseEntity<Response> dropdown() {
        return ResponseEntity.status(HttpStatus.OK).body(smsAccountOperation.doDropdown());
    }
}
