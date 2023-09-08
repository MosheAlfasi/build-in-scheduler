package com.lym.buildin.scheduler.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@ConditionalOnExpression("${aspect.enabled:true}")
@RequiredArgsConstructor
public class ControllerAspect {
    private final Logger logger = LogManager.getLogger(ControllerAspect.class.getName());

    @Around("within(@org.springframework.web.bind.annotation.RestController *) && execution(* *(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get action data
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String sourceName = className + "." + methodName;

        // Get HTTP data
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String resource = request.getRequestURL().toString();
        String httpVerb = request.getMethod();

        logger.info("Incoming request | resource: {} | HTTP Verb: {} | method: {}", resource, httpVerb, sourceName);

        Object result = joinPoint.proceed();

        return result;
    }
}
