package com.endlosiot.common.notification.provider;

import java.util.Properties;

/**
 * This interface declares mail sender method, which will be implemented by different types of providers.
 *
 * @author Dhruvang.Joshi
 * @since 26/07/2017
 */
public interface EmailProvider {
    /**
     * This method is used to set email properties base on given provider type.
     *
     * @param properties
     * @param port
     */
    public void setProperties(Properties properties, int port);
}
