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
package com.endlosiot.common.model;

import lombok.Data;

/**
 * This is Jwt Token model which maps jwt token references for every request.
 *
 * @author Jaydip
 * @since 13/03/2021
 */
@Data
public class JwtTokenModel {

    private boolean isResetPasswordToken;
    private boolean isActivationToken;
    private boolean isLoginToken;
    private boolean isCaptchaToken;
    private boolean isTwoFactorToken;
    private String uniqueToken;
    private boolean isFirstLoginToken;
    private boolean isRegistrationToken;

    public JwtTokenModel() {
        super();
    }

    public JwtTokenModel(boolean isResetPasswordToken, boolean isActivationToken, boolean isLoginToken,
                         boolean isCaptchaToken, boolean isTwoFactorToken, boolean isRegistrationToken, boolean isFirstLoginToken) {
        super();
        this.isResetPasswordToken = isResetPasswordToken;
        this.isActivationToken = isActivationToken;
        this.isLoginToken = isLoginToken;
        this.isCaptchaToken = isCaptchaToken;
        this.isTwoFactorToken = isTwoFactorToken;
        this.isRegistrationToken = isRegistrationToken;
        this.isFirstLoginToken = isFirstLoginToken;
    }

    public static JwtTokenModel createResetPasswordToken() {
        return new JwtTokenModel(true, false, false, false, false, false, false);
    }

    public static JwtTokenModel createActivationToken() {
        return new JwtTokenModel(false, true, false, false, false, false, false);
    }

    public static JwtTokenModel createLoginToken() {
        return new JwtTokenModel(false, false, true, false, false, false, false);
    }

    public static JwtTokenModel createCaptchaToken() {
        return new JwtTokenModel(false, false, false, true, false, false, false);
    }

    public static JwtTokenModel createTwoFactorToken() {
        return new JwtTokenModel(false, false, false, false, true, false, false);
    }

    public static JwtTokenModel createRegistrationToken() {
        return new JwtTokenModel(false, false, false, false, false, true, false);
    }

    public static JwtTokenModel createFirstTimeLoginToken() {
        return new JwtTokenModel(false, false, false, false, false, false, true);
    }

}
