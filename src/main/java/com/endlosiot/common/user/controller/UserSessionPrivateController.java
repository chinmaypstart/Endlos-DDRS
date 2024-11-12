package com.endlosiot.common.user.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.aop.Authorization;
import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.operation.UserSessionOperation;
import com.endlosiot.common.user.view.UserSessionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller maps all user session related apis.
 *
 * @author
 * @since
 */
@RestController
@RequestMapping("private/user-session")
public class UserSessionPrivateController extends AbstractController<UserSessionView> {

    @Autowired
    UserSessionOperation userSessionOperation;

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.VIEW)
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userSessionOperation.doView(id));
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(@RequestBody UserSessionView userSessionView,
                                           @RequestParam(name = "start", required = true) Integer start,
                                           @RequestParam(name = "recordSize", required = true) Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userSessionOperation.doSearch(userSessionView, start, recordSize));
    }

    @Override
    public void isValidSaveData(UserSessionView view) throws EndlosiotAPIException {
    }

    @Override
    public BaseOperation<UserSessionView> getOperation() {
        return userSessionOperation;
    }
}