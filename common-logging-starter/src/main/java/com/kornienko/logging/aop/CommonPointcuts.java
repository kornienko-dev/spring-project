package com.kornienko.logging.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonPointcuts {

    /*
            @within - check annotation on class level
        */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void isControllerLayer() {
    }

    /*
        within - check class type name
     */
    @Pointcut("within(com.kornienko.*.service.*Service)")
    public void isServiceLayer() {
    }
}
