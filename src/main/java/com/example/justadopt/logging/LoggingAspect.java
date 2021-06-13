package com.example.justadopt.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.justadopt.app.controller..*(..)))")
    public void requestPetsMethods() {
        // nothing to do here
    }

    @Pointcut("execution(* com.example.justadopt.security.controller..*(..)))")
    public void requestSecurityMethods() {
        // nothing to do here
    }

    @Around("requestSecurityMethods() || requestPetsMethods()")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        LOGGER.info(String.format("Execution time of %s.%s :: %s ms", className, methodName, stopWatch.getTotalTimeMillis()));

        return result;
    }
}
