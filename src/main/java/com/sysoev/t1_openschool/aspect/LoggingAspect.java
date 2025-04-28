package com.sysoev.t1_openschool.aspect;

import com.sysoev.t1_openschool.model.Task;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    //Логируем по аннотации входной Task
    @Before("@annotation(com.sysoev.t1_openschool.aspect.annotation.LogEnterTask)")
    public void logEnter(JoinPoint joinPoint) {
        log.info("Вызвали метод {}", joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            // Если это Task, можно вывести детали
            if (arg instanceof Task task) {
                log.info("Детали входных данных: id={}, title={}, description={}, userId={}",
                        task.getId(), task.getTitle(), task.getDescription(), task.getUserId());
            }
        }
    }

    //Логируем успешную обработку сервисов

    @AfterReturning(
            pointcut = "execution(* com.sysoev.t1_openschool.service..*(..))",
            returning = "result"
    )
    public void recordSuccessfulExecution(JoinPoint joinPoint, Object result) {
        if (result != null) {
            log.info("Успешно выполнен метод - {}, класса - {}, с результатом выполнения - {}",
                    joinPoint.getSignature().getName(),
                    joinPoint.getSourceLocation().getWithinType().getSimpleName(),
                    result);
        } else {
            log.info("Успешно выполнен метод - {}, класса - {}",
                    joinPoint.getSignature().getName(),
                    joinPoint.getSourceLocation().getWithinType().getSimpleName());
        }
    }

    //Логируем ошибки в сервисах
    @AfterThrowing(pointcut = "execution(* com.sysoev.t1_openschool.service..*(..))",
            throwing = "exception")
    public void recordFailedExecution(JoinPoint joinPoint, Exception exception) {
        log.error("Метод - {},  выдал исключение - {}, в классе {}",
                joinPoint.getSignature().getName(),
                exception.getMessage(),
                exception.getClass());
    }

    //Замер времени выполнения
    @Around("@annotation(com.sysoev.t1_openschool.aspect.annotation.ExecutionTimeMeasuring)")
    public Object timeDetected(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            log.info("Метод: {} отработал за: {} ms",
                    joinPoint.getSignature().getName(),
                    (endTime - startTime));

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Произошла ошибка в методе: {}", joinPoint.getSignature().getName());
            throw e;
        }
    }
}
