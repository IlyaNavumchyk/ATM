package com.navumchyk.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
@Aspect
public class LoggingAspect {


    @Pointcut("within(com.navumchyk..*)")
    private void securitiesAndServicesClasses() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service))")
    private void classesWithServiceAnnotation() {
    }

    @Pointcut("execution(public * *(..))")
    private void executePublicMethods() {
    }

    @Pointcut("executePublicMethods() && securitiesAndServicesClasses() && classesWithServiceAnnotation()")
    private void aroundSecurityAndServicePointcut() {
    }

    @Around("aroundSecurityAndServicePointcut()")
    public Object logAroundServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getSignature();

        Object[] args = joinPoint.getArgs();
        if ((args.length != 0) && (args[0] instanceof HashMap)) {
            args[0] = "database";
        }

        log.warn("Method {} start with args {}.",
                signature.getName(), args);

        Object proceed;
        try {

            proceed = joinPoint.proceed();
            log.warn("Method {} successful finished", signature.getName());
            return proceed;
        } catch (Throwable e) {
            log.error("Method {} threw an exception {} with massage {}",
                    signature.getName(), e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }
}
