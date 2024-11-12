package com.endlosiot.common.notification.provider;

import java.util.Properties;

/**
 * This class provides java mail sender object for ssl provider.
 *
 * @author Dhruvang.Joshi
 * @since 26/07/2017
 */
public class EmailSSLProvider implements EmailProvider {

    @Override
    public void setProperties(Properties properties, int port) {
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.fallback", "false");
    }

}
