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
package com.endlosiot.common.enums;

import java.io.Serializable;

/**
 * This is used to give response code & message to vendor request.
 *
 * @author Core team.
 * @version 1.0
 * @since 21/09/2016
 */
public enum ResponseCode implements Serializable {

    SUCCESSFUL(1000, "Successful"), SAVE_SUCCESSFULLY(1001, "Data saved successfully."),
    UPDATE_SUCCESSFULLY(1002, "Data updated successfully."), DELETE_SUCCESSFULLY(1003, "Data deleted successfully."),
    RESENT_VERIFICATION_LINK_SUCCESSFULLY(1004, "A verification email has been resent. Please check your email for further details."),
    CHANGE_PASSWORD_SUCCESSFUL(1005, "You have changed your password successfully. Please login into system using it."),
    LOGGED_OUT_SUCCESSFUL(1006, "You have successfully logged out."),
    ACTIVATION_SUCCESSFUL(1007, "Your account has been activated successfully."),
    RESET_PASSWORD_SUCCESSFUL(1008, "You have successfully reset your password. Please login into system using it."),
    FORGET_PASSWORD_VERIFICATION_SUCCESSFUL(1009, "Your token for forget password verified successfully, You can now change your password."),
    FORGET_PASSWORD_SUCCESSFUL(1010, "If the email address is registered with us, we’ll send you an email with instructions to reactivate your account."),

    RESENT_PASSWORD_VERIFICATION_OTP_SUCCESSFULLY(1015,
            "Reset Password Verification OTP is resent successfully on your registered mobile / Email ID."),


    // Any code above 2000 is an error code.
    INTERNAL_SERVER_ERROR(2000, "System is unable to process the request."),
    INVALID_REQUEST(2001, "Invalid request."),

    // Common

    INVALID_EMAIL_OR_MOBILE_NUMBER(2002, "Incorrect email or mobile. Please re-enter your email and mobile."),
    EMAIL_VERIFICATION(2003, "Please verify your email account to continue."),
    AUTHENTICATION_REQUIRED(2004, "Authentication is required to access requested resource."),

    LOGO_IS_INVALID(2005, "Logo Is Invalid"),
    SUBJECT_IS_MISSING(2006, "Subject is missing"),
    SUBJECT_MAX_LENGTH_EXCEED(2007, "Max 1000 characters are allowed in subject."),
    EMAIL_BCC_IS_INVALID(2008, "Email bcc is invalid"),
    EMAIL_CC_IS_INVALID(2009, "Email cc is invalid"),

    //JWTUtil
    INVALID_JSON(2010, "Invalid json format."),
    INVALID_JSON_TOKEN(2011, "Invalid JSON Token"),
    EXPIRED_TOKEN(2012, "Link is expired or have already been used"),
    EXPIRED_JSON_TOKEN(2013, "Expired JSON Token"),
    INVALID_LOGINID_OR_PASSWORD(2014, "Incorrect email or password. Please re-enter your email and password."),

    // Module
    MODULE_IS_INVALID(2015, "Module is invalid"),
    MODULE_IS_MISSING(2016, "Module is missing"),
    MODULE_RIGHT_IS_MISSING(2017, "Module & Rights is missing"),

    // Role
    ROLE_ALREADY_EXIST(2018, "Role already exist"),
    ROLE_TYPE_IS_INVALID(2019, "Role type is invalid"),
    RIGHT_IS_INVALID(2020, "Right is invalid"), RIGHT_IS_MISSING(2021, "Right is missing"),
    CANT_DELETE_ROLE(2022, "You can't delete a role as its binded with user(s)."),
    ROLE_IS_INVALID(2023, "The role is invalid"), ROLE_IS_MISSING(2024, "Role is missing"),

    // file
    FILE_ID_IS_MISSING(2025, "FileId is mandatory."),
    FILE_IS_MISSING(2026, "File is mandatory."),
    INVALID_FILE_FORMAT(2027, "Invalid file format"),
    FILE_ID_IS_INVALID(2028, "FileId is invalid."),
    UPLOAD_IMAGE_ONLY(2029, "Please upload image file(s), Other file types are not allowed."),
    UNAUTHORIZED_ACCESS(2030, "You don't have a permission to access it."),
    NO_DATA_FOUND(2031, "No data found"),
    UNABLE_TO_UPLOAD_FILE(2032, "Unable to upload a file."),
    UNABLE_TO_CREATE_THUMBNAIL(2033, "Unable to create thumbnail."),
    UNABLE_TO_DOWNLOAD_FILE(2034, "Unable to download a file."),
    UNABLE_TO_CONVERT_INTO_BASE64(2035, "Unable to convert image into bytes"),

