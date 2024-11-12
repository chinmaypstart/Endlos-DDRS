package com.endlosiot.aop;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.threadlocal.Auditor;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.model.UserModel;
import com.endlosiot.common.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * This will be used to perform access control validation. This aspect will be applied when method
 * has been annotated with Authorization Annotation.
 *
 * @version 1.0
 */
@Component
@Aspect
public class AuthorizationAspect {

    @Before("@annotation(authorization)")
    public void authorized(JoinPoint joinPoint, Authorization authorization)
            throws EndlosiotAPIException {
        UserModel userModel = Auditor.getAuditor();
        if (userModel == null) {
            LoggerService.info(
                    joinPoint.getSignature().getName().toUpperCase(),
                    " Unauthorized access, " + WebUtil.getCurrentRequest().getRequestURI(),
                    "");
            throw new EndlosiotAPIException(
                    ResponseCode.UNAUTHORIZED_ACCESS.getCode(),
                    ResponseCode.UNAUTHORIZED_ACCESS.getMessage());
        }

        RightsEnum rightsEnum = authorization.rights();
        ModuleEnum moduleEnum = authorization.modules();
        boolean hasAccess = userModel.hasAccess(Long.valueOf(moduleEnum.getId()), Long.valueOf(rightsEnum.getId()));
        if (!hasAccess) {
            LoggerService.info(
                    joinPoint.getSignature().getName().toUpperCase(),
                    " Unauthorized access of, " + WebUtil.getCurrentRequest().getRequestURI(),
                    " by "
                            + (StringUtils.isEmpty(userModel.getEmail())
                            ? userModel.getMobile()
                            : userModel.getEmail()));
            throw new EndlosiotAPIException(
                    ResponseCode.UNAUTHORIZED_ACCESS.getCode(),
                    ResponseCode.UNAUTHORIZED_ACCESS.getMessage());
        }
    }
}
