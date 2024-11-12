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
package com.endlosiot.common.util;

import com.endlosiot.common.exception.EndlosiotAPIException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class contains all hashing methods.
 *
 * @author Nirav.Shah
 * @since 23/06/2018
 */
public class HashUtil {

    private HashUtil() {
    }

    private static ThreadLocal<MessageDigest> md5MessageDigest = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private static ThreadLocal<MessageDigest> sha512MessageDigest = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA-512");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private static ThreadLocal<MessageDigest> sha256MessageDigest = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    /**
     * Generate md5 hash for given string & return hash string.
     *
     * @param saltedPassword
     * @return
     */
    public static String getmd5(String saltedPassword) {
        md5MessageDigest.get().update(saltedPassword.getBytes());
        return toHex(md5MessageDigest.get().digest());
    }

    /**
     * Generate sha512 hash for given string & return hash string.
     *
     * @param saltedPassword
     * @return
     */
    public static String getSha512(String saltedPassword) {
        sha512MessageDigest.get().update(saltedPassword.getBytes());
        return toHex(sha512MessageDigest.get().digest());
    }

    /**
     * Generate sha256 hash for given string & return hash string.
     *
     * @param saltedString
     * @return
     */
    public static String getSha256(String saltedString) {
        sha256MessageDigest.get().update(saltedString.getBytes());
        return toHex(sha256MessageDigest.get().digest());
    }

    /**
     * This method converted byte array into hexa decimal string.
     *
     * @param array
     * @return
     */
    public static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            String format = "%0" + paddingLength + "d";
            return String.format(format, 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * This method converts given string value into byte array
     *
     * @param hex
     * @return byte[]
     */
    public static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * This will generated dynamic alphanumeric salt.
     *
     * @return
     */
    public static String getDynamicSalt() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    /**
     * This method is use to compare vendor & server password
     *
     * @param dbPassword     storePassword
     * @param vendorPassword vendorPassword
     * @return boolean
     */
    public static boolean passwordVerification(String dbPassword, String vendorPassword, String dynamicSalt) {
        String securePassword = getSha512(dbPassword + dynamicSalt);
        if (vendorPassword.equals(securePassword)) {
            return true;
        }
        return false;
    }

    public static String hash(String hash) throws EndlosiotAPIException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        return bCryptPasswordEncoder.encode(hash);
    }

    public static boolean matchHash(String rawHash, String encodedHash) throws EndlosiotAPIException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        return bCryptPasswordEncoder.matches(rawHash, encodedHash);
    }

    /**
     * This method is used to generate auth token.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    public static String generateAuthToken() throws EndlosiotAPIException {
        return hash(Utility.generateToken(6) + DateUtility.getCurrentEpoch() + Utility.generateToken(8));
    }

    /**
     * This method is used to generate device token.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    public static String generateDeviceToken() throws EndlosiotAPIException {
        return hash(Utility.generateUuid() + DateUtility.getCurrentEpoch());
    }

    /**
     * This method is used to generate APi keys shared with multiple vendor.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    public static String generateApiKey(String name) throws EndlosiotAPIException {
        return hash(Utility.generateToken(6) + DateUtility.getCurrentEpoch() + name + Utility.generateToken(8));
    }
}