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

import com.endlosiot.common.enums.JWTExceptionEnum;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.model.JwtTokenModel;
import com.endlosiot.common.setting.model.SystemSettingModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * this utility will be used to generate JWT(JSON Web Token) and get all
 * operation related JWT token.
 *
 * @author Jaydip
 * @since 13/03/2021
 */
public class JwtUtil {

    /**
     * this method will use to extract data from JWT token.
     *
     * @param token
     * @param claims
     * @return
     * @throws EndlosiotAPIException
     */
    public static String extractData(String token, Claims claims) {
        try {
            return extractClaim(token, Claims::getSubject, claims);
        } catch (EndlosiotAPIException e) {
            LoggerService.exception(e);
        }
        return null;
    }

    /**
     * this method will use to get expiration from token.
     *
     * @param token
     * @param claims
     * @return
     * @throws EndlosiotAPIException
     */
    public static Date extractExpiration(String token, Claims claims) {
        try {
            return extractClaim(token, Claims::getExpiration, claims);
        } catch (EndlosiotAPIException e) {
            LoggerService.exception(e);
        }
        return null;
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver, Claims claims)
            throws EndlosiotAPIException {
        if (claims == null) {
            claims = extractAllClaims(token);
        }
        if (claims != null) {
            return claimsResolver.apply(claims);
        }
        return null;
    }

    /**
     * This method will check JWT token expiration and if token not expired then it
     * will return all Claims otherwise it will return null.
     *
     * @param token
     * @return
     * @throws EndlosiotAPIException
     */
    public static Claims extractAllClaims(String token) throws EndlosiotAPIException {
        try {
            return Jwts.parser().setSigningKey(SystemSettingModel.getSecretKeyForGenerateJWTToken())
                    .parseClaimsJws(token).getBody();
        } catch (SignatureException signatureException) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_JSON_TOKEN.getCode(),
                    ResponseCode.INVALID_JSON_TOKEN.getMessage());
        } catch (ExpiredJwtException expiredJwtException) {
            throw new EndlosiotAPIException(ResponseCode.EXPIRED_TOKEN.getCode(),
                    ResponseCode.EXPIRED_TOKEN.getMessage());
        }

    }

    /**
     * This method is use to check JWT token Signature and also check JWT token is
     * expired or not and return pair class with Pair<SignatureException,
     * ExpiredException>.
     *
     * @param token
     * @return
     */
    public static JWTExceptionEnum isValidJWTToken(String token) {
        try {
            Jwts.parser().setSigningKey(SystemSettingModel.getSecretKeyForGenerateJWTToken()).parseClaimsJws(token)
                    .getBody();
            return JWTExceptionEnum.NO_EXCEPTION;
        } catch (SignatureException signatureException) {
            LoggerService.exception(signatureException);
            return JWTExceptionEnum.SIGNAUTURE_EXCEPTION;
        } catch (ExpiredJwtException expiredJwtException) {
            LoggerService.exception(expiredJwtException);
            return JWTExceptionEnum.EXPIRED_JWT_EXCEPTION;
        }
    }

    /**
     * this method will generate and give you jwt token as per details.
     *
     * @param userModel
     * @param requestJsonPayload
     * @return
     */
    public static String generateAccessToken(String subject, String requestJsonPayload, JwtTokenModel jwtTokenModel) {
        Map<String, Object> claims = new HashMap<>();
        if (!StringUtils.isBlank(requestJsonPayload)) {
            claims.put(Constant.REQUEST_PAYLOAD, requestJsonPayload);
        }
        int expiryTimeInMinutes = 0;
        if (jwtTokenModel != null) {
            if (jwtTokenModel.isActivationToken()) {
                expiryTimeInMinutes = SystemSettingModel.getActivationJwtTokenExpiryTimeInMinutes();
            } else if (jwtTokenModel.isLoginToken()) {
                expiryTimeInMinutes = SystemSettingModel.getAccessJwtTokenExpiryTimeInMinutes();
            } else if (jwtTokenModel.isResetPasswordToken()) {
                expiryTimeInMinutes = SystemSettingModel.getResetPasswordJwtTokenExpiryTimeInMinutes();
            } else if (jwtTokenModel.isTwoFactorToken()) {
                expiryTimeInMinutes = SystemSettingModel.getTwoFactorTokenExpiryTimeInMinutes();
            } else if (jwtTokenModel.isFirstLoginToken()) {
                expiryTimeInMinutes = SystemSettingModel.getFirstLoginTokenExpiryTimeInMinutes();
            } else if (jwtTokenModel.isRegistrationToken()) {
                expiryTimeInMinutes = SystemSettingModel.getRegistrationJwtTokenExpiryTimeInMinutes();
            }
        } else {
            expiryTimeInMinutes = SystemSettingModel.getRegistrationJwtTokenExpiryTimeInMinutes();
        }
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addMinutes(new Date(System.currentTimeMillis()), expiryTimeInMinutes))
                .signWith(SignatureAlgorithm.HS256, SystemSettingModel.getSecretKeyForGenerateJWTToken()).compact();
    }

    /**
     * this method will use to create refresh token based on requirements.
     *
     * @param subject
     * @return
     */
    public static String generateRefreshToken(String subject, String requestJsonPayload) {
        Map<String, Object> claims = new HashMap<>();
        if (!StringUtils.isBlank(requestJsonPayload)) {
            claims.put(Constant.REQUEST_PAYLOAD, requestJsonPayload);
        }
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addDays(new Date(System.currentTimeMillis()),
                        SystemSettingModel.getRefreshJwtTokenExpiryTimeInDay()))
                .signWith(SignatureAlgorithm.HS256, SystemSettingModel.getSecretKeyForGenerateJWTToken()).compact();
    }

    /**
     * This method will check JWT token expiration and if token not expired then it
     * will return all Claims otherwise it will return null.
     *
     * @param token
     * @return
     * @throws EndlosiotAPIException
     */
    public static String expireJwtToken(String token) {
        Claims claims = null;
        try {
            claims = JwtUtil.extractAllClaims(token);
        } catch (EndlosiotAPIException e) {
            LoggerService.exception(e);
        }
        String userEmail = JwtUtil.extractData(token, claims);
        return Jwts.builder().setClaims(claims).setSubject(userEmail).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(DateUtils.addMinutes(new Date(System.currentTimeMillis()), 0))
                .signWith(SignatureAlgorithm.HS256, SystemSettingModel.getSecretKeyForGenerateJWTToken()).compact();

    }
}
