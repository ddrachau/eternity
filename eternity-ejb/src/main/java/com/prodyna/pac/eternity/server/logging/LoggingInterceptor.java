package com.prodyna.pac.eternity.server.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Logging interceptor which wraps around method calls on classes marked for @Logging.
 */
@Logging
@Interceptor
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

        Logger log = LoggerFactory.getLogger(ic.getTarget().getClass().getName() + "<Interceptor>");

        log.debug(">>> " + ic.getMethod().getName());
        Object ret = ic.proceed();
        log.debug("<<< " + ic.getMethod().getName());

        return ret;

    }

}
