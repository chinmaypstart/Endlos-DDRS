package com.endlosiot.common.util;

import com.endlosiot.common.logger.LoggerService;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * This class is used to perform all http related operations.
 *
 * @author Nirav.Shah
 * @since 26/09/2018
 */
public class HttpUtil {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-HotelPartner-IP",
            "WL-Proxy-HotelPartner-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_VENDOR_IP",
            "HTTP_VENDOR_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private HttpUtil() {
    }

    public static StringBuffer sendHttpPost(String url, String urlParams) throws IOException {
        DataOutputStream dataOutputStream = null;
        BufferedReader bufferedReader = null;
        try {
            URL obj = new URL(url);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) obj.openConnection();

            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpsURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            httpsURLConnection.setDoOutput(true);

            dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
            if (urlParams != null) {
                dataOutputStream.writeBytes(urlParams);
            }
            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                LoggerService.error("HttpUtil", url, "Status : " + responseCode);
                return null;
            }

            bufferedReader =
                    new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            return response;
        } finally {
            if (dataOutputStream != null) {
                dataOutputStream.flush();
                dataOutputStream.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    /**
     * Will return remote address
     *
     * @return
     */
    public static String getRemoteAddress() {
        return WebUtil.getCurrentRequest().getRemoteAddr();
    }

    /**
     * This method returns user's browsers name.
     *
     * @return
     */
    public static String getUserBrowser() {
        UserAgent userAgent =
                UserAgent.parseUserAgentString(WebUtil.getCurrentRequest().getHeader("User-Agent"));
        if (userAgent != null) {
            return userAgent.getBrowser().getName();
        }
        return null;
    }

    /**
     * This method is used to get user os info.
     *
     * @return
     */
    public static String getUserOs() {
        UserAgent userAgent =
                UserAgent.parseUserAgentString(WebUtil.getCurrentRequest().getHeader("User-Agent"));
        if (userAgent != null) {
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            if (operatingSystem != null) {
                return operatingSystem.getName();
            }
        }
        return null;
    }

    /**
     * This method is used to get user device info.
     *
     * @return
     */
    public static String getUserDevice() {
        UserAgent userAgent =
                UserAgent.parseUserAgentString(WebUtil.getCurrentRequest().getHeader("User-Agent"));
        if (userAgent != null) {
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            if (operatingSystem != null) {
                return operatingSystem.getDeviceType().getName();
            }
        }
        return null;
    }

    /**
     * Will return context path
     *
     * @return
     */
    public static String getContextPath() {
        return WebUtil.getCurrentRequest().getContextPath() + "/";
    }

    /**
     * This method is used to get client ip address.
     *
     * @return
     */
    public static String getUserIpAddress() {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = WebUtil.getCurrentRequest().getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return new StringTokenizer(ip, ",").nextToken().trim();
            }
        }
        return WebUtil.getCurrentRequest().getRemoteAddr();
    }
}
