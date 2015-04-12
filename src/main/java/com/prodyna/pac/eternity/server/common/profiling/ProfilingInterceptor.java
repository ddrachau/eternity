package com.prodyna.pac.eternity.server.common.profiling;

import com.prodyna.pac.eternity.server.common.logging.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Profiling interceptor which wraps around method calls on classes marked for @Profiling.
 */
@Logging
@Interceptor
public class ProfilingInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {

        Logger log = LoggerFactory.getLogger(ic.getTarget().getClass().getName() + "<Interceptor>");

        long start = System.currentTimeMillis();
        Object ret = ic.proceed();
        log.info(ic.getMethod().getName() + "<<< took " + (System.currentTimeMillis() - start) + "ms");

        return ret;

    }

}
