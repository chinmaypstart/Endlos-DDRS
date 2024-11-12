/* Copyright -2019 @intentlabs
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
package com.endlosiot.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All common operation will be added here.
 *
 * @author Nirav.Shah
 * @since 27/12/2019
 */
public class Utility {

    public static final SecureRandom secureRandom = new SecureRandom();

    private Utility() {
    }

    /**
     * This method is used to generate an alpha numeric random number based on given
     * length.
     *
     * @param otpLength
     * @return String number
     */
    public static String generateToken(int tokenLength) {
        return RandomStringUtils.randomAlphanumeric(tokenLength);
    }

    /**
     * This method is used to generate a unique id and replace all the hypen in
     * generated UUID.
     *
     * @return
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * This method is used to generate a UUID.
     *
     * @return
     */
    public static String generateUuidWithHash() {
        return UUID.randomUUID().toString();
    }

    /**
     * This method is used to generating random Token.
     *
     * @param otpLength
     * @return String number
     */
    public static String generateOTP(int tokenLength) {
        return RandomStringUtils.randomNumeric(tokenLength);
    }

    public static boolean isValidPattern(String value, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static BigDecimal formateBigDecimal(BigDecimal value, Integer limit) {
        if (value == null) {
            return null;
        }
        return value.setScale(limit, RoundingMode.HALF_UP);
    }

    public static String indianFormat(String value) {
        String decimal = "";
        if (value.contains(".")) {
            decimal = value.substring(value.indexOf("."), value.length());
            value = value.substring(0, value.indexOf("."));
        }

        value = value.replace(",", "");
        char lastDigit = value.charAt(value.length() - 1);
        String result = "";
        int len = value.length() - 1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--) {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0)) {
                result = "," + result;
            }
        }
        return (result + lastDigit + decimal);
    }

    public static String getSchoolDomain(String domain, String appUrl) {
        return "http:" + File.separator + File.separator + domain + "." + appUrl;
    }

    public static String getSuperAdminUrl(String adminurl) {
        return "http:" + File.separator + File.separator + adminurl;
    }

    public static String getSchoolAdminUrl(String domain, String appUrl) {
        return "http:" + File.separator + File.separator + domain + "." + appUrl + File.separator + "adminlogin";
    }

    public static String getRegistrationDomain(String registrationUrl) {
        return "http:" + File.separator + File.separator + registrationUrl;
    }

    public static String getAlumnlyVerificationUrl(String domain, String verifyToken, String appUrl) {
        return getSchoolDomain(domain, appUrl) + File.separator + "registration-verification" + File.separator
                + verifyToken;
    }

    public static String getSchoolVerificationUrl(String verifyToken, String registrationUrl) {
        return getRegistrationDomain(registrationUrl) + File.separator + "registration-verification" + File.separator
                + verifyToken;
    }

    public static String getSchoolLogoUrl(String fileId, String apiurl) {
        return "http:" + File.separator + File.separator + apiurl + File.separator + "alumnly-api" + File.separator
                + "public" + File.separator + "file" + File.separator + "download-image?fileId=" + fileId;
    }

    public static String getImageUrl(String fileId, String apiurl) {
        return "http:" + File.separator + File.separator + apiurl + File.separator + "alumnly-api" + File.separator
                + "public" + File.separator + "file" + File.separator + "download-image?fileId=" + fileId;
    }

    public static String getFundRaisingViewLink(Long id, String domain, String appUrl) {
        return getSchoolDomain(domain, appUrl) + File.separator + "alumni" + File.separator + "fund-raising"
                + File.separator + "view" + File.separator + id;
    }

    public static String getAlumniViewLink(Long id, String domain, String appUrl) {
        return getSchoolDomain(domain, appUrl) + File.separator + "alumni" + File.separator + "directory"
                + File.separator + "profile" + File.separator + "view" + File.separator + id;
    }

    public static String getEventViewLink(Long id, String domain, String appUrl) {
        return getSchoolDomain(domain, appUrl) + File.separator + "alumni" + File.separator + "event" + File.separator
                + "view" + File.separator + id;
    }

    public static String getAlumniProfileUrl(String fileId, String apiurl) {
        return "http:" + File.separator + File.separator + apiurl + File.separator + "alumnly-api" + File.separator
                + "public" + File.separator + "file" + File.separator + "download-image?fileId=" + fileId;
    }

    public static void main(String[] args) {
        // Connecting to Redis server on localhost
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server sucessfully");
        // store data in redis list
        // Get the stored data and print it
        Set<String> list = jedis.keys("*");

        for (String key : list) {
            System.out.println("List of stored keys:: " + key);
        }
    }

    public static String removeExtraSpaceBetweenWords(String str) {
        return str.replaceAll(" +", " ");
    }

}