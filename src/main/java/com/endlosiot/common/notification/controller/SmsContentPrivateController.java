package com.endlosiot.common.notification.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.aop.Authorization;
import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.notification.enums.CommunicationFields;
import com.endlosiot.common.notification.enums.NotificationEnum;
import com.endlosiot.common.notification.operation.SmsContentOperation;
import com.endlosiot.common.notification.view.SmsContentView;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.validation.InputField;
import com.endlosiot.common.validation.Validator;
import com.endlosiot.common.view.KeyValueView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This Controller Maps all Sms content related Apis
 *
 * @author neha
 * @since 15/02/2023
 */
@RestController
@RequestMapping(value = "/private/sms-content")
public class SmsContentPrivateController extends AbstractController<SmsContentView> {
    @Autowired
    SmsContentOperation smsContentOperation;

    @Override
    public BaseOperation<SmsContentView> getOperation() {
        return smsContentOperation;
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.ADD)
    public ResponseEntity<Response> save(@RequestBody SmsContentView smsContentView) throws EndlosiotAPIException {
        if (smsContentView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(smsContentView);
        return ResponseEntity.status(HttpStatus.OK).body(smsContentOperation.doSave(smsContentView));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.VIEW)
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(smsContentOperation.doView(id));
    }

    @Override
    @AccessLog
   // @Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> update(@RequestBody SmsContentView smsContentView)
            throws EndlosiotAPIException {
        if (smsContentView == null || smsContentView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(smsContentView);
        return ResponseEntity.status(HttpStatus.OK).body(smsContentOperation.doUpdate(smsContentView));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.DELETE)
    public ResponseEntity<Response> delete(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(smsContentOperation.doDelete(id));
    }

    @Override
    @AccessLog
    public ResponseEntity<Response> activeInActive(@RequestParam(name = "id") Long id)
            throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(@RequestBody SmsContentView smsContentView,
                                           @RequestParam(name = "start", required = true) Integer start,
                                           @RequestParam(name = "recordSize", required = true) Integer recordSize) throws EndlosiotAPIException {
        if (smsContentView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(smsContentOperation.doSearch(smsContentView, start, recordSize));
    }

    @GetMapping(value = "/communication-field")
    @AccessLog
    public ResponseEntity<Response> communicationFields() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        CommunicationFields.MAP.forEach((key, value) -> keyValueViews.add(KeyValueView.create(key, value.getName())));
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }

    @Override
    public void isValidSaveData(SmsContentView smsContentView) throws EndlosiotAPIException {
        if (smsContentView.getSmsAccountView() == null
                || (smsContentView.getSmsAccountView() != null && smsContentView.getSmsAccountView().getKey() == 0)) {
            throw new EndlosiotAPIException(ResponseCode.SMS_ACCOUNT_IS_MISSING.getCode(),
                    ResponseCode.SMS_ACCOUNT_IS_MISSING.getMessage());
        }
        if (smsContentView.getNotificationView() == null || (smsContentView.getNotificationView() != null
                && smsContentView.getNotificationView().getKey() == 0)) {
            throw new EndlosiotAPIException(ResponseCode.NOTIFICATION_IS_MISSING.getCode(),
                    ResponseCode.NOTIFICATION_IS_MISSING.getMessage());
        }
        if (NotificationEnum.fromId(smsContentView.getNotificationView().getKey().intValue()) == null) {
            throw new EndlosiotAPIException(ResponseCode.NOTIFICATION_IS_INVALID.getCode(),
                    ResponseCode.NOTIFICATION_IS_INVALID.getMessage());
        }
        Validator.STRING.isValid(new InputField("SMS_CONTENT_NAME", smsContentView.getName(), true, 100));
        Validator.STRING.isValid(new InputField("SMS_CONTENT", smsContentView.getContent(), true));

        Validator.STRING.isValid(new InputField("SMS_TEMPLATE_ID", smsContentView.getTemplateId(), false, 20));
    }
}
