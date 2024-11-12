package com.endlosiot.common.locale;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * This is a transaction email configuration which specify when to run this scheduler and and how
 * many record to be processed at a time.
 *
 * @author chetanporwal
 * @since 29/08/2023
 */
@Data
@Configuration
@ComponentScan
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class LocaleConfiguration {

    //    @Value("${system.default.locale.country}")
    private String country;

    //  @Value("${system.default.locale.lang}")
    private String lang;

    private String defaultLocale;

    @PostConstruct
    public void setDefaultLocale() {
        this.defaultLocale = this.lang + "-" + this.country;
    }
}