    // Email
    UNABLE_TO_DELETE_EMAIL_ACCOUNT(2036, "Unable to delete it as it's already binded with email content(s)."),
    RATE_PER_HOUR_INVALID(2037, "Rate per hour is invalid"), RATE_PER_DAY_INVALID(2038, "Rate per day is invalid"),
    AUTHENTICATION_METHOD_IS_MISSING(2039, "Authentication method is missing"),
    AUTHENTICATION_METHOD_IS_INVALID(2040, "Authentication method is invalid"),
    AUTHENTICATION_SECURITY_IS_MISSING(2041, "Authentication security is missing"),
    AUTHENTICATION_SECURITY_IS_INVALID(2042, "Authentication security is invalid"),
    TIMEOUT_IS_INVALID(2043, "Timeout is invalid"), EMAIL_CONTENT_ALREADY_EXIST(2044, "Email content already exist"),
    EMAIL_ACCOUNT_ALREADY_EXIST(2045, "Email account already exist"),
    EMAIL_ACCOUNT_IS_INVALID(2046, "Email account is invalid"), EMAIL_ACCOUNT_IS_MISSING(2047, "Email account is missing"),
    EMAIL_CONTENT_IS_MISSING(2048, "Email content is missing"),
    REPLY_TO_IS_MISSING(2049, "Reply To is missing"),
    REPLY_TO_MAX_LENGTH_EXCEED(2050, "Max 100 characters are allowed in Reply To"),
    REPLY_TO_IS_INVALID(2051, "Reply To is invalid"),
    EMAIL_USER_NAME_IS_MISSING(2052, "User Name is missing"),
    EMAIL_USER_NAME_MAX_LENGTH_EXCEED(2053, "Max 100 characters are allowed in User Name"),
    EMAIL_ACCOUNT_NAME_IS_MISSING(2054, "Email Account Name is missing"),
    MAIL_ACCOUNT_NAME_MAX_LENGTH_EXCEED(2055, "Max 100 characters are allowed in Email Account Name"),
    EMAIL_FROM_IS_MISSING(2056, "Email From is missing"), EMAIL_FROM_MAX_LENGTH_EXCEED(2057, "Max 500 characters are allowed in Email From"),
    HOST_IS_MISSING(2058, "Host is missing"), HOST_MAX_LENGTH_EXCEED(2059, "Max 500 characters are allowed in Host"),
    PORT_IS_MISSING(2060, "Port is missing"), PORT_MAX_LENGTH_EXCEED(2061, "Max 65555 characters are allowed in Port"),
    PORT_MIN_LENGTH_EXCEED(2062, "Min 0 characters are allowed in Port"), PORT_IS_INVALID(2063, "Port is invalid"),

    // Notification
    NOTIFICATION_IS_MISSING(2064, "Notification is missing"), NOTIFICATION_IS_INVALID(2065, "Notification is invalid"),

