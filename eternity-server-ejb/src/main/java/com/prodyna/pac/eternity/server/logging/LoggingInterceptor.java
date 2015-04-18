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

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {

        Logger log = LoggerFactory.getLogger(ic.getTarget().getClass().getName() + "<Interceptor>");

        log.info(">>> " + ic.getMethod().getName());
        Object ret = ic.proceed();
        log.info("<<< " + ic.getMethod().getName());

        return ret;

    }

}
