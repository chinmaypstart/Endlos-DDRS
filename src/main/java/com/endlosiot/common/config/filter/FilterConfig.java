package com.endlosiot.common.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Collections;

/**
 * This class set default view resolver.
 *
 * @author Nirav.Shah
 * @since 18/09/2023
 */
@Configuration
public class FilterConfig extends WebMvcConfigurationSupport {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<EveryRequestFilter> everyRequestFilter() {
        FilterRegistrationBean<EveryRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new EveryRequestFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<PublicRequestFilter> publicRequestFilter() {
        FilterRegistrationBean<PublicRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new PublicRequestFilter());
        filterRegistrationBean.addUrlPatterns("/public/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<PrivateRequestFilter> privateRequestFilter() {
        FilterRegistrationBean<PrivateRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new PrivateRequestFilter());
        filterRegistrationBean.addUrlPatterns("/private/*");
        return filterRegistrationBean;
    }
}