    //SystemSetting
    PASSWORD_EXPIRATION_MAX_AGE_NEEDED_IS_INVALID(2066, "Only numeric value allowed for PASSWORD_EXPIRATION_MAX_AGE_NEEDED"),
    PASSWORD_EXPIRATION_MAX_AGE_DAYS_IS_INVALID(2067, "Only numeric value allowed for PASSWORD_EXPIRATION_MAX_AGE_DAYS"),
    PASSWORD_GENERATION_MIN_LENGTH_IS_INVALID(2068, "Minimum length 8 allowed for PASSWORD_GENERATION_MIN_LENGTH"),
    PASSWORD_GENERATION_MAX_LENGTH_IS_INVALID(2069, "Maximum length 16 allowed for PASSWORD_GENERATION_MAX_LENGTH"),
    PASSWORD_GENERATION_MIN_LOWER_CASE_ALPHABETS_IS_INVALID(2070, "Only numeric value allowed for PASSWORD_GENERATION_MIN_LOWER_CASE_ALPHABETS"),
    PASSWORD_GENERATION_MIN_UPPER_CASE_ALPHABETS_IS_INVALID(2071, "Only numeric value allowed for PASSWORD_GENERATION_MIN_UPPER_CASE_ALPHABETS"),
    PASSWORD_GENERATION_MIN_SPECIAL_CHARACTERS_IS_INVALID(2072, "Only special character allowed for PASSWORD_GENERATION_MIN_SPECIAL_CHARACTERS"),
    PASSWORD_GENERATION_MIN_NUMERICS_IS_INVALID(2073, "Only numeric value allowed for PASSWORD_GENERATION_MIN_NUMERICS"),
    DEFAULT_PASSWORD_CHANGE_REQUIRED_IS_INVALID(2074, "Only boolean value allowed for DEFAULT_PASSWORD_CHANGE_REQUIRED"),
    DEFAULT_PASSWORD_CHANGE(2075, "Password change required"),
    RESET_PASSWORD_TOKEN_VALID_MINUTES_IS_INVALID(2076, "Only numeric value allowed for RESET_PASSWORD_TOKEN_VALID_MINUTES"),
    PASSWORD_USED_VALIDATION_ENABLED_IS_INVALID(2077, "Only boolean value allowed for PASSWORD_USED_VALIDATION_ENABLED"),
    TWO_FACTOR_AUTHENTICATION_ENABLED_IS_INVALID(2078, "Only boolean value allowed for TWO_FACTOR_AUTHENTICATION_ENABLED"),
    DEVICE_COOKIE_TIME_IN_SECONDS_IS_INVALID(2079, "Only numeric value allowed for DEVICE_COOKIE_TIME_IN_SECONDS"),
    SESSION_INACTIVE_TIME_IN_MINUTES_IS_INVALID(2080, "Only numeric value allowed for SESSION_INACTIVE_TIME_IN_MINUTES"),
    MAX_ALLOWED_DEVICE_IS_INVALID(2081, "Only numeric value allowed for MAX_ALLOWED_DEVICE"),
    CAPTCHA_IMAGE_PATH_IS_INVALID(2082, "Only string value allowed for CAPTCHA_IMAGE_PATH"),
    DEFAULT_FILE_PATH_IS_INVALID(2083, "Only string value allowed for DEFAULT_FILE_PATH"),
    ADMIN_URL_IS_INVALID(2084, "Only string value allowed for ADMIN_URL"),
    RESET_PASSWORD_TOKEN_VALID_MINUTES_IS_MISSING(2085, "RESET_PASSWORD_TOKEN_VALID_MINUTES is mandatory"),
    MAX_PASSWORD_STORE_COUNT_PER_USER_IS_MISSING(2086, "MAX_PASSWORD_STORE_COUNT_PER_USER is mandatory"),
    PASSWORD_GENERATION_MIN_LENGTH_IS_MISSING(2087, "PASSWORD_GENERATION_MIN_LENGTH is mandatory"),
    PASSWORD_GENERATION_MAX_LENGTH_IS_MISSING(2088, "PASSWORD_GENERATION_MAX_LENGTH is mandatory"),
    PASSWORD_GENERATION_MIN_LOWER_CASE_ALPHABETS_IS_MISSING(2089, "PASSWORD_GENERATION_MIN_LOWER_CASE_ALPHABETS is mandatory"),
    PASSWORD_GENERATION_MIN_UPPER_CASE_ALPHABETS_IS_MISSING(2090, "PASSWORD_GENERATION_MIN_UPPER_CASE_ALPHABETS is mandatory"),
    PASSWORD_GENERATION_MIN_SPECIAL_CHARACTERS_IS_MISSING(2091, "PASSWORD_GENERATION_MIN_SPECIAL_CHARACTERS is mandatory"),
    PASSWORD_GENERATION_MIN_NUMERICS_IS_MISSING(2092, "PASSWORD_GENERATION_MIN_NUMERICS is mandatory"),
    PASSWORD_EXPIRATION_MAX_AGE_DAYS_IS_MISSING(2093, "PASSWORD_EXPIRATION_MAX_AGE_DAYS is mandatory"),
    CAN_NOT_CHANGE_OWN_ROLE(2094, "You can't change own role."),
    PASSWORD_EXPIRED(2095, "Your password has been expired, Please change your password."),
    TEMP_PASSWORD_SESSION(2096, "For Access the portal you need to change password."),
    PASSWORD_IS_INVALID(2097, "Password should be minimum of 8 characters and maximum of 16 characters containing at least one capital, number and a special character."),
    EMAIL_MOBILE_IS_MISSING(2098, "Enter a valid email address to proceed."),
    LEFT_ATTEMPTS(2099, "Incorrect email or password. You’ve ${remainingAttempt} attempts left before your account is locked."),
    INACTIVE_USER(2100, "Your account has been inactivated."),
    INVALID_REFRESH_JSON_TOKEN(2101, "Invalid Refresh Token"),
    DUPLICATE_PASSWORD_USER(2102, "The password entered is one of your last used password.Please provide a new password."),
    CURRENT_PASSWORD_IS_INVALID(2103, "Incorrect Password."),
    VALIDATE_NEW_DEVICE(2104, " Login detected from a new device."),
    DUPLICATE_MOBILE_USER(2105, "Your mobile number is already registered with us."),
    DUPLICATE_EMAIL_USER(2106, "You’re already registered with us. To reset your password please go to “Forgot Your Password” screen and enter your email address."),
    CAN_NOT_CHANGE_OWN_ACTIVATION_STATUS(2107, "You can't change own activation status."),
    NOT_ALLOWED_DELETE_OWN_ACCOUNT(2108, "You can't delete your own account."),
    PASSWORD_NOT_MATCH(2109, "New and Confirm password do not match."),
    NAME_IS_MISSING(2110, "Name is missing"), NAME_MAX_LENGTH_EXCEED(2111, "Max 30 characters are allowed in name"),
    NAME_IS_INVALID(2112, "Name is invalid"),
    EMAIL_IS_MISSING(2113, "Email id is missing"),
    EMAIL_MAX_LENGTH_EXCEED(2114, "Max 100 characters are allowed in email"),
    EMAIL_IS_INVALID(2115, "Enter a valid email address to proceed."),
    PASSWORD_MAX_LENGTH_EXCEED(2116, "Max ${maxpasswordcount} characters are allowed in Password"),
    PASSWORD_MIN_LENGTH_EXCEED(2117, "Min ${minpasswordcount} characters are allowed in Password"),
    CAPTCHA_IS_INVALID(2118, "Captcha is invalid"), CAPTCHA_IS_MISSING(2119, "Captcha is missing"),
    MOBILE_IS_MISSING(2120, "Mobile is missing"),
    MOBILE_MAX_LENGTH_EXCEED(2121, "Max 20 characters are allowed in mobile"), MOBILE_IS_INVALID(2122, "Mobile is invalid"),
    THIS_ACCOUNT_HAS_NO_PASSWORD(2123, "This account doesn't have a password. You can Login With your social account or generate password using forgot password screen."),
    MAX_PASSWORD_STORE_COUNT_PER_USER_IS_INVALID(2124, "Only numeric value allowed for MAX_PASSWORD_STORE_COUNT_PER_USER"),
    ACCOUNT_IS_LOCKED(2125, "Your account is locked. Please try again later"),
    LOCK_USER_ACCOUNT_IS_INVALID(2126, "Only boolean value allowed for LOCK_USER_ACCOUNT"),
    FAILED_LOGIN_ATTEMPT_COUNT_IS_INVALID(2127, "Only numeric value allowed for FAILED_LOGIN_ATTEMPT_COUNT"),
    UNLOCK_ACCOUNT_TIME_IN_HOURS_IS_INVALID(2128, "Only numeric value allowed for UNLOCK_ACCOUNT_TIME_IN_HOURS"),
    PASSWORD_GENERATION_SYNTAX_CHECKING_ENABLED_IS_INVALID(2129, "Only boolean value allowed for PASSWORD_GENERATION_SYNTAX_CHECKING_ENABLED"),
    SECRET_KEY_FOR_GENERATE_JWT_TOKEN_IS_INVALID(2130, "Only string value allowed for SECRET_KEY_FOR_GENERATE_JWT_TOKEN"),
    GENERATE_REFRESH_TOKEN_TIME_IN_MINUTES_IS_INVALID(2131, "Only numeric value allowed for GENERATE_REFRESH_TOKEN_TIME_IN_MINUTES"),
    REGISTRATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES_IS_INVALID(2132, "Only numeric value allowed for REGISTRATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES"),
    RESET_PASSWORD_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES_IS_INVALID(2133, "Only numeric value allowed for RESET_PASSWORD_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES"),
    CAPTCHA_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES_IS_INVALID(2134, "Only numeric value allowed for CAPTCHA_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES"),
    ACTIVATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES_IS_INVALID(2135, "Only numeric value allowed for ACTIVATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES"),
    REFRESH_JWT_TOKEN_EXPIRY_TIME_IN_DAY_IS_INVALID(2136, "Only numeric value allowed for REFRESH_JWT_TOKEN_EXPIRY_TIME_IN_DAY"),
    ACCESS_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES_IS_INVALID(2137, "Only numeric value allowed for ACCESS_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES"),
    TWO_FACTOR_TOKEN_EXPIRY_TIME_IN_MINUTES_IS_INVALID(2138, "Only numeric value allowed for TWO_FACTOR_TOKEN_EXPIRY_TIME_IN_MINUTES"),
    FIRST_LOGIN_TOKEN_EXPIRY_TIME_IN_MINUTES_IS_INVALID(2139, "Only numeric value allowed for FIRST_LOGIN_TOKEN_EXPIRY_TIME_IN_MINUTES"),
    EMAIL_ID_OR_MOBILE_NOT_REGISTERED(2140, "The provided email ID is not registered with us."),
    RESENT_VERIFICATION_OTP_SUCCESSFULLY(2141, "Verification OTP is resent successfully on your registered   mobile."),
    MAX_LIMIT_REACHED_FOR_RESEND_VERIFICATION_CODE(2142, "You have reached a maximum limit of sending a verification code."),
    MAX_LIMIT_REACHED_FOR_RESEND_RESET_PASSWORD(2143, "You have reached a maximum limit of sending a reset password code."),





