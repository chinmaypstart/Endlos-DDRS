package com.endlosiot.common.response;

import com.endlosiot.common.threadlocal.Auditor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.StringUtils;

/**
 * This is common response which will be send for every request..
 *
 * @author Nirav.Shah
 * @since 03/08/2018
 */
@JsonInclude(Include.NON_NULL)
public class CommonResponse implements Response {

    private static final long serialVersionUID = 3217452268355902474L;
    private int code;
    private String message;
    private String accessToken;
    private String refreshToken;

    private boolean hasError;

    protected CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.hasError = (code >= 2000);
        if (Auditor.getAuditor() != null && !StringUtils.isEmpty(Auditor.getAuditor().getNewAccessJWTToken())) {
            this.accessToken = Auditor.getAuditor().getNewAccessJWTToken();
        }
    }

    protected CommonResponse(int code, String message, String accessToken, String refreshToken) {
        this.code = code;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.hasError = (code >= 2000);
    }

    public static CommonResponse create(int code, String message) {
        return new CommonResponse(code, message);
    }

    public static CommonResponse create(int code, String message, String accessToken, String refreshToken) {
        return new CommonResponse(code, message, accessToken, refreshToken);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isHasError() {
        return hasError;
    }
}