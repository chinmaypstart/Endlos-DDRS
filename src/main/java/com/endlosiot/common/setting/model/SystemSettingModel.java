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
package com.endlosiot.common.setting.model;

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.common.modelenums.CommonStatusEnum;
import com.endlosiot.common.validation.DataType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is System Setting model which maps system setting list table to
 * class.This model contains common configuration and map their values. This is
 * applicable to all users of the system.This configuration is not specific to
 * any client.
 *
 * @author Nirav
 * @since 20/12/2019
 */

@Entity(name = "systemSettingModel")
@Table(name = "systemsetting")
public class SystemSettingModel extends IdentifierModel {

    private static final long serialVersionUID = 6653648434546572167L;

    @Column(name = "key", nullable = false, length = 100)
    private String key;

    @Column(name = "value", nullable = false, length = 1000)
    private String value;

    @Column(name = "enumdatatype", nullable = false)
    private Integer dataType;

    private static Map<String, String> systemSettings = new ConcurrentHashMap<>();

    // Account Lock settings When Wrong Attempts

    private static final String LOCK_USER_ACCOUNT_ON_FAILED_ATTEMPTS = "LOCK_USER_ACCOUNT_ON_FAILED_ATTEMPTS";
    private static final String FAILED_LOGIN_ATTEMPT_COUNT = "FAILED_LOGIN_ATTEMPT_COUNT";
    private static final String UNLOCK_ACCOUNT_TIME_IN_HOURS = "UNLOCK_ACCOUNT_TIME_IN_HOURS";

    // default

    private static final String DEFAULT_FILE_PATH = "DEFAULT_FILE_PATH";
    private static final String RESET_PASSWORD_TOKEN_VALID_MINUTES = "RESET_PASSWORD_TOKEN_VALID_MINUTES";

    // On User Create Default
    private static final String DEFAULT_PASSWORD_CHANGE_REQUIRED = "DEFAULT_PASSWORD_CHANGE_REQUIRED";

    // password Syntax checking config
    private static final String PASSWORD_GENERATION_SYNTAX_CHECKING_ENABLED = "PASSWORD_GENERATION_SYNTAX_CHECKING_ENABLED";
    private static final String PASSWORD_GENERATION_MIN_LOWER_CASE_ALPHABETS = "PASSWORD_GENERATION_MIN_LOWER_CASE_ALPHABETS";
    private static final String PASSWORD_GENERATION_MIN_UPPER_CASE_ALPHABETS = "PASSWORD_GENERATION_MIN_UPPER_CASE_ALPHABETS";
    private static final String PASSWORD_GENERATION_MIN_SPECIAL_CHARACTERS = "PASSWORD_GENERATION_MIN_SPECIAL_CHARACTERS";
    private static final String PASSWORD_GENERATION_MIN_NUMERICS = "PASSWORD_GENERATION_MIN_NUMERICS";
    private static final String PASSWORD_GENERATION_MIN_LENGTH = "PASSWORD_GENERATION_MIN_LENGTH";
    private static final String PASSWORD_GENERATION_MAX_LENGTH = "PASSWORD_GENERATION_MAX_LENGTH";

    private static final String PASSWORD_USED_VALIDATION_ENABLED = "PASSWORD_USED_VALIDATION_ENABLED";
    private static final String MAX_PASSWORD_STORE_COUNT_PER_USER = "MAX_PASSWORD_STORE_COUNT_PER_USER";
    private static final String PASSWORD_EXPIRATION_MAX_AGE_NEEDED = "PASSWORD_EXPIRATION_MAX_AGE_NEEDED";
    private static final String PASSWORD_EXPIRATION_MAX_AGE_DAYS = "PASSWORD_EXPIRATION_MAX_AGE_DAYS";

    private static final String SECRET_KEY_FOR_GENERATE_JWT_TOKEN = "SECRET_KEY_FOR_GENERATE_JWT_TOKEN";
    private static final String GENERATE_REFRESH_TOKEN_TIME_IN_MINUTES = "GENERATE_REFRESH_TOKEN_TIME_IN_MINUTES";
    private static final String REGISTRATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES = "REGISTRATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES";
    private static final String RESET_PASSWORD_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES = "RESET_PASSWORD_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES";
    private static final String CAPTCHA_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES = "CAPTCHA_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES";
    private static final String ACTIVATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES = "ACTIVATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES";
    private static final String REFRESH_JWT_TOKEN_EXPIRY_TIME_IN_DAY = "REFRESH_JWT_TOKEN_EXPIRY_TIME_IN_DAY";
    private static final String ACCESS_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES = "ACCESS_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES";
    private static final String TWO_FACTOR_TOKEN_EXPIRY_TIME_IN_MINUTES = "TWO_FACTOR_TOKEN_EXPIRY_TIME_IN_MINUTES";
    private static final String FIRST_LOGIN_TOKEN_EXPIRY_TIME_IN_MINUTES = "FIRST_LOGIN_TOKEN_EXPIRY_TIME_IN_MINUTES";

