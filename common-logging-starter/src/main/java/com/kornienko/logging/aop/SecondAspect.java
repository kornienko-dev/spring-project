package com.kornienko.logging.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class SecondAspect {

    @Around(value = "com.kornienko.logging.aop.FirstAspect.anyFindByIdServiceMethod() && target(service) && args(id)", argNames = "joinPoint,service,id")
    public Object addLoggingAround(ProceedingJoinPoint joinPoint, Object service, Object id) throws Throwable {
        log.info("AROUND before - invoked findById method in class {} with id {}", service, id);
        try {
            Object result = joinPoint.proceed();
            log.info("AROUND after returning - invoked findById method in class {} with result {}", service, result);
            return result;
        } catch (Throwable exception) {
            log.info("AROUND after throwing - invoked findById method in class {} with exception {}: {}",
                    service,
                    exception.getClass(),
                    exception.getMessage());
            throw exception;
        } finally {
            log.info("AROUND after (finally) - invoked findById method in class {}", service);
        }
    }
}
