package com.example.chargingPileSystem.aspect;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.chargingPileSystem.annotation.AllowedRole;
import com.example.chargingPileSystem.exception.AccessForbiddenException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class RoleAspect {
    private final String execution = "execution(* com.example.chargingPileSystem.controller.*.*(..))";

    @Pointcut(value = execution)
    public void pointcut() {}


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
       MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
       Method method = methodSignature.getMethod();
       AllowedRole roleAspect = method.getAnnotation(AllowedRole.class);
       if(roleAspect == null){
           return joinPoint.proceed();
       }
       int RequireRole = roleAspect.role();
       int RequestRole = (int)AuthCheckAspect.claims.get("role");
       if (RequireRole > RequestRole ){
           throw new AccessForbiddenException();
       }

       return joinPoint.proceed();
    }
}
