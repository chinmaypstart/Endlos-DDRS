/*******************************************************************************
 * Copyright -2018 @intentlabs
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
package com.endlosiot.common.config.filter;

import com.endlosiot.common.enums.JWTExceptionEnum;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.model.JwtTokenModel;
import com.endlosiot.common.modelenums.CommonStatusEnum;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.setting.model.SystemSettingModel;
import com.endlosiot.common.threadlocal.Auditor;
import com.endlosiot.common.threadlocal.Uuid;
import com.endlosiot.common.user.model.TokenBlackListModel;
import com.endlosiot.common.user.model.UserModel;
import com.endlosiot.common.user.model.UserPasswordModel;
import com.endlosiot.common.user.service.TokenBlackListService;
import com.endlosiot.common.user.service.UserPasswordService;
import com.endlosiot.common.user.service.UserService;
import com.endlosiot.common.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * This is private request filter. Private request filter authentication users
 * using it's session and browser's informations. It also prepared role module
 * rights map which will be used by authorization annotation to check
 * authorization of user.
 *
 * @author nirav
 * @since 30/10/2018
 */
@Component
public class PrivateRequestFilter implements Filter {

    private ApplicationContext applicationContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.setApplicationContext(
                WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        UserPasswordService userPasswordService = (UserPasswordService) applicationContext
                .getBean("userPasswordService");
        UserService userService = (UserService) applicationContext.getBean("userService");
        TokenBlackListService tokenBlackListService = (TokenBlackListService) applicationContext
                .getBean("tokenBlackListService");

        if (httpServletRequest.getRequestURI().endsWith("get-access-token")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        UserModel userModel = getDeviceCookieAndValidateJWTToken(httpServletRequest, httpServletResponse, userService,
                tokenBlackListService);
//        if (!httpServletRequest.getRequestURI().endsWith("reset-password")) {
//            validateLockAccount(userModel, userService, httpServletResponse);
//        }

        try {
            if (!validateUserDetailsAndRequest(userModel, httpServletRequest, httpServletResponse)) {
                return;
            }
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return;
        }
        if (!isPasswordExpired(userModel, httpServletRequest, httpServletResponse, userPasswordService)) {
            return;
        }
        Auditor.setAuditor(userModel);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        Auditor.removeAuditor();
        Uuid.removeUuid();
    }

//    /***
//     * this method will used to validate request as per JWT token model.
//     *
//     * @param userModel
//     * @param userService
//     * @throws IOException
//     */
//    private void validateLockAccount(UserModel userModel, UserService userService,
//                                     HttpServletResponse httpServletResponse) throws IOException {
//        if (CommonStatusEnum.YES.getId().equals(SystemSettingModel.getLockUserAccount())
//                && userModel.getFailedAttempt() != null
//                && userModel.getFailedAttempt() >= SystemSettingModel.getFailedLoginAttemptCount()) {
//            if (Instant.now().isAfter(DateUtility.getEpochAfterSpecificPeriod(userModel.getLastFailedAttemptDate(),
//                    ChronoUnit.HOURS, SystemSettingModel.getUnlockAccountTimeInHours()))) {
//                userModel.setFailedAttempt(null);
//                userModel.setLastFailedAttemptDate(null);
//                userService.update(userModel);
//            } else {
//                CommonResponse commonResponse = CommonResponse.create(ResponseCode.ACCOUNT_IS_LOCKED.getCode(),
//                        ResponseCode.ACCOUNT_IS_LOCKED.getMessage().replace("${hours}",
//                                SystemSettingModel.getUnlockAccountTimeInHours().toString()));
//                WebUtil.sendResponse(httpServletResponse, commonResponse);
//            }
//        }
//    }

    /**
     * this method will used to validate request as per JWT token model.
     *
     * @param userModel
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws EndlosiotAPIException
     * @throws IOException
     */
    private boolean validateUserDetailsAndRequest(UserModel userModel, HttpServletRequest httpServletRequest,
                                                  HttpServletResponse httpServletResponse) throws EndlosiotAPIException, IOException {
        JwtTokenModel jwtTokenModel = null;
        Claims claims = JwtUtil.extractAllClaims(userModel.getAccessJWTToken());
        if (claims != null && claims.get(Constant.REQUEST_PAYLOAD) != null) {
            jwtTokenModel = JsonUtil.toObject(claims.get(Constant.REQUEST_PAYLOAD).toString(), JwtTokenModel.class);
        }
        if (httpServletRequest.getRequestURI().endsWith("reset-password")) {
            if (claims == null || (claims != null && claims.get(Constant.REQUEST_PAYLOAD) == null)) {
                return false;
            }
        }
        if (jwtTokenModel == null) {
            return false;
        } else {
            if (StringUtils.isEmpty(jwtTokenModel.getUniqueToken())) {
                CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_JSON_TOKEN.getCode(),
                        ResponseCode.INVALID_JSON_TOKEN.getMessage());
                WebUtil.sendResponse(httpServletResponse, commonResponse);
                return false;
            } else {
                if (!jwtTokenModel.getUniqueToken().equals(userModel.getUniqueToken())) {
                    CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_JSON_TOKEN.getCode(),
                            ResponseCode.INVALID_JSON_TOKEN.getMessage());
                    WebUtil.sendResponse(httpServletResponse, commonResponse);
                    return false;
                }
            }
            if (jwtTokenModel.isFirstLoginToken()
                    && !httpServletRequest.getRequestURI().endsWith("first-time-password-change")) {
                CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_REQUEST.getCode(),
                        ResponseCode.INVALID_REQUEST.getMessage());
                WebUtil.sendResponse(httpServletResponse, commonResponse);
                return false;
            }
            if (jwtTokenModel.isResetPasswordToken() && !httpServletRequest.getRequestURI().endsWith("reset-password")
                    && !httpServletRequest.getRequestURI().endsWith("logout")) {
                CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_REQUEST.getCode(),
                        ResponseCode.INVALID_REQUEST.getMessage());
                WebUtil.sendResponse(httpServletResponse, commonResponse);
                return false;
            }
            if (jwtTokenModel.isActivationToken() && (!httpServletRequest.getRequestURI().endsWith("activate")
                    && !httpServletRequest.getRequestURI().endsWith("resend-verification-link"))) {
                CommonResponse commonResponse = CommonResponse.create(ResponseCode.EMAIL_VERIFICATION.getCode(),
                        ResponseCode.EMAIL_VERIFICATION.getMessage());
                WebUtil.sendResponse(httpServletResponse, commonResponse);
                return false;
            }
            if (jwtTokenModel.isRegistrationToken() && (!httpServletRequest.getRequestURI().endsWith("login")
                    && !httpServletRequest.getRequestURI().endsWith("activate")
                    && !httpServletRequest.getRequestURI().endsWith("resend-verification-link"))) {
                CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_REQUEST.getCode(),
                        ResponseCode.INVALID_REQUEST.getMessage());
                WebUtil.sendResponse(httpServletResponse, commonResponse);
                return false;
            }
            userModel.setJwtTokenModel(jwtTokenModel);
        }
        return true;
    }

    /**
     * This method is used to validate password expiration.
     *
     * @param userModel
     * @param httpServletRequest
     * @param httpServletResponse
     * @param userPasswordService
     * @throws IOException
     */
    private boolean isPasswordExpired(UserModel userModel, HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse, UserPasswordService userPasswordService) throws IOException {
        if (CommonStatusEnum.YES.equals(SystemSettingModel.getPasswordExpirationMaxAgeNeeded())
                && !httpServletRequest.getRequestURI().endsWith("change-password")
                && !httpServletRequest.getRequestURI().endsWith("logout")) {
            UserPasswordModel userPasswordModel = userPasswordService.getCurrent(userModel.getId());
            if (userPasswordModel != null) {
                if (Instant.now().isAfter(DateUtility.getEpochAfterSpecificPeriod(userPasswordModel.getCreate(),
                        ChronoUnit.DAYS, SystemSettingModel.getPasswordExpirationMaxAgeDays()))) {
                    CommonResponse commonResponse = CommonResponse.create(ResponseCode.PASSWORD_EXPIRED.getCode(),
                            ResponseCode.PASSWORD_EXPIRED.getMessage());
                    WebUtil.sendResponse(httpServletResponse, commonResponse);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method is used get jwt token validate user and device cookie related
     * details.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param userService
     * @throws IOException
     */
    private UserModel getDeviceCookieAndValidateJWTToken(HttpServletRequest httpServletRequest,
                                                         HttpServletResponse httpServletResponse, UserService userService,
                                                         TokenBlackListService tokenBlackListService) throws IOException {
        String jwtAccessTokenHeader = httpServletRequest.getHeader(Constant.AUTHORIZATION);
        String refreshAccessTokenHeader = httpServletRequest.getHeader(Constant.REFRESH_TOKEN);

        String accessToken = validateAccessToken(jwtAccessTokenHeader, httpServletResponse);
        String refreshToken = validateRefreshToken(refreshAccessTokenHeader, httpServletResponse);

        JWTExceptionEnum jwtExceptionEnum = JwtUtil.isValidJWTToken(accessToken);
        if (jwtExceptionEnum == null) {
            CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
            sendResponse(httpServletResponse, commonResponse);
            return null;
        }
        if (jwtExceptionEnum.equals(JWTExceptionEnum.SIGNAUTURE_EXCEPTION)) {
            CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_JSON_TOKEN.getCode(),
                    ResponseCode.INVALID_JSON_TOKEN.getMessage());
            sendTokenExceptionResponse(httpServletResponse, commonResponse);
            return null;
        }
        if (jwtExceptionEnum.equals(JWTExceptionEnum.EXPIRED_JWT_EXCEPTION)) {
            JWTExceptionEnum refreshJwtExceptionEnum = JwtUtil.isValidJWTToken(refreshToken);
            if (refreshJwtExceptionEnum == null) {
                CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_REQUEST.getCode(),
                        ResponseCode.INVALID_REQUEST.getMessage());
                sendResponse(httpServletResponse, commonResponse);
                return null;
            }
            if (refreshJwtExceptionEnum.equals(JWTExceptionEnum.SIGNAUTURE_EXCEPTION)) {
                CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_REFRESH_JSON_TOKEN.getCode(),
                        ResponseCode.INVALID_REFRESH_JSON_TOKEN.getMessage());
                sendTokenExceptionResponse(httpServletResponse, commonResponse);
                return null;
            }
            if (refreshJwtExceptionEnum.equals(JWTExceptionEnum.EXPIRED_JWT_EXCEPTION)) {
                CommonResponse commonResponse = CommonResponse.create(ResponseCode.EXPIRED_JSON_TOKEN.getCode(),
                        ResponseCode.EXPIRED_JSON_TOKEN.getMessage());
                sendTokenExceptionResponse(httpServletResponse, commonResponse);
                return null;
            }
            UserModel userModel = getUserFromToken(refreshToken, httpServletResponse, userService);
            if (userModel == null) {
                sendUnAuthorizeResponse(httpServletResponse);
                return null;
            }
            JwtTokenModel jwtTokenModel = JwtTokenModel.createLoginToken();
            try {
                userModel.setAccessJWTToken(JwtUtil.generateAccessToken(userModel.getEmail(),
                        JsonUtil.toJson(jwtTokenModel), jwtTokenModel));
            } catch (Exception e) {
                LoggerService.exception(e);
            }
            TokenBlackListModel tokenBlackListModel = new TokenBlackListModel();
            tokenBlackListModel.setUserModel(userModel);
            tokenBlackListModel.setJwtToken(accessToken);
            tokenBlackListService.create(tokenBlackListModel);
            return userModel;
        }
        UserModel userModel = getUserFromToken(accessToken, httpServletResponse, userService);
        if (userModel == null) {
            sendUnAuthorizeResponse(httpServletResponse);
            return null;
        }
        TokenBlackListModel tokenBlackListModel = tokenBlackListService.findByUserAndToken(userModel.getId(),
                accessToken);
        if (tokenBlackListModel != null) {
            sendUnAuthorizeResponse(httpServletResponse);
            return null;
        }
        userModel.setAccessJWTToken(accessToken);
        return userModel;
    }

    /**
     * This method is used to validate accessToken.
     *
     * @param jwtAccesssTokenHeader
     * @param httpServletResponse
     * @throws IOException
     */
    private String validateAccessToken(String jwtAccesssTokenHeader, HttpServletResponse httpServletResponse)
            throws IOException {
        if (StringUtils.isBlank(jwtAccesssTokenHeader)) {
            LoggerService.error(jwtAccesssTokenHeader + "Access");
            sendUnAuthorizeResponse(httpServletResponse);
            return null;
        }
        String[] accessToken = jwtAccesssTokenHeader.split(" ");
        if (accessToken == null || accessToken.length != 2) {
            CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_REFRESH_JSON_TOKEN.getCode(),
                    ResponseCode.INVALID_REFRESH_JSON_TOKEN.getMessage());
            sendTokenExceptionResponse(httpServletResponse, commonResponse);
            return null;
        }
        return accessToken[1];
    }

    /**
     * This method is used to validate refreshToken.
     *
     * @param refreshAccesssTokenHeader
     * @param httpServletResponse
     * @throws IOException
     */
    private String validateRefreshToken(String refreshAccesssTokenHeader, HttpServletResponse httpServletResponse)
            throws IOException {
        if (StringUtils.isBlank(refreshAccesssTokenHeader)) {
            LoggerService.error(refreshAccesssTokenHeader + "refresh");
            sendUnAuthorizeResponse(httpServletResponse);
            return null;
        }
        String[] refreshToken = refreshAccesssTokenHeader.split(" ");
        if (refreshToken == null || refreshToken.length != 2) {
            CommonResponse commonResponse = CommonResponse.create(ResponseCode.INVALID_REFRESH_JSON_TOKEN.getCode(),
                    ResponseCode.INVALID_REFRESH_JSON_TOKEN.getMessage());
            sendTokenExceptionResponse(httpServletResponse, commonResponse);
            return null;
        }
        return refreshToken[1];
    }

    /**
     * This method is used to get user from token.
     *
     * @param jwtAccessToken
     * @param httpServletResponse
     * @param userService
     * @throws IOException
     */
    private UserModel getUserFromToken(String jwtAccessToken, HttpServletResponse httpServletResponse,
                                       UserService userService) throws IOException {
        Claims claims = null;
        UserModel userModel = null;
        String userEmail = null;
        try {
            claims = JwtUtil.extractAllClaims(jwtAccessToken);
        } catch (EndlosiotAPIException e) {
            LoggerService.exception(e);
        }
        userEmail = JwtUtil.extractData(jwtAccessToken, claims);
        if (userEmail != null) {
            userModel = userService.findByEmail(userEmail);
        }
        return userModel;
    }

    /**
     * Send an error response in json from the filter.
     *
     * @param httpServletResponse
     * @param commonResponse
     * @throws IOException
     */
    private void sendResponse(HttpServletResponse httpServletResponse, CommonResponse commonResponse)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", commonResponse.getMessage());
        errorDetails.put("code", commonResponse.getCode());

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(httpServletResponse.getWriter(), errorDetails);
    }

    /**
     * Send unAutorized response in json from the filter.
     *
     * @param httpServletResponse
     * @throws IOException
     */
    private void sendUnAuthorizeResponse(HttpServletResponse httpServletResponse) throws IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ResponseCode.AUTHENTICATION_REQUIRED.getMessage());
        errorDetails.put("code", ResponseCode.AUTHENTICATION_REQUIRED.getCode());

        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(httpServletResponse.getWriter(), errorDetails);
    }

    /**
     * Send token exception response in json from the filter.
     *
     * @param httpServletResponse
     * @param commonResponse
     * @throws IOException
     */
    private void sendTokenExceptionResponse(HttpServletResponse httpServletResponse, CommonResponse commonResponse)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", commonResponse.getMessage());
        errorDetails.put("code", commonResponse.getCode());

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        objectMapper.writeValue(httpServletResponse.getWriter(), errorDetails);
    }

    @Override
    public void destroy() {
        // do nothing
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}