/*******************************************************************************
 * Copyright -2017 @intentlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.endlosiot.common.notification.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.aop.Authorization;
import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.notification.enums.CommunicationFields;
import com.endlosiot.common.notification.operation.EmailContentOperation;
import com.endlosiot.common.notification.view.EmailContentView;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.view.KeyValueView;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller maps all emailcontent related apis
 *
 * @author Nirav.Shah
 * @since 23/07/2018
 */
@RestController
@RequestMapping("/private/email-content")
public class EmailContentPrivateController extends AbstractController<EmailContentView> {
    @Autowired
    EmailContentOperation emailContentOperation;

    @Override
    public BaseOperation<EmailContentView> getOperation() {
        return emailContentOperation;
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> save(@RequestBody EmailContentView emailContentView)
            throws EndlosiotAPIException {
        if (emailContentView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(emailContentView);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(emailContentOperation.doSave(emailContentView));
        } catch (ConstraintViolationException constraintViolationException) {
            LoggerService.exception(constraintViolationException);
            throw new EndlosiotAPIException(ResponseCode.EMAIL_CONTENT_ALREADY_EXIST.getCode(),
                    ResponseCode.EMAIL_CONTENT_ALREADY_EXIST.getMessage());
        }
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(emailContentOperation.doView(id));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> update(@RequestBody EmailContentView emailContentView)
            throws EndlosiotAPIException {
        if (emailContentView == null || emailContentView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(emailContentView);
        return ResponseEntity.status(HttpStatus.OK).body(emailContentOperation.doUpdate(emailContentView));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> delete(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(emailContentOperation.doDelete(id));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> activeInActive(@PathVariable Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    @AccessLog
   // @Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> search(@RequestBody EmailContentView emailContentView,
                                           @RequestParam(name = "start", required = false) Integer start,
                                           @RequestParam(name = "recordSize", required = false) Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(emailContentOperation.doSearch(emailContentView, start, recordSize));
    }

    @Override
    public void isValidSaveData(EmailContentView emailContentView) throws EndlosiotAPIException {
        EmailContentView.isValid(emailContentView);
    }

    /**
     * This method is used to prepare dropdown for communication field.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/communication-field")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> communicationFields() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        CommunicationFields.MAP.forEach((key, value) -> {
            keyValueViews.add(KeyValueView.create(Long.valueOf(key), value.getName()));
        });
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }
}