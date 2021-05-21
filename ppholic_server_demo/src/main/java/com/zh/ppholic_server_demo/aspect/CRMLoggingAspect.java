package com.zh.ppholic_server_demo.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CRMLoggingAspect {
    
    // setup logger
    private Logger myLogger = Logger.getLogger(getClass().getName());

    // setup pointcut declarations
    @Pointcut("execution(* com.zh.ppholic_server_demo.rest.*.*(..))")
    private void forRESTControllerPackage() {};

    @Pointcut("execution(* com.zh.ppholic_server_demo.controller.*.*(..))")
    private void forControllerPackage() {};

    @Pointcut("execution(* com.zh.ppholic_server_demo.service.*.*(..))")
    private void forServicePackage() {};

    @Pointcut("execution(* com.zh.ppholic_server_demo.dao.*.*(..))")
    private void forDAOPackage() {};

    @Pointcut("forRESTControllerPackage() || (forControllerPackage() || (forServicePackage() || forDAOPackage()))")
    private void forAppFlow() {};

    // add @Before advice
    @Before("forAppFlow()")
    public void before(JoinPoint theJoinPoint){
        // display the method we're calling
        String theMethod = theJoinPoint.getSignature().toShortString();
        myLogger.info("=> in @Before, calling method: " + theMethod);

        // display the arguments
        Object[] args = theJoinPoint.getArgs();

        for (Object tempArg: args){
            myLogger.info("=> argument: " + tempArg);
        }
    }

    // add @AfterReturning advice
    @AfterReturning(pointcut = "forAppFlow()", returning = "theResult")
    public void afterReturning(JoinPoint theJoinPoint, Object theResult){
        // display the method we're calling
        String theMethod = theJoinPoint.getSignature().toShortString();
        myLogger.info("=> in @AfterReturning, calling method: " + theMethod);
 
        // display the result
        myLogger.info("=> result: " + theResult);
    }
}
