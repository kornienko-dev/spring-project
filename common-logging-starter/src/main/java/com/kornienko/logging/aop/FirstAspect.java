package com.kornienko.logging.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
public class FirstAspect {

    /*
        execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
     */
    @Pointcut("execution(public * com.kornienko.*.service.*Service.findById(*))")
    public void anyFindByIdServiceMethod() {
    }

    @Before(value = "anyFindByIdServiceMethod() " +
                    "&& args(id) " +
                    "&& target(service) " +
                    "&& this(serviceProxy) " +
                    "&& @within(transactional)", argNames = "joinPoint,id,service,serviceProxy,transactional")
//    @Before("execution(public * com.kornienko.spring.service.*Service.findById(*))")
    public void addLogging(JoinPoint joinPoint,
                           Long id,
                           Object service,
                           Object serviceProxy,
                           Transactional transactional) {
        log.info("before - invoked findById method in class {} with id {}", service, id);
    }

    @AfterReturning(value = "anyFindByIdServiceMethod() " +
                            "&& target(service)",
            returning = "result", argNames = "service,result")
    public void addLoggingAfterReturning(Object service, Object result) {
        log.info("after returning - invoked findById method in class {} with result {}", service, result);

    }

    @AfterThrowing(value = "anyFindByIdServiceMethod() " +
                           "&& target(service)",
            throwing = "exception")
    public void addLoggingAfterThrowing(Throwable exception, Object service) {
        log.info("after throwing - invoked findById method in class {} with exception {}: {}",
                service,
                exception.getClass(),
                exception.getMessage());
    }

    @After(value = "anyFindByIdServiceMethod() && target(service)")
    public void addLoggingAfterFinally(Object service) {
        log.info("after (finally) - invoked findById method in class {}", service);
    }
}
