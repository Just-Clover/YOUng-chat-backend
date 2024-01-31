package com.clover.youngchat.global.aop;


import java.lang.reflect.Method;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j(topic = "LogAop")
@Aspect
@Component
public class LogAop {

    private static final String TRACE_ID = "traceId";

    @Pointcut("execution(* com.clover.youngchat..*Controller.*(..))")
    public void controller() {
    }

    @Pointcut("execution(* com.clover.youngchat..*Service.*(..))")
    public void service() {
    }

    @Before("controller()")
    public void beforeLogic(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                log.info("method = {} type = {} value = {}", method.getName(),
                    arg.getClass().getSimpleName(), arg);
            }
        }
    }

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterLogic(JoinPoint joinPoint, Object returnValue) throws Throwable {
        log.info("returnValue = {}", returnValue);
        MDC.clear();
    }
}
