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

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.user.view.CaptchaResponseView;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class CaptchaUtility {

    private CaptchaUtility() {
    }

    public static CaptchaResponseView verify(String gRecaptchaResponse, String captchaSiteVerifyUrl, String secretKey)
            throws EndlosiotAPIException, IOException {
        CaptchaResponseView captchaResponseView = new CaptchaResponseView();
        captchaResponseView.setCaptchaResponseToken(gRecaptchaResponse);
        if (StringUtils.isBlank(gRecaptchaResponse)) {
            throw new EndlosiotAPIException(ResponseCode.CAPTCHA_IS_INVALID.getCode(),
                    ResponseCode.CAPTCHA_IS_INVALID.getMessage());
        }
        try {
            URL obj = new URL(captchaSiteVerifyUrl);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String postParams = "secret=" + secretKey + "&response=" + gRecaptchaResponse;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            captchaResponseView.setResponseCode(responseCode);

            if (responseCode != HttpStatus.SC_OK) {
                throw new EndlosiotAPIException(ResponseCode.CAPTCHA_IS_INVALID.getCode(),
                        ResponseCode.CAPTCHA_IS_INVALID.getMessage());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String sucess = JsonUtil.getValueOfSpecificKeyFromJsonData(response.toString(), "success");
            String error = JsonUtil.getValueOfSpecificKeyFromJsonData(response.toString(), "error-codes");
            if (!StringUtils.isBlank(error)) {
                List<String> errorCodes = JsonUtil.toObjectList(error);
                if (!errorCodes.isEmpty()) {
                    captchaResponseView.setErrorCodes(errorCodes);
                }
            }
            if (sucess.equalsIgnoreCase("false")) {
                LoggerService.error(response.toString());
                throw new EndlosiotAPIException(ResponseCode.CAPTCHA_IS_INVALID.getCode(),
                        ResponseCode.CAPTCHA_IS_INVALID.getMessage());
            } else {
                captchaResponseView.setStatus(true);
            }
        } catch (EndlosiotAPIException e) {
            LoggerService.exception(e);
            throw new EndlosiotAPIException(ResponseCode.CAPTCHA_IS_INVALID.getCode(),
                    ResponseCode.CAPTCHA_IS_INVALID.getMessage());
        }
        return captchaResponseView;
    }
}