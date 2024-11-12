package com.endlosiot.common.notification.provider;

import java.util.Properties;

/**
 * This class provides java mail sender object for tsl provider.
 *
 * @author Dhruvang.Joshi
 * @since 26/07/2017
 */
public class EmailTLSProvider implements EmailProvider {

    @Override
    public void setProperties(Properties properties, int port) {
        properties.put("mail.smtp.starttls.enable", true);
    }

}
