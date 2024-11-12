package com.endlosiot.common.notification.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.aop.Authorization;
import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.notification.enums.EmailAuthenticationMethod;
import com.endlosiot.common.notification.enums.EmailAuthenticationSecurity;
import com.endlosiot.common.notification.operation.EmailAccountOperation;
import com.endlosiot.common.notification.view.EmailAccountView;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.validation.InputField;
import com.endlosiot.common.validation.RegexEnum;
import com.endlosiot.common.validation.Validator;
import com.endlosiot.common.view.KeyValueView;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Controller Maps all Email Account related Apis
 *
 * @author Nisha.Panchal
 * @since 17/07/2018
 */
@RestController
@RequestMapping("/private/email-account")
public class EmailAccountPrivateController extends AbstractController<EmailAccountView> {
    @Autowired
    EmailAccountOperation emailAccountOperation;

    @Override
    public BaseOperation<EmailAccountView> getOperation() {
        return emailAccountOperation;
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.EMAIL_ACCOUNT, rights = RightsEnum.ADD)
    public ResponseEntity<Response> save(@RequestBody EmailAccountView emailAccountView)
            throws EndlosiotAPIException {
        if (emailAccountView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(emailAccountView);
        return ResponseEntity.status(HttpStatus.OK).body(emailAccountOperation.doSave(emailAccountView));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.EMAIL_ACCOUNT, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> update(@RequestBody EmailAccountView emailAccountView)
            throws EndlosiotAPIException {
        if (emailAccountView == null || emailAccountView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(emailAccountView);
        return ResponseEntity.status(HttpStatus.OK).body(emailAccountOperation.doUpdate(emailAccountView));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.EMAIL_ACCOUNT, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(@RequestBody EmailAccountView emailAccountView, Integer start,
                                           Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(emailAccountOperation.doSearch(emailAccountView, start, recordSize));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.EMAIL_ACCOUNT, rights = RightsEnum.VIEW)
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(emailAccountOperation.doView(id));
    }

    @Override
    @AccessLog
   // @Authorization(modules = ModuleEnum.EMAIL_ACCOUNT, rights = RightsEnum.DELETE)
    public ResponseEntity<Response> delete(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(emailAccountOperation.doDelete(id));
        } catch (PersistenceException persistenceException) {
            LoggerService.exception(persistenceException);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_DELETE_EMAIL_ACCOUNT.getCode(),
                    ResponseCode.UNABLE_TO_DELETE_EMAIL_ACCOUNT.getMessage());
        }
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.EMAIL_ACCOUNT, rights = RightsEnum.ACTIVATION)
    public ResponseEntity<Response> activeInActive(@PathVariable Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    public void isValidSaveData(EmailAccountView emailAccountView) throws EndlosiotAPIException {
        Validator.STRING.isValid(new InputField("EMAIL_ACCOUNT_NAME", emailAccountView.getName(), true, 100));
        Validator.STRING.isValid(new InputField("HOST", emailAccountView.getHost(), true, 500));
        Validator.STRING.isValid(
                new InputField("PORT", String.valueOf(emailAccountView.getPort()), true, 0, 65555, RegexEnum.NUMERIC));
        Validator.STRING.isValid(new InputField("EMAIL_USER_NAME", emailAccountView.getUserName(), true, 100));
        Validator.STRING.isValid(new InputField("PASSWORD", emailAccountView.getPassword(), true, 6, 500));
        Validator.STRING
                .isValid(new InputField("REPLY_TO", emailAccountView.getReplyToEmail(), true, 100, RegexEnum.EMAIL));

        Validator.STRING.isValid(new InputField("EMAIL_FROM", emailAccountView.getEmailFrom(), true, 500));

        if (emailAccountView.getRatePerHour() != null) {
            Pattern pattern = Pattern.compile(RegexEnum.NUMERIC.getValue());
            Matcher matcher = pattern.matcher(String.valueOf(emailAccountView.getRatePerHour()));
            if (!matcher.matches()) {
                throw new EndlosiotAPIException(ResponseCode.RATE_PER_HOUR_INVALID.getCode(),
                        ResponseCode.RATE_PER_HOUR_INVALID.getMessage());
            }
        }
        if (emailAccountView.getUpdateRatePerHour() != null) {
            Pattern pattern = Pattern.compile(RegexEnum.NUMERIC.getValue());
            Matcher matcher = pattern.matcher(String.valueOf(emailAccountView.getRatePerHour()));
            if (!matcher.matches()) {
                throw new EndlosiotAPIException(ResponseCode.RATE_PER_HOUR_INVALID.getCode(),
                        ResponseCode.RATE_PER_HOUR_INVALID.getMessage());
            }
        }

        if (emailAccountView.getRatePerDay() != null) {
            Pattern pattern = Pattern.compile(RegexEnum.NUMERIC.getValue());
            Matcher matcher = pattern.matcher(String.valueOf(emailAccountView.getRatePerDay()));
            if (!matcher.matches()) {
                throw new EndlosiotAPIException(ResponseCode.RATE_PER_DAY_INVALID.getCode(),
                        ResponseCode.RATE_PER_DAY_INVALID.getMessage());
            }
        }
        if (emailAccountView.getUpdateRatePerDay() != null) {
            Pattern pattern = Pattern.compile(RegexEnum.NUMERIC.getValue());
            Matcher matcher = pattern.matcher(String.valueOf(emailAccountView.getRatePerDay()));
            if (!matcher.matches()) {
                throw new EndlosiotAPIException(ResponseCode.RATE_PER_DAY_INVALID.getCode(),
                        ResponseCode.RATE_PER_DAY_INVALID.getMessage());
            }
        }
        if (emailAccountView.getAuthenticationMethod() == null
                || emailAccountView.getAuthenticationMethod().getKey() == null) {
            throw new EndlosiotAPIException(ResponseCode.AUTHENTICATION_METHOD_IS_MISSING.getCode(),
                    ResponseCode.AUTHENTICATION_METHOD_IS_MISSING.getMessage());
        }
        if (EmailAuthenticationMethod.fromId(
                Integer.parseInt(String.valueOf(emailAccountView.getAuthenticationMethod().getKey()))) == null) {
            throw new EndlosiotAPIException(ResponseCode.AUTHENTICATION_METHOD_IS_INVALID.getCode(),
                    ResponseCode.AUTHENTICATION_METHOD_IS_INVALID.getMessage());
        }
        if (emailAccountView.getAuthenticationSecurity() == null
                || emailAccountView.getAuthenticationSecurity().getKey() == null) {
            throw new EndlosiotAPIException(ResponseCode.AUTHENTICATION_SECURITY_IS_MISSING.getCode(),
                    ResponseCode.AUTHENTICATION_SECURITY_IS_MISSING.getMessage());
        }
        if (EmailAuthenticationSecurity.fromId(
                Integer.parseInt(String.valueOf(emailAccountView.getAuthenticationSecurity().getKey()))) == null) {
            throw new EndlosiotAPIException(ResponseCode.AUTHENTICATION_SECURITY_IS_INVALID.getCode(),
                    ResponseCode.AUTHENTICATION_SECURITY_IS_INVALID.getMessage());
        }
        if (emailAccountView.getTimeOut() != null) {
            Pattern pattern = Pattern.compile(RegexEnum.NUMERIC.getValue());
            Matcher matcher = pattern.matcher(String.valueOf(emailAccountView.getTimeOut()));
            if (!matcher.matches()) {
                throw new EndlosiotAPIException(ResponseCode.TIMEOUT_IS_INVALID.getCode(),
                        ResponseCode.TIMEOUT_IS_INVALID.getMessage());
            }
        }
    }

    /**
     * This method is used to prepare a list of authentication method.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/auth-method")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> authMethod() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        EmailAuthenticationMethod.MAP.forEach((key, value) -> {
            keyValueViews.add(KeyValueView.create(Long.valueOf(key), value.getName()));
        });
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }

    /**
     * This method is used to prepare a list of authentication security.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/auth-security")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> authSecurity() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        EmailAuthenticationSecurity.MAP.forEach((key, value) -> {
            keyValueViews.add(KeyValueView.create(Long.valueOf(key), value.getName()));
        });
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }

    /**
     * This method is used to provide a list of active email accounts.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/dropdown")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> dropdown() {
        return ResponseEntity.status(HttpStatus.OK).body(emailAccountOperation.doDropdown());
    }
}