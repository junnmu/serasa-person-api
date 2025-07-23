package com.serasa.personapi.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Before("restControllerMethods()")
    public void logRequest(JoinPoint joinPoint) {
        var methodSignature = (MethodSignature) joinPoint.getSignature();
        var className = methodSignature.getDeclaringType().getSimpleName();
        var methodName = methodSignature.getName();
        var args = Arrays.toString(joinPoint.getArgs());

        log.info("class={}, method={}, step=request, payload={}", className, methodName, args);
    }

    @AfterReturning(value = "restControllerMethods()", returning = "response")
    public void logResponse(JoinPoint joinPoint, ResponseEntity<?> response) {
        var methodSignature = (MethodSignature) joinPoint.getSignature();
        var className = methodSignature.getDeclaringType().getSimpleName();
        var methodName = methodSignature.getName();

        log.info("class={}, method={}, step=response, status={}, payload={}",
            className,
            methodName,
            response.getStatusCode().value(),
            response.getBody()
        );
    }
}
