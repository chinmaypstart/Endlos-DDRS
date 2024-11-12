package com.endlosiot.common.notification.provider;

import com.endlosiot.common.notification.enums.ProviderType;

/**
 * This class provides Java mail send object base of provider type.
 *
 * @author Dhruvang.Joshi
 * @since 26/07/2017
 */
public class EmailProviderFactory {

    public static EmailProvider loadEmailProvider(ProviderType providerType) {
        if (ProviderType.SSL_SECURE.equals(providerType)) {
            return new EmailSSLProvider();
        }
        if (ProviderType.TLS_SECURE.equals(providerType)) {
            return new EmailTLSProvider();
        }
        return null;
    }
}
