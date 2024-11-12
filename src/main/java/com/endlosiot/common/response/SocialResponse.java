package com.endlosiot.common.response;

/**
 * This is used to send response of view request for any model.
 *
 * @author Dhruvang Joshi
 * @since 09/02/2018
 */
public class SocialResponse extends CommonResponse {

    private static final long serialVersionUID = -1698438611739275048L;
    private String url;

    private SocialResponse(int responseCode, String message, String url) {
        super(responseCode, message);
        this.url = url;
    }

    public static SocialResponse create(int responseCode, String message, String url) {
        return new SocialResponse(responseCode, message, url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
