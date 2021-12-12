package org.home.todobackend.aop;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Log
@Aspect
@Component

public class LoggingAspect {

    @Around("execution(* org.home.todobackend.controller..*(..))")
    public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //Получаем сигнатуру методов
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        log.info("-------  "+className+"."+methodName+"   -------");
        final StopWatch stopWatch = new StopWatch();
        //Засекаем время выполнения метода
        stopWatch.start();
        //Выполняем сам метод
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();
        log.info("-------  "+className+"."+methodName+"   ----   Execution :"+stopWatch.getTotalTimeMillis()+"ms   -------");
        return result;
    }
}
