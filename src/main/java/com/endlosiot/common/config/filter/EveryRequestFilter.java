package com.endlosiot.common.config.filter;


import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.logger.Uuid;
import com.endlosiot.common.util.Constant;
import com.endlosiot.common.util.Utility;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * This is every request filter. Any request coming from any Device will pass through this filter.
 * It checked cross domain values and allow access based on it.
 *
 * @author Nirav.Shah
 * @since 18/09/2023
 */
@Component
public class EveryRequestFilter implements Filter {

    private static final String EVERY_REQUEST_FILTER = "EveryRequestFilter";

    private ApplicationContext applicationContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.setApplicationContext(
                WebApplicationContextUtils.getRequiredWebApplicationContext(
                        filterConfig.getServletContext()));
        ImageIO.scanForPlugins();
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String originHeader = httpServletRequest.getHeader("Origin");
        AllowDomainConfiguration allowDomainConfiguration =
                (AllowDomainConfiguration) applicationContext.getBean("allowDomainConfiguration");

        if (!StringUtils.isBlank(originHeader)) {
            if (!allowDomainConfiguration.isAllowDomain(originHeader)) {
                return;
            }
            httpServletResponse.addHeader("Access-Control-Allow-Origin", originHeader);
            httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.addHeader(
                    "Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            httpServletResponse.addHeader(
                    "Access-Control-Allow-Headers",
                    "Content-Type, cache-control, x-requested-with, "
                            + Constant.AUTHORIZATION
                            + ", "
                            + Constant.REFRESH_TOKEN);
            if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
                httpServletResponse.addHeader("Access-Control-Max-Age", "84600");
                return;
            }
        }

        Uuid.setUuid(Utility.generateUuid());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        Uuid.removeUuid();
    }

    @Override
    public void destroy() {
        LoggerService.info(EVERY_REQUEST_FILTER, "Destroy", "Tomcat is been stopped");
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
