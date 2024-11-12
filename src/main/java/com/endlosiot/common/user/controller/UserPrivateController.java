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
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.threadlocal.Auditor;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.operation.UserOperation;
import com.endlosiot.common.user.view.RoleView;
import com.endlosiot.common.user.view.UserView;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller maps all user related apis.
 *
 * @author Hemil.Shah
 * @since 01/05/2023
 */
@RestController
@RequestMapping("private/user")
public class UserPrivateController extends AbstractController<UserView> {

    @Autowired
    private UserOperation userOperation;

    @Override
    public BaseOperation<UserView> getOperation() {
        return userOperation;
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.ADD)
    public ResponseEntity<Response> save(@RequestBody UserView userView) throws EndlosiotAPIException {
        if (userView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(userView);
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doSave(userView));
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.VIEW)
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doView(id));
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.UPDATE)
    public ResponseEntity<Response> update(@RequestBody UserView userView) throws EndlosiotAPIException {
        if (userView == null || userView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidUpdateData(userView);
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doUpdate(userView));
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.DELETE)
    public ResponseEntity<Response> delete(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        if (Auditor.getAuditor().getId().equals(id)) {
            throw new EndlosiotAPIException(ResponseCode.NOT_ALLOWED_DELETE_OWN_ACCOUNT.getCode(),
                    ResponseCode.NOT_ALLOWED_DELETE_OWN_ACCOUNT.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doDelete(id));
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(@RequestBody UserView userView,
                                           @RequestParam(name = "start", required = true) Integer start,
                                           @RequestParam(name = "recordSize", required = true) Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doSearch(userView, start, recordSize));
    }

    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.LIST)
    @GetMapping(value = "/display")
    public ResponseEntity<Response> displayGrid(@RequestParam(name = "start", required = true) Integer start,
                                                @RequestParam(name = "recordSize", required = true) Integer recordSize) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.ACTIVATION)
    public ResponseEntity<Response> activeInActive(@PathVariable Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doActiveInActive(id));
    }

    /**
     * Remove user's current session.
     *
     * @param httpServletRequest
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/logout")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> logout(HttpServletRequest httpServletRequest) throws EndlosiotAPIException {
        String jwtAccesssTokenHeader = httpServletRequest.getHeader("Authorization");
        String[] accessToken = jwtAccesssTokenHeader.split(" ");
        String jwtAccessToken = accessToken[1];
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doLogout(jwtAccessToken));
    }

    /**
     * This method is used to set a new password.
     *
     * @param userView
     * @return
     * @throws EndlosiotAPIException
     */
    @PostMapping(value = "/reset-password")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> resetPassword(@RequestBody UserView userView) throws EndlosiotAPIException {
        validateResetPasswordRequest(userView);
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doResetPassword(userView));
    }

    /**
     * This method is used to change user's password.
     *
     * @param userView
     * @return
     * @throws EndlosiotAPIException
     */
    @PostMapping(value = "/change-password")
    @ResponseBody
    public ResponseEntity<Response> changePassword(@RequestBody UserView userView) throws EndlosiotAPIException {
        validateResetPasswordRequest(userView);
        UserView.validatePassword(userView.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doChangePassword(userView));
    }

    private void validateResetPasswordRequest(UserView userView) throws EndlosiotAPIException {
        if (userView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        UserView.validatePassword(userView.getPassword());
        UserView.validatePassword(userView.getConfirmPassword());
        if (!userView.getPassword().equals(userView.getConfirmPassword())) {
            throw new EndlosiotAPIException(ResponseCode.PASSWORD_NOT_MATCH.getCode(),
                    ResponseCode.PASSWORD_NOT_MATCH.getMessage());
        }
    }

    /**
     * This method is used to check session of user.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/is-loggedIn")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> isLoggedIn() throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doIsLoggedIn());
    }

    /**
     * this api will use to get new access token when it is expired.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @PostMapping(value = "/get-access-token")
    @ResponseBody
    public ResponseEntity<Response> getAccessToken(@RequestBody UserView userView) throws EndlosiotAPIException {
        if (userView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        if (StringUtils.isBlank(userView.getAccessToken()) || StringUtils.isBlank(userView.getRefreshToken())) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doGetAccessToken(userView));
    }

    @Override
    public void isValidSaveData(UserView userView) throws EndlosiotAPIException {
        UserView.isValid(userView);
        if (userView.getRoleViews() == null || userView.getRoleViews().isEmpty()) {
            throw new EndlosiotAPIException(ResponseCode.ROLE_IS_MISSING.getCode(),
                    ResponseCode.ROLE_IS_MISSING.getMessage());
        }
        for (RoleView roleView : userView.getRoleViews()) {
            if (roleView != null && roleView.getId() == null) {
                throw new EndlosiotAPIException(ResponseCode.ROLE_IS_MISSING.getCode(),
                        ResponseCode.ROLE_IS_MISSING.getMessage());
            }
        }
    }

    public void isValidUpdateData(UserView userView) throws EndlosiotAPIException {
        UserView.isValid(userView);
        if (userView.getRoleViews() != null && !userView.getRoleViews().isEmpty()) {
            for (RoleView roleView : userView.getRoleViews()) {
                if (roleView != null && roleView.getId() == null) {
                    throw new EndlosiotAPIException(ResponseCode.ROLE_IS_MISSING.getCode(),
                            ResponseCode.ROLE_IS_MISSING.getMessage());
                }
            }
        }
    }

    /**
     * This method is used to resend password verification otp.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/resend-password-verification-otp")
    @AccessLog
    public ResponseEntity<Response> resendPasswordVerificationOtp() throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doResendPasswordVerificationOtp());
    }

    /**
     * This method is used to reset password verification.
     *
     * @return
     * @throws EndlosiotAPIException
     */

    @GetMapping(value = "/reset-password-verification")
    @ResponseBody
    public ResponseEntity<Response> resetPasswordVerification(@RequestParam("resetPasswordVerification") String otp)
            throws EndlosiotAPIException {
        if (StringUtils.isBlank(otp)) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doResetPasswordVerification(otp));
    }
    @GetMapping(value = "/add")
    @AccessLog
    @Authorization(modules = ModuleEnum.USER, rights = RightsEnum.ADD)
    public Response add() throws EndlosiotAPIException {
        return CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage());
    }
}