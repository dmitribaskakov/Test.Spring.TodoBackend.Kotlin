package org.home.todobackend.aop

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.aspectj.lang.annotation.Around
import kotlin.Throws
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect
@Component
class LoggingAspect {

    companion object {
        val log: Logger = LogManager.getLogger(LoggingAspect::class.java.name)
    }

    @Around("execution(* org.home.todobackend.controller..*(..))")
    fun profileControllerMethods(proceedingJoinPoint: ProceedingJoinPoint): Any {
        //Получаем сигнатуру методов
        val methodSignature = proceedingJoinPoint.signature as MethodSignature
        val className = methodSignature.declaringType.simpleName
        val methodName = methodSignature.name
        log.info("-------  $className.$methodName   ---- Start ---")
        val stopWatch = StopWatch()
        //Засекаем время выполнения метода
        stopWatch.start()
        //Выполняем сам метод
        val result = proceedingJoinPoint.proceed()
        stopWatch.stop()
        log.info("-------  $className.$methodName  ----   Execution :" + stopWatch.totalTimeMillis + "ms   -------")
        return result
    }
}