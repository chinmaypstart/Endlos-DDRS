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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

public class CookieUtility {

    private CookieUtility() {
    }

    /**
     * This method is used get the cookie values.
     *
     * @param cookieName
     * @param cookies
     * @return
     */
    public static String getCookie(String cookieName, Cookie[] cookies) {
        if (StringUtils.isBlank(cookieName)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * This method is used to set cookies at vendor side.
     *
     * @param httpServletResponse
     * @param cookieName          - name of the cookie
     * @param value               - value of cookie
     * @param maxAge              - till number of seconds cookie will remain valid,
     *                            0 means expire immediately and other value in
     *                            seconds.
     * @param path
     * @return
     */
    public static void setCookie(
            HttpServletResponse httpServletResponse,
            String cookieName,
            String value,
            Integer maxAge,
            String path) {
        if (httpServletResponse == null
                || StringUtils.isBlank(cookieName)
                || StringUtils.isBlank(value)) {
            return;
        }
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(maxAge != null ? maxAge.intValue() : (60 * 60 * 24 * 365 * 10));
        cookie.setPath(StringUtils.isNotBlank(path) ? path : "/");
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);
        //		addCookie(httpServletResponse, cookie, "None");
    }

    public static void addCookie(HttpServletResponse response, Cookie cookie, String sameSite) {

        StringBuilder c = new StringBuilder(64 + cookie.getValue().length());

        c.append(cookie.getName());
        c.append('=');
        c.append(cookie.getValue());

        append2cookie(c, "domain", cookie.getDomain());
        append2cookie(c, "path", cookie.getPath());
        append2cookie(c, "SameSite", sameSite);
        c.append("; HttpOnly");
        c.append("; secure");
        if (cookie.getMaxAge() >= 0) {
            append2cookie(c, "Max-Age", String.valueOf(cookie.getMaxAge()));
        }
        response.addHeader("Set-Cookie", c.toString());
    }

    private static void append2cookie(StringBuilder cookie, String key, String value) {
        if (key == null || value == null || key.trim().equals("") || value.trim().equals("")) {
            return;
        }

        cookie.append("; ");
        cookie.append(key);
        cookie.append('=');
        cookie.append(value);
    }
}