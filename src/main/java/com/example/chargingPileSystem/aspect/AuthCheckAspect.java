package com.example.chargingPileSystem.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Aspect
@Component
@Order(2)
public class AuthCheckAspect {
    private static final String EXECUTE = "execution(* com.example.chargingPileSystem.controller.*.*(..))";
    public static Map<String, Object> claims;
    @Resource
    private HttpServletRequest httpServletRequest;

    // 定义切点
    @Pointcut(value = EXECUTE)
    public void pointCut() {
    }

    ;

    @Around("pointCut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        httpServletRequest.getParameter("userPhone");
        claims.put("userPhone", httpServletRequest.getParameter("userPhone"));
        claims.put("role", httpServletRequest.getParameter("role"));
        joinPoint.proceed();
    }

}
