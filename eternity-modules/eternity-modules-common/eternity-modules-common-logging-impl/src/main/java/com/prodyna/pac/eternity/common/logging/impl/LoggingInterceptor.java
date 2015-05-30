package com.prodyna.pac.eternity.common.logging.impl;

import com.prodyna.pac.eternity.common.logging.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * Logging interceptor which wraps around method calls on classes marked for @Logging.
 */
@Logging
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor implements Serializable {

    /**
     * Logs the execution for method invocations flagged be @Logging
     *
     * @param ic the invocation context of the interceptor
     * @return the execution result
     * @throws Exception an exception in case one occurred
     */
    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {

        Logger log = LoggerFactory.getLogger(ic.getTarget().getClass().getName() + "<LoggingInterceptor>");

        log.info(">>> " + ic.getMethod().getName());
        Object ret = ic.proceed();
        log.info("<<< " + ic.getMethod().getName());

        return ret;

    }

}
