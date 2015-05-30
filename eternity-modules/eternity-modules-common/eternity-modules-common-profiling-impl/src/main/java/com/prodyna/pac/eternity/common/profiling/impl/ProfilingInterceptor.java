package com.prodyna.pac.eternity.common.profiling.impl;

import com.prodyna.pac.eternity.common.profiling.Profiling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * Profiling interceptor which wraps around method calls on classes marked for @Profiling.
 */
@Profiling
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class ProfilingInterceptor implements Serializable {

    /**
     * Logs the execution time for method invocations flagged be @Profiling
     *
     * @param ic the invocation context of the interceptor
     * @return the execution result
     * @throws Exception an exception in case one occurred
     */
    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {

        Logger log = LoggerFactory.getLogger(ic.getTarget().getClass().getName() + "<ProfilingInterceptor>");

        long start = System.currentTimeMillis();
        Object ret = ic.proceed();
        log.info(ic.getMethod().getName() + "<<< took " + (System.currentTimeMillis() - start) + "ms");

        return ret;

    }

}
