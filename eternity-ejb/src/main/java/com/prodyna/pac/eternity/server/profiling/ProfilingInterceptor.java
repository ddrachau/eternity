package com.prodyna.pac.eternity.server.profiling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Profiling interceptor which wraps around method calls on classes marked for @Profiling.
 */
@Profiling
@Interceptor
public class ProfilingInterceptor {

    /**
     * Logs the execution time for method invocations flagged be @Profiling
     *
     * @param ic the invocation context of the interceptor
     * @return the execution result
     * @throws Exception an exception in case one occurred
     */
    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {

        Logger log = LoggerFactory.getLogger(ic.getTarget().getClass().getName() + "<Interceptor>");

        long start = System.currentTimeMillis();
        Object ret = ic.proceed();
        log.info(ic.getMethod().getName() + "<<< took " + (System.currentTimeMillis() - start) + "ms");

        return ret;

    }

}
