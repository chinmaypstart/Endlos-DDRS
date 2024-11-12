/*******************************************************************************
 * Copyright -2019 @intentlabs
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
package com.endlosiot.common.user.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.aop.Authorization;
import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.enums.RoleTypeEnum;
import com.endlosiot.common.user.operation.RoleOperation;
import com.endlosiot.common.user.view.RoleModuleRightsView;
import com.endlosiot.common.user.view.RoleView;
import com.endlosiot.common.view.KeyValueView;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller maps all Role related APIs.
 *
 * @author Nirav.Shah
 * @since 08/02/2018
 */
@RestController
@RequestMapping("/private/role")
public class RolePrivateController extends AbstractController<RoleView> {
    @Autowired
    private RoleOperation roleOperation;

    @Override
    public BaseOperation<RoleView> getOperation() {
        return roleOperation;
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.ROLE, rights = RightsEnum.ADD)
    public ResponseEntity<Response> save(@RequestBody RoleView roleView) throws EndlosiotAPIException {
        if (roleView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(roleView);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(roleOperation.doSave(roleView));
        } catch (ConstraintViolationException constraintViolationException) {
            LoggerService.exception(constraintViolationException);
            throw new EndlosiotAPIException(ResponseCode.ROLE_ALREADY_EXIST.getCode(),
                    ResponseCode.ROLE_ALREADY_EXIST.getMessage());
        }
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.ROLE, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> update(@RequestBody RoleView roleView) throws EndlosiotAPIException {
        if (roleView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        if (roleView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(roleView);
        return ResponseEntity.status(HttpStatus.OK).body(roleOperation.doUpdate(roleView));
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.ROLE, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(@RequestBody RoleView roleView, Integer start, Integer recordSize)
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(roleOperation.doSearch(roleView, start, recordSize));
    }

    @Override
    public void isValidSaveData(RoleView roleView) throws EndlosiotAPIException {
        validateModuleRights(roleView);
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.ROLE, rights = RightsEnum.VIEW)
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(roleOperation.doView(id));
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.ROLE, rights = RightsEnum.DELETE)
    public ResponseEntity<Response> delete(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(roleOperation.doDelete(id));
        } catch (PersistenceException persistenceException) {
            LoggerService.exception(persistenceException);
            throw new EndlosiotAPIException(ResponseCode.CANT_DELETE_ROLE.getCode(),
                    ResponseCode.CANT_DELETE_ROLE.getMessage());
        }
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.ROLE, rights = RightsEnum.ACTIVATION)
    public ResponseEntity<Response> activeInActive(@PathVariable Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    /**
     * This method is used get list of roles without record size.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/drop-down")
    public ResponseEntity<Response> dropDown() throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(roleOperation.doDropdown());
    }

    /**
     * This method is used get list of roles type without record size.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/dropdown-type")
    @ResponseBody
    public ResponseEntity<Response> dropdownType() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        RoleTypeEnum.MAP.forEach((key, value) -> {
            keyValueViews.add(KeyValueView.create(key, value.getName()));
        });
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }

    /**
     * This method is used to validate module & rights data.
     *
     * @param roleView
     * @throws EndlosiotAPIException
     */
    private void validateModuleRights(RoleView roleView) throws EndlosiotAPIException {
        if (roleView.getRoleModuleRightsViews() == null
                || (roleView.getRoleModuleRightsViews() != null && roleView.getRoleModuleRightsViews().isEmpty())) {
            throw new EndlosiotAPIException(ResponseCode.MODULE_RIGHT_IS_MISSING.getCode(),
                    ResponseCode.MODULE_RIGHT_IS_MISSING.getMessage());
        }
        for (RoleModuleRightsView roleModuleRightsView : roleView.getRoleModuleRightsViews()) {
            if (roleModuleRightsView.getModuleView() == null) {
                throw new EndlosiotAPIException(ResponseCode.MODULE_IS_MISSING.getCode(),
                        ResponseCode.MODULE_IS_MISSING.getMessage());
            }
            if (roleModuleRightsView.getRightsView() == null) {
                throw new EndlosiotAPIException(ResponseCode.RIGHT_IS_MISSING.getCode(),
                        ResponseCode.RIGHT_IS_MISSING.getMessage());
            }
            ModuleEnum moduleEnum = ModuleEnum.fromId(roleModuleRightsView.getModuleView().getId());
            if (moduleEnum == null) {
                throw new EndlosiotAPIException(ResponseCode.MODULE_IS_INVALID.getCode(),
                        ResponseCode.MODULE_IS_INVALID.getMessage());
            }
            if (RightsEnum.fromId(roleModuleRightsView.getRightsView().getId()) == null) {
                throw new EndlosiotAPIException(ResponseCode.RIGHT_IS_INVALID.getCode(),
                        ResponseCode.RIGHT_IS_INVALID.getMessage());
            }
        }
    }

    @AccessLog
    @Authorization(modules = ModuleEnum.ROLE, rights = RightsEnum.ADD)
    public Response add() throws EndlosiotAPIException {
        return CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage());
    }
}