    INVALID_OTP(2144, "Invalid OTP."),
    OTP_EXPIRED(2145, "Otp is expired or have already been used"),
    //Address
    PINCODE_IS_MISSING(2146, "pincode is missing"),
    PINCODE_IS_INVALID(2147, "pincode is invalid."),
    PINCODE_MAX_LENGTH_EXCEED(2248,"Max 6 characters are allowed in pincode"),
    PINCODE_MIN_LENGTH_EXCEED(2249,"Min 4 characters are allowed in pincode."),
    ADDRESS_MAX_LENGTH_EXCEED(2150, "Max 500 characters are allowed in address."),
    ADDRESS_IS_MISSING(2151, "address is missing"),
    AREA_MAX_LENGTH_EXCEED(2152, "Max 500 characters are allowed in area."),

    CITY_IS_MISSING(2153, "city is missing"),
    STATE_IS_MISSING(2154, "state is missing"),
    //sms
    SMS_ACCOUNT_ALREADY_EXIST(2155, "sms account already exist"),
    UNABLE_TO_DELETE_SMS_ACCOUNT(2156, "unable to delete sms account"),
    SMS_CONTENT_ALREADY_EXIST(2157, "sms content already exist"), SMS_ACCOUNT_IS_INVALID(2158, "sms account is invalid"),
    SMS_CONTENT_NOT_FOUND(2159, "sms content not found"), SMS_ACCOUNT_IS_MISSING(2160, "sms account is missing"),
    SMS_ACCOUNT_MOBILE_MAX_LENGTH_EXCEED(2161, "sms account mobile max length exceed"),
    SMS_ACCOUNT_MOBILE_IS_MISSING(2162, "sms account mobile is missing"),
    SMS_ACCOUNT_PASSWORD_IS_MISSING(2163, "SMS account auth Id is missing"),
    SMS_ACCOUNT_PASSWORD_MAX_LENGTH_EXCEED(2164, "Max 200 characters are allowed in SMS account auth Id"),
    SMS_ACCOUNT_PE_ID_MAX_LENGTH_EXCEED(2165, "Max 20 characters are allowed in SMS account pe Id"),
    SMS_ACCOUNT_SENDER_ID_IS_MISSING(2166, "SMS account sender Id is missing"),
    SMS_ACCOUNT_SENDER_ID_MAX_LENGTH_EXCEED(2167, "Max 6 characters are allowed in SMS account sender Id"),
    SMS_CONTENT_IS_MISSING(2168, "SMS content is missing"),
    SMS_CONTENT_NAME_IS_MISSING(2169, "sms content name is missing"),
    SMS_CONTENT_NAME_MAX_LENGTH_EXCEED(2170, "Max 100 characters are allowed in SMS Content Name"),
    SMS_TEMPLATE_ID_MAX_LENGTH_EXCEED(2171, "Max 20 characters are allowed in SMS template Id"),

