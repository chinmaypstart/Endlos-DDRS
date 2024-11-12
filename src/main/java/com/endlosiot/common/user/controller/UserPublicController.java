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
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.operation.UserOperation;
import com.endlosiot.common.user.view.UserView;
import com.endlosiot.common.util.Utility;
import com.endlosiot.common.validation.RegexEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller maps all user related apis.
 *
 * @author Dhruvang.Joshi
 * @since 24/11/2018
 */
@RestController
@RequestMapping("public/user")
public class UserPublicController {

    @Autowired
    private UserOperation userOperation;

    @PostMapping("/login")
    @AccessLog
    public ResponseEntity<Response> login(@RequestBody UserView userView) throws EndlosiotAPIException {
        if (userView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        boolean isLoginThroughEmail = isValidLoginDetail(userView);
        if (StringUtils.isEmpty(userView.getPassword())) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_EMAIL_OR_MOBILE_NUMBER.getCode(),
                    ResponseCode.INVALID_EMAIL_OR_MOBILE_NUMBER.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doLogin(userView, isLoginThroughEmail));
    }

    /**
     * To Get Reset Password link.
     *
     * @param userView
     * @return
     * @throws EndlosiotAPIException
     */
    @PostMapping(value = "/send-reset-link")
    @AccessLog
    public ResponseEntity<Response> sendResetLink(@RequestBody UserView userView) throws EndlosiotAPIException {
        if (userView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        boolean isLoginThroughEmail = isValidLoginDetail(userView);
//		if (captchaConfigProperty.isCaptchaValidation()) {
//			if (userView.getCaptchaResponseView() != null
//					&& !StringUtils.isBlank(userView.getCaptchaResponseView().getCaptchaResponseToken())) {
//				CaptchaUtility.verify(userView.getCaptchaResponseView().getCaptchaResponseToken(),
//						captchaConfigProperty.getCaptchaSiteVerifyUrl(), captchaConfigProperty.getSecretKey());
//			}
//		}
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doSendResetLink(userView, isLoginThroughEmail));
    }

    /**
     * This method is used to validate user's email id.
     *
     * @param token
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/activate")
    @ResponseBody
    public ResponseEntity<Response> activate(@RequestParam("verificationToken") String token)
            throws EndlosiotAPIException {
        if (StringUtils.isBlank(token)) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doActivate(token));
    }

    /**
     * This method is used to validate password.
     *
     * @param userView
     * @return
     * @throws EndlosiotAPIException
     */
    @PostMapping(value = "/validate-password")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> validatePassword(@RequestBody UserView userView) throws EndlosiotAPIException {
        UserView.validatePassword(userView.getPassword());
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage()));
    }

    private boolean isValidLoginDetail(UserView userView) throws EndlosiotAPIException {
        if (StringUtils.isBlank(userView.getLoginId())) {
            throw new EndlosiotAPIException(ResponseCode.EMAIL_MOBILE_IS_MISSING.getCode(),
                    ResponseCode.EMAIL_MOBILE_IS_MISSING.getMessage());
        }
        boolean isLoginThroughEmail = false;
        if (Utility.isValidPattern(userView.getLoginId(), RegexEnum.EMAIL.getValue())) {
            isLoginThroughEmail = true;
        }
        if (!isLoginThroughEmail
                && !Utility.isValidPattern(userView.getLoginId(), RegexEnum.MOBILE_NUMBER.getValue())) {
            throw new EndlosiotAPIException(ResponseCode.EMAIL_MOBILE_IS_MISSING.getCode(),
                    ResponseCode.EMAIL_MOBILE_IS_MISSING.getMessage());
        }
        return isLoginThroughEmail;
    }

    @GetMapping(value = "/get-customer")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> getCustomer(@RequestParam(name = "id", required = true) Long id)
            throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOperation.doView(id));
    }

    @PostMapping("/resent-email")
    @AccessLog
    public ResponseEntity<Response> sendResetEmail(@RequestBody UserView userView) throws EndlosiotAPIException {
        if (userView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        boolean isLoginThroughEmail = isValidLoginDetail(userView);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userOperation.doSendResentEmail(userView, isLoginThroughEmail));
    }
}