    // Two factor authentication
    private static final String TWO_FACTOR_AUTHENTICATION_ENABLED = "TWO_FACTOR_AUTHENTICATION_ENABLED";
    private static final String MAX_ALLOWED_DEVICE = "MAX_ALLOWED_DEVICE";
    private static final String DEVICE_COOKIE_TIME_IN_SECONDS = "DEVICE_COOKIE_TIME_IN_SECONDS";

    private static final String SET_PASSWORD_TOKEN_EXPIRE_IN_DAYS = "SET_PASSWORD_TOKEN_EXPIRE_IN_DAYS";
    private static final String RESET_PASSWORD_DAY_WISE_LIMIT = "RESET_PASSWORD_DAY_WISE_LIMIT";
    private static final String RESET_PASSWORD_RESEND_LIMIT = "RESET_PASSWORD_RESEND_LIMIT";
    private static final String URL = "URL";

    private static final String MIN_WEIGHT = "MIN_WEIGHT";
    private static final String MAX_WEIGHT = "MAX_WEIGHT";
    private static final String MIN_DELIVERY_CHARGE = "MIN_DELIVERY_CHARGE";
    private static final String IN_BETWEEN_DELIVERY_CHARGE = "IN_BETWEEN_DELIVERY_CHARGE";
    private static final String MAX_DELIVERY_CHARGE = "MAX_DELIVERY_CHARGE";
    private static final String OTP_VERIFICATION_VALID_MINUTE = "OTP_VERIFICATION_VALID_MINUTE";


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataType getDataType() {
        return DataType.getFromId(this.dataType);
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType.getId();
    }

    public static void add(SystemSettingModel systemSettingModel) {
        systemSettings.put(systemSettingModel.getKey(), systemSettingModel.getValue());
    }

    public static void remove(String key) {
        systemSettings.remove(key);
    }

    public static String get(String key) {
        return systemSettings.get(key);
    }

    public static Long getMaxAllowedDevice() {
        return Long.valueOf(systemSettings.get(MAX_ALLOWED_DEVICE));
    }

    public static Integer getDeviceCookieTimeInSeconds() {
        return Integer.valueOf(systemSettings.get(DEVICE_COOKIE_TIME_IN_SECONDS));
    }

    public static Long getResetPasswordTokenValidMinutes() {
        return Long.valueOf(systemSettings.get(RESET_PASSWORD_TOKEN_VALID_MINUTES));
    }

    public static CommonStatusEnum getPasswordUsedValidationEnabled() {
        return CommonStatusEnum.fromId(Integer.valueOf(systemSettings.get(PASSWORD_USED_VALIDATION_ENABLED)));
    }

    public static Integer getMaxPasswordStoreCountPerUser() {
        return Integer.valueOf(systemSettings.get(MAX_PASSWORD_STORE_COUNT_PER_USER));
    }

    public static String getDefaultFilePath() {
        return systemSettings.get(DEFAULT_FILE_PATH);
    }

    public static CommonStatusEnum getDefaultPasswordChangeRequired() {
        return CommonStatusEnum.fromId(Integer.valueOf(systemSettings.get(DEFAULT_PASSWORD_CHANGE_REQUIRED)));
    }

    public static CommonStatusEnum getPasswordExpirationMaxAgeNeeded() {
        return CommonStatusEnum.fromId(Integer.valueOf(systemSettings.get(PASSWORD_EXPIRATION_MAX_AGE_NEEDED)));
    }

    public static Long getPasswordExpirationMaxAgeDays() {
        return Long.valueOf(systemSettings.get(PASSWORD_EXPIRATION_MAX_AGE_DAYS));
    }

    public static Integer getPasswordGenerationMinLength() {
        return Integer.valueOf(systemSettings.get(PASSWORD_GENERATION_MIN_LENGTH));
    }

    public static Integer getPasswordGenerationMaxLength() {
        return Integer.valueOf(systemSettings.get(PASSWORD_GENERATION_MAX_LENGTH));
    }

    public static Integer getPasswordGenerationMinLowerCaseAlphabets() {
        return Integer.valueOf(systemSettings.get(PASSWORD_GENERATION_MIN_LOWER_CASE_ALPHABETS));
    }

    public static Integer getPasswordGenerationMinUpperCaseAlphabets() {
        return Integer.valueOf(systemSettings.get(PASSWORD_GENERATION_MIN_UPPER_CASE_ALPHABETS));
    }

