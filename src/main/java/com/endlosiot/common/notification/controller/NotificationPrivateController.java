/*******************************************************************************
 * Copyright -2018 @intentlabs
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
import com.endlosiot.common.notification.operation.NotificationOperation;
import com.endlosiot.common.notification.view.NotificationView;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This Controller maps all API related to notification.
 *
 * @author Nirav.Shah
 * @since 17/07/2018
 */
@RestController
@RequestMapping("/private/notification")
public class NotificationPrivateController extends AbstractController<NotificationView> {
    @Autowired
    NotificationOperation notificationOperation;

    @Override
    public BaseOperation<NotificationView> getOperation() {
        return notificationOperation;
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.ADD)
    public ResponseEntity<Response> save(@RequestBody NotificationView notificationView)
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .create(ResponseCode.UNAUTHORIZED_ACCESS.getCode(), ResponseCode.UNAUTHORIZED_ACCESS.getMessage()));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> update(@RequestBody NotificationView notificationView)
            throws EndlosiotAPIException {
        if (notificationView == null || notificationView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(notificationOperation.doUpdate(notificationView));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(@RequestBody NotificationView notificationView, Integer start,
                                           Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationOperation.doSearch(notificationView, start, recordSize));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.VIEW)
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .create(ResponseCode.UNAUTHORIZED_ACCESS.getCode(), ResponseCode.UNAUTHORIZED_ACCESS.getMessage()));
    }

    @Override
    @AccessLog
   // @Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.DELETE)
    public ResponseEntity<Response> delete(@PathVariable Long id) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .create(ResponseCode.UNAUTHORIZED_ACCESS.getCode(), ResponseCode.UNAUTHORIZED_ACCESS.getMessage()));
    }

    @Override
    @AccessLog
    //@Authorization(modules = ModuleEnum.NOTIFICATION, rights = RightsEnum.ACTIVATION)
    public ResponseEntity<Response> activeInActive(@PathVariable Long id) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .create(ResponseCode.UNAUTHORIZED_ACCESS.getCode(), ResponseCode.UNAUTHORIZED_ACCESS.getMessage()));
    }

    @Override
    public void isValidSaveData(NotificationView notificationView) throws EndlosiotAPIException {
    }
}