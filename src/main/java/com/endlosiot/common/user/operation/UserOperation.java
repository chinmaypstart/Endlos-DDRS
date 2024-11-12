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
package com.endlosiot.common.user.operation;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.view.UserView;

/**
 * @author Hemil.Shah
 * @since 01/05/2023
 */
public interface UserOperation extends BaseOperation<UserView> {

    /**
     * Validate users credential, session and device to allow him to login into a
     * system.
     *
     * @param userView
     * @return
     * @throws EndlosiotAPIException
     */
    Response doLogin(UserView userView, boolean socialLogin) throws EndlosiotAPIException;

    /**
     * This method is used to remove user's auth token.
     *
     * @param jwtAccessToken
     * @return
     * @throws EndlosiotAPIException
     */
    Response doLogout(String jwtAccessToken) throws EndlosiotAPIException;

    /**
     * It is used to validate user's email id and activate his/her account.
     *
     * @param token
     * @return
     * @throws EndlosiotAPIException
     */
    Response doActivate(String token) throws EndlosiotAPIException;

    /**
     * This method is used to send reset password link.
     *
     * @param userView
     * @param isLoginThroughEmail
     * @return
     * @throws EndlosiotAPIException
     */
    Response doSendResetLink(UserView userView, boolean isLoginThroughEmail) throws EndlosiotAPIException;

    /**
     * This method is used to reset user's password.
     *
     * @param userView
     * @return
     * @throws EndlosiotAPIException
     */
    Response doResetPassword(UserView userView) throws EndlosiotAPIException;

    /**
     * This method is used to change user's password.
     *
     * @param userView
     * @return
     * @throws EndlosiotAPIException
     */
    Response doChangePassword(UserView userView) throws EndlosiotAPIException;

    /**
     * This method is used to get islogged in.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    Response doIsLoggedIn() throws EndlosiotAPIException;

    /**
     * this method will use to get new access token.
     *
     * @param userView
     * @return
     * @throws EndlosiotAPIException
     */
    Response doGetAccessToken(UserView userView) throws EndlosiotAPIException;

    /**
     * This method validates User's reset password token.
     *
     * @param otp
     * @return
     * @throws EndlosiotAPIException
     */
    Response doResetPasswordVerification(String otp) throws EndlosiotAPIException;

    /**
     * It is used to resend password verification otp.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    Response doResendPasswordVerificationOtp() throws EndlosiotAPIException;
//	Response doGetCustomer(Long id) throws LiveUniqueLifeAPIException;


    /**
     * It is used to send resent email.
     *
     * @param userView
     * @param isLoginThroughEmail
     * @return
     * @throws EndlosiotAPIException
     */
    Response doSendResentEmail(UserView userView, boolean isLoginThroughEmail) throws EndlosiotAPIException;

    /**
     * This method is used unlock account.
     *
     * @param id
     * @return
     * @throws EndlosiotAPIException
     */
    Response doUnlockAccount(Long id) throws EndlosiotAPIException;
}