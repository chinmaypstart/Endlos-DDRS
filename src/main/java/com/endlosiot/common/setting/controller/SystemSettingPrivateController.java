package com.endlosiot.common.setting.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.aop.Authorization;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.setting.operation.SystemSettingOperation;
import com.endlosiot.common.setting.view.SystemSettingView;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.validation.InputField;
import com.endlosiot.common.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller maps all setting related apis.
 *
 * @author Nirav
 * @since 24/11/2018
 */
@Controller
@RequestMapping("/private/system-setting")
public class SystemSettingPrivateController {
    @Autowired
    SystemSettingOperation systemSettingOperation;

    /**
     * This method is used to handle search request coming from client for any module.
     *
     * @param systemSettingView
     * @return
     * @throws EndlosiotAPIException
     */
    @PostMapping(value = "/search")
    @AccessLog
    @Authorization(modules = ModuleEnum.SYSTEM_SETTING, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(
            @RequestBody(required = false) SystemSettingView systemSettingView,
            Integer start,
            Integer recordSize)
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(systemSettingOperation.doSearch(systemSettingView, start, recordSize));
    }

    /**
     * This method is used to handle update request coming from client for any module.
     *
     * @param systemSettingView
     * @return
     * @throws EndlosiotAPIException
     */
    @PutMapping(value = "/update")
    @AccessLog
    @Authorization(modules = ModuleEnum.SYSTEM_SETTING, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> update(@RequestBody SystemSettingView systemSettingView)
            throws EndlosiotAPIException {
        if (systemSettingView == null) {
            throw new EndlosiotAPIException(
                    ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(systemSettingView);
        systemSettingOperation.doUpdate(systemSettingView);

        List<SystemSettingView> systemSettingViews = new ArrayList<>();
        systemSettingViews.add(systemSettingView);
        systemSettingOperation.doRefreshMap(systemSettingViews);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CommonResponse.create(
                                ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                                ResponseCode.UPDATE_SUCCESSFULLY.getMessage()));
    }

    public void isValidSaveData(SystemSettingView systemSettingView) throws EndlosiotAPIException {
        Validator.STRING.isValid(new InputField("PROPERTY", systemSettingView.getKey(), true, 100));
        Validator.STRING.isValid(
                new InputField("PROPERTY_VALUE", systemSettingView.getValue(), true, 100));
    }

    /**
     * This method is used to handle view request coming from client for any module.
     *
     * @param key
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/view")
    @AccessLog
    @Authorization(modules = ModuleEnum.SYSTEM_SETTING, rights = RightsEnum.VIEW)
    public ResponseEntity<Response> view(@RequestParam(name = "key", required = true) String key)
            throws EndlosiotAPIException {
        if (StringUtils.isBlank(key)) {
            throw new EndlosiotAPIException(
                    ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(systemSettingOperation.doView(key));
    }

    /**
     * This method is used to handle edit request coming from client for any module.
     *
     * @param key
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/edit")
    @AccessLog
    @Authorization(modules = ModuleEnum.SYSTEM_SETTING, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> edit(@RequestParam(name = "key", required = true) String key)
            throws EndlosiotAPIException {
        if (StringUtils.isBlank(key)) {
            throw new EndlosiotAPIException(
                    ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(systemSettingOperation.doEdit(key));
    }

    /**
     * This method is used to handle update bulk request coming from client for any module.
     *
     * @param systemSettingViews
     * @return
     * @throws EndlosiotAPIException
     */
    @PutMapping(value = "/updatebulk")
    @AccessLog
    @Authorization(modules = ModuleEnum.SYSTEM_SETTING, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> updateBulk(
            @RequestBody List<SystemSettingView> systemSettingViews) throws EndlosiotAPIException {
        if (systemSettingViews == null || systemSettingViews.isEmpty()) {
            throw new EndlosiotAPIException(
                    ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        systemSettingOperation.doUpdateBulk(systemSettingViews);
        systemSettingOperation.doRefreshMap(systemSettingViews);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CommonResponse.create(
                                ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                                ResponseCode.UPDATE_SUCCESSFULLY.getMessage()));
    }
}

	        
	  


