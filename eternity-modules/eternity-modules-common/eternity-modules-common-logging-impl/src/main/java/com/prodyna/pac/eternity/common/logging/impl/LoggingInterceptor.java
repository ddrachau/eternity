package com.prodyna.pac.eternity.common.logging.impl;

import com.prodyna.pac.eternity.common.logging.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Logging interceptor which wraps around method calls on classes marked for @Logging.
 */
@Interceptor
@Logging
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    /**
     * Logs the execution for method invocations flagged be @Logging
     *
     * @param ic the invocation context of the interceptor
     * @return the execution result
     * @throws Exception an exception in case one occurred
     */
    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {

        Class<?> targetClass = ic.getTarget().getClass();
        Logger log = LoggerFactory.getLogger(targetClass.getName() + "<LoggingInterceptor>");

        String loggingName = targetClass.getName() + "." + ic.getMethod().getName();
        log.debug(">>> " + loggingName);
        Object ret = ic.proceed();
        log.debug("<<< " + loggingName);

        return ret;

    }

}
