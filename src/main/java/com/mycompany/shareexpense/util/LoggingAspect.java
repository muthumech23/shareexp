
package com.mycompany.shareexpense.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

	private final Logger log = Logger.getLogger(this.getClass());

	
	@Pointcut("execution(* com.mycompany.shareexpense..*.*(..))")  
    public void servicePointcut(){}  
      
    @Around("servicePointcut()")  
    public Object myadvice(ProceedingJoinPoint joinPoint) throws Throwable   
    {  
    	StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object retVal = joinPoint.proceed();
		stopWatch.stop();

		String packageName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
		
		log.debug("Executing ==>"+packageName);
		
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
		log.info("Execution Completed [" + packageName + ": Time taken= " + stopWatch.getTotalTimeMillis() + " ms ]");
		
		return retVal; 
    }  
	

	@AfterReturning(pointcut = "execution(* com.mycompany.shareexpense..*.*(..))",
					returning = "retVal")
	public void afterReturningAdvice(Object retVal) {

		log.debug("\tReturn Params: " + retVal.toString());
	}

	/**
	 * This is the method which I would like to execute
	 * if there is an exception raised by any method.
	 */
	@AfterThrowing(	pointcut = "execution(* com.mycompany.shareexpense..*.*(..))",
					throwing = "ex")
	public void AfterThrowingAdvice(Throwable ex) {

		log.error("********************** DANGER *************************** \nThere has been an exception: " + ex.toString(), ex);
	}

}
