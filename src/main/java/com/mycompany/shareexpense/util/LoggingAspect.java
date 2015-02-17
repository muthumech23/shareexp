package com.mycompany.shareexpense.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


/**
 * DOCUMENT ME!
 *
 * @author Muthukumaran Swaminathan
 * @version $Revision$
 */
@Aspect
@Component
public class LoggingAspect {
    /**
     * DOCUMENT ME!
     */
    private final Logger log = Logger.getLogger(this.getClass());


    /**
     * DOCUMENT ME!
     */
    @Pointcut("execution(* com.mycompany.shareexpense..*.*(..))")
    public void servicePointcut() {
    }


    /**
     * DOCUMENT ME!
     *
     * @param joinPoint DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Throwable DOCUMENT ME!
     */
    @Around("servicePointcut()")
    public Object myadvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object retVal = null;

        try {
            StopWatch stopWatch = new StopWatch();

            stopWatch.start();

            retVal = joinPoint.proceed();
            stopWatch.stop();

            String packageName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();

            log.debug("Executing ==>" + packageName);

            StringBuffer logMessage = new StringBuffer();

            logMessage.append("\tInput Params: ");

            // append args
            Object[] args = joinPoint.getArgs();

            for (int i = 0; i < args.length; i++) {
                logMessage.append(args[i]).append(",");
            }


            if (args.length > 0) {
                logMessage.deleteCharAt(logMessage.length() - 1);
            }


            log.debug(logMessage.toString());
            log.info("Execution Completed [" + packageName + ": Time taken= " + stopWatch.getTotalTimeMillis() +
                    " ms ]");
        } catch (Exception e) {
            log.error("********************** DANGER *************************** \nThere has been an exception myadvice: " + e + "", e);
            throw e;
        }


        return retVal;
    }


    /**
     * DOCUMENT ME!
     *
     * @param retVal DOCUMENT ME!
     */
    @AfterReturning(pointcut = "execution(* com.mycompany.shareexpense..*.*(..))", returning = "retVal")
    public void afterReturningAdvice(Object retVal) throws Throwable {
        try {
            log.debug("\tReturn Params: " + retVal + "");
        } catch (Exception ex) {
            log.error("********************** DANGER *************************** \nThere has been an exception afterReturningAdvice: " + ex + "", ex);
            throw ex;
        }
    }


    /**
     * This is the method which I would like to execute if there is an exception raised by any method.
     *
     * @param ex DOCUMENT ME!
     */
    @AfterThrowing(pointcut = "execution(* com.mycompany.shareexpense..*.*(..))", throwing = "ex")
    public void AfterThrowingAdvice(Throwable ex) throws Throwable {
        try {
            log.error("********************** DANGER *************************** \nThere has been an exception: " + ex + "", ex);
            throw ex;
        } catch (Exception e) {
            log.error("********************** DANGER *************************** \nThere has been an exception AfterThrowingAdvice: " + e + "", e);
            throw e;
        }
    }
}
