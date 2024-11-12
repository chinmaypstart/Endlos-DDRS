/**
 *
 */
package com.endlosiot.common.util;

import com.endlosiot.common.file.enums.FileUploadType;
import com.endlosiot.common.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * Web request related utility.
 *
 * @author nirav
 * @since 27/12/2019
 */
public class WebUtil {

    private WebUtil() {
    }

    /**
     * It is used to get current http servlet request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    /**
     * To get the current response object.
     *
     * @return
     */
    public static HttpServletResponse getCurrentResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    }

    public static String getDomain(HttpServletRequest httpServletRequest) {
        String origin = httpServletRequest.getHeader("origin");
        return origin.substring(origin.indexOf("//") + 2, origin.indexOf("."));
    }

    /**
     * Send an error response in json from the filter.
     *
     * @param httpServletResponse
     * @param commonResponse
     * @throws IOException
     */
    public static void sendResponse(HttpServletResponse httpServletResponse, Response response) throws IOException {
        ObjectMapper json = new ObjectMapper();
        httpServletResponse.setContentType("application/json");
        json.writeValue(httpServletResponse.getOutputStream(), response);
    }

    /**
     * Send an error response in json from the filter.
     *
     * @param httpServletResponse
     * @param commonResponse
     * @throws IOException
     */
    public static void setCookie(String fileName, Long uploadTime, HttpServletResponse httpServletResponse) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        switch (FileUploadType.fromId(fileExtension)) {
            case pdf:
                httpServletResponse.setContentType("application/pdf");
                break;
            case jpeg:
                httpServletResponse.setContentType("image/jpeg");
                break;
            case png:
                httpServletResponse.setContentType("application/png");
                break;
            case jpg:
                httpServletResponse.setContentType("image/jpeg");
                break;
            case xls:
                httpServletResponse.setContentType("application/xls");
                break;
            case xlsx:
                httpServletResponse.setContentType("application/xlsx");
                break;
            case csv:
                httpServletResponse.setContentType("text/csv");
                break;
            case gif:
                httpServletResponse.setContentType("image/gif");
                break;
            case avi:
                httpServletResponse.setContentType("video/x-msvideo");
                break;
            case mp4:
                httpServletResponse.setContentType("video/mp4");
                break;
            case json:
                httpServletResponse.setContentType("application/json");
                break;
            case jfif:
                httpServletResponse.setContentType("image/jpeg");
                break;
            case heif:
                httpServletResponse.setContentType("image/heif");
                break;
            case svg:
                httpServletResponse.setContentType("image/svg+xml");
                break;
            case heic:
                httpServletResponse.setContentType("image/heic");
                break;
            default:
                break;
        }
        httpServletResponse.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        httpServletResponse.setHeader("Cache-control", "max-age=31536000");
        httpServletResponse.setHeader("Last-Modified", uploadTime.toString());
    }

}