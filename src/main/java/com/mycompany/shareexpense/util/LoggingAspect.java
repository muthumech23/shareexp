
package com.mycompany.shareexpense.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

@Aspect
public class LoggingAspect {

	private final Logger	log	= Logger.getLogger(this.getClass());

	@Around("execution(* com.mycompany.shareexpense.*.*(..))")
	public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object retVal = joinPoint.proceed();
		stopWatch.stop();

		StringBuffer logMessage = new StringBuffer();
		logMessage.append(joinPoint.getTarget().getClass().getName());
		logMessage.append(".");
		logMessage.append(joinPoint.getSignature().getName());
		logMessage.append("(");

		// append args
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			logMessage.append(args[i]).append(",");
		}
		if (args.length > 0) {
			logMessage.deleteCharAt(logMessage.length() - 1);
		}
		logMessage.append(")");
		logMessage.append(" execution time: ");
		logMessage.append(stopWatch.getTotalTimeMillis());
		logMessage.append(" ms");
		log.debug(logMessage.toString());

		return retVal;
	}

	@AfterReturning(pointcut = "execution(* com.mycompany.shareexpense.*.*(..))",
					returning = "retVal")
	public void afterReturningAdvice(Object retVal) {

		log.debug("Returning:" + retVal.toString());
	}

	/**
	 * This is the method which I would like to execute
	 * if there is an exception raised by any method.
	 */
	@AfterThrowing(	pointcut = "execution(* com.mycompany.shareexpense.*.*(..))",
					throwing = "ex")
	public void AfterThrowingAdvice(Throwable ex) {

		log.error("There has been an exception: " + ex.toString(), ex);
	}

}
