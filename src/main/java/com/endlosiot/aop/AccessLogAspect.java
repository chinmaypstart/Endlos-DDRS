package com.endlosiot.aop;

import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.threadlocal.Auditor;
import com.endlosiot.common.user.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Access logger aspect is log requested URI with the time taken by that URL.
 *
 * @author Nirav.Shah
 * @since 18/09/2023
 */
@Component
@Aspect
public class AccessLogAspect {

    /**
     * Used to log requested url & time taken by url.
     *
     * @param pjp @apiNote
     * @return Object @apiNote
     * @throws Throwable @return
     */
    @Around("@annotation(accessLog)")
    public Object around(ProceedingJoinPoint pjp, AccessLog accessLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            HttpServletRequest httpServletRequest =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            UserModel userModel = Auditor.getAuditor();
            if (userModel == null) {
                LoggerService.info(
                        pjp.getSignature().getName().toUpperCase(),
                        httpServletRequest.getRequestURI(),
                        (System.currentTimeMillis() - startTime) + "ms");
            } else {
                if (StringUtils.isBlank(userModel.getEmail())) {
                    LoggerService.info(
                            pjp.getSignature().getName().toUpperCase(),
                            userModel.getMobile() + " Requested for, " + httpServletRequest.getRequestURI(),
                            (System.currentTimeMillis() - startTime) + "ms");
                } else {
                    LoggerService.info(
                            pjp.getSignature().getName().toUpperCase(),
                            userModel.getEmail() + " Requested for, " + httpServletRequest.getRequestURI(),
                            (System.currentTimeMillis() - startTime) + "ms");
                }
            }
        }
    }
}