    URL_IS_MISSING(2272, "URL is missing."),

    DATA_IS_MISSING(2173, "Data Is Missing"),
    CLIENT_ALREADY_EXIST(2174, "Client is Already Exist"), CLIENT_IS_INVALID(2175, "Client is invalid"),
    CLIENT_NAME_IS_MISSING(2176, "Client Name is missing"),
    CLIENT_NAME_MAX_LENGTH_EXCEED(2177, "Max 500 characters are allowed in Client Name"),
    CLIENT_NAME_IS_INVALID(2178, "Client Name is invalid"),
    CLIENT_NAME_ALREADY_EXIST(2179, "Client Name is Already Exist"),
    DELETE_DEPENDENCY(2180, "Delete other dependency."), CLIENT_IS_MISSING(2181, "Client is missing"),

    // LOCATION
    LOCATION_NAME_IS_MISSING(2182, "Location Name is missing"),
    LOCATION_NAME_MAX_LENGTH_EXCEED(2183, "Max 500 characters are allowed in Client Location Name"),
    LOCATION_NAME_IS_INVALID(2184, "Location Name is invalid"),
    LOCATION_NAME_ALREADY_EXIST(2185, "Location Name is Already Exist"), LATITUDE_IS_MISSING(2186, "Latitude is missing"),
    LATITUDE_MAX_LENGTH_EXCEED(2187, "Max 20 characters are allowed in Latitude"),
    LATITUDE_IS_INVALID(2188, "Latitude is invalid"), LONGITUDE_IS_MISSING(2189, "Longitude is missing"),
    LONGITUDE_MAX_LENGTH_EXCEED(2190, "Max 20 characters are allowed in Longitude"),
    LONGITUDE_IS_INVALID(2191, "Longitude is invalid"), ALTITUDE_IS_MISSING(2192, "Altitude is missing"),
    ALTITUDE_MAX_LENGTH_EXCEED(2193, "Max 20 characters are allowed in Altitude"),
    ALTITUDE_IS_INVALID(2194, "Altitude is invalid"), LOCATION_ALREADY_EXIST(2195, "Location is Already Exist"),

