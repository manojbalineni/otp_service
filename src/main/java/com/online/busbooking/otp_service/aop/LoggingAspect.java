package com.online.busbooking.otp_service.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.online.busbooking.otp_service.controller..*(..))")
    public void controllerLayer(){

    }

    @Pointcut("execution(* com.online.busbooking.otp_service.service.impl..*(..))")
    public void serviceLayer(){

    }

    @Before("controllerLayer()")
    public void logBefore(JoinPoint joinPoint){
        System.out.println("======= LOGGING AOP WORKING =======");
        logger.info("Controller : {} called with arguments : {}" , joinPoint.getSignature() , joinPoint.getArgs());
    }

    @AfterReturning(value =  "controllerLayer()" , returning = "result")
    public void logAfter(JoinPoint joinPoint , Object result){
        logger.info("Response from {} : {}" , joinPoint.getSignature() , result);
    }

    @AfterThrowing(value = "controllerLayer()" , throwing = "ex")
    public void logException(JoinPoint joinPoint , Throwable ex){
        logger.error("Exception in {} : {} " , joinPoint.getSignature() , ex.getMessage());
    }

    @Before("serviceLayer()")
    public void logBeforeService(JoinPoint joinPoint){
        logger.info("Service Layer : {} called with arguments {}" , joinPoint.getSignature() , joinPoint.getArgs());
    }

    @AfterReturning(value = "serviceLayer()" , returning = "result")
    public void logAfterInService(JoinPoint joinPoint , Object result){
        logger.info("Response from the service method - {} is {}" , joinPoint.getSignature() , joinPoint.getArgs());
    }

    @AfterThrowing(value = "serviceLayer()" ,throwing = "ex")
    public void logExceptionInService(JoinPoint joinPoint , Throwable ex){
        logger.error("Exception occurred in the service method {} is {} " , joinPoint.getSignature() , ex.getMessage());
    }

}