    public static Integer getPasswordGenerationMinSpecialCharacters() {
        return Integer.valueOf(systemSettings.get(PASSWORD_GENERATION_MIN_SPECIAL_CHARACTERS));
    }

    public static Integer getPasswordGenerationMinNumerics() {
        return Integer.valueOf(systemSettings.get(PASSWORD_GENERATION_MIN_NUMERICS));
    }

    public static String getSecretKeyForGenerateJWTToken() {
        return systemSettings.get(SECRET_KEY_FOR_GENERATE_JWT_TOKEN);
    }

    public static Integer getGenerateRefreshTokenTimeInMinutes() {
        return Integer.valueOf(systemSettings.get(GENERATE_REFRESH_TOKEN_TIME_IN_MINUTES));
    }

    public static Integer getRegistrationJwtTokenExpiryTimeInMinutes() {
        return Integer.valueOf(systemSettings.get(REGISTRATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES));
    }

    public static Integer getResetPasswordJwtTokenExpiryTimeInMinutes() {
        return Integer.valueOf(systemSettings.get(RESET_PASSWORD_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES));
    }

    public static Integer getCaptchaJwtTokenExpiryTimeInMinutes() {
        return Integer.valueOf(systemSettings.get(CAPTCHA_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES));
    }

    public static Integer getActivationJwtTokenExpiryTimeInMinutes() {
        return Integer.valueOf(systemSettings.get(ACTIVATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES));
    }

    public static Integer getAccessJwtTokenExpiryTimeInMinutes() {
        return Integer.valueOf(systemSettings.get(ACCESS_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES));
    }

    public static Integer getRefreshJwtTokenExpiryTimeInDay() {
        return Integer.valueOf(systemSettings.get(REFRESH_JWT_TOKEN_EXPIRY_TIME_IN_DAY));
    }

    public static Integer getTwoFactorTokenExpiryTimeInMinutes() {
        return Integer.valueOf(systemSettings.get(TWO_FACTOR_TOKEN_EXPIRY_TIME_IN_MINUTES));
    }

    public static Integer getFirstLoginTokenExpiryTimeInMinutes() {
        return Integer.valueOf(systemSettings.get(FIRST_LOGIN_TOKEN_EXPIRY_TIME_IN_MINUTES));
    }

    public static Integer getPasswordGenerationSyntaxCheckingEnabled() {
        return Integer.valueOf(systemSettings.get(PASSWORD_GENERATION_SYNTAX_CHECKING_ENABLED));
    }

    public static CommonStatusEnum getTwoFactorAuthenticationEnable() {
        return CommonStatusEnum.fromId(Integer.valueOf(systemSettings.get(TWO_FACTOR_AUTHENTICATION_ENABLED)));
    }

    public static Integer getUnlockAccountTimeInHours() {
        return Integer.valueOf(systemSettings.get(UNLOCK_ACCOUNT_TIME_IN_HOURS));
    }

    public static Integer getLockUserAccount() {
        return Integer.valueOf(systemSettings.get(LOCK_USER_ACCOUNT_ON_FAILED_ATTEMPTS));
    }

    public static Integer getFailedLoginAttemptCount() {
        return Integer.valueOf(systemSettings.get(FAILED_LOGIN_ATTEMPT_COUNT));
    }

    public static Long getSetPasswordTokenExpireInDays() {
        return Long.valueOf(systemSettings.get(SET_PASSWORD_TOKEN_EXPIRE_IN_DAYS));
    }

    public static Integer getResetPasswordDayWiseLimit() {
        return Integer.valueOf(systemSettings.get(RESET_PASSWORD_DAY_WISE_LIMIT));
    }

    public static Integer getResetPasswordResendLimit() {
        return Integer.valueOf(systemSettings.get(RESET_PASSWORD_RESEND_LIMIT));
    }

    public static String getUrl() {
        return systemSettings.get(URL);
    }

    public static Long getMinWeight() {
        return Long.valueOf(systemSettings.get(MIN_WEIGHT));
    }

    public static Long getMaxWeight() {
        return Long.valueOf(systemSettings.get(MAX_WEIGHT));
    }

    public static Long getMinDeliveryCharge() {
        return Long.valueOf(systemSettings.get(MIN_DELIVERY_CHARGE));
    }

    public static Long getInBetweenDeliveryCharge() {
        return Long.valueOf(systemSettings.get(IN_BETWEEN_DELIVERY_CHARGE));
    }

    public static Long getMaxDeliveryCharge() {
        return Long.valueOf(systemSettings.get(MAX_DELIVERY_CHARGE));
    }

    public static Long getOtpVerificationValidMinute() {
        return Long.valueOf(systemSettings.get(OTP_VERIFICATION_VALID_MINUTE));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SystemSettingModel other = (SystemSettingModel) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }
}