    INVALID_CLIENT_ID(2196, "Client Id is invalid."),

    UPDATE_INACTIVE_RECORD(2197, "You are not allowed to update an inactive record"),
    GATEWAY_IS_MISSING(2198, "Gateway is missing."), GATEWAY_IS_INVALID(2199,"Gateway is invalid." ),
    DEVICE_ID_MAX_LENGTH_EXCEED(2200,"Max 40 characters are allowed in device id."),
    MODEL_NUMBER_MAX_LENGTH_EXCEED(2201,"Max 100 characters are allowed in model number."),
    GATEWAY_ID_MAX_LENGTH_EXCEED(2202,"Max 40 characters are allowed in device id."),
    IP_ADDRESS_MAX_LENGTH_EXCEED(2203,"Max 50 characters are allowed in ip address."),
    MAC_ADDRESS_MAX_LENGTH_EXCEED(2204,"Max 50 characters are allowed in mac address."),

    LOCATION_ID_MAX_LENGTH_EXCEED(2205,"Max 40 characters are allowed in location id."),
    GATEWAY_NAME_IS_MISSING(2206, "Gateway Name is missing"),
    GATEWAY_NAME_MAX_LENGTH_EXCEED(2207, "Max 500 characters are allowed in Gateway Name"),
    GATEWAY_NAME_IS_INVALID(2208, "Gateway Name is invalid"),
    DEVICE_NAME_IS_MISSING(2209, "Device Name is missing"),
    DEVICE_NAME_MAX_LENGTH_EXCEED(2210, "Max 500 characters are allowed in Device Name"),
    DEVICE_NAME_IS_INVALID(2211, "Device Name is invalid"),
    SITE_ID_MAX_LENGTH_EXCEED(2212, "Max 40 characters are allowed in site id."),
    SITE_UUID_MAX_LENGTH_EXCEED(2213, "Max 40 characters are allowed in site uuid."),
    ROLE_NAME_ALREADY_EXIST(2214, "Role Name is Already Exist"),
    DELETE_SUCCESSFULLY_ROLE(2215, "User role deleted successfully."),
    CANT_DELETE_DEVICE(2216, "You can't delete a Device as it's bound to something else."),
    CANT_DELETE_PARAMETER(2217, "You can't delete a Device Parameter as it's bound to something else.");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * This methods is used to fetch Enum base on given id.
     *
     * @param code enum key
     * @return ResponseCode enum
     */
    public static ResponseCode fromId(int code) {
        for (ResponseCode responseCode : values()) {
            if (responseCode.code == code) {
                return responseCode;
            }
        }
        return null;
    }
}