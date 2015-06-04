package com.prodyna.pac.eternity.common.profiling.impl;

import com.prodyna.pac.eternity.common.profiling.Profiling;
import com.prodyna.pac.eternity.common.profiling.ProfilingMXBean;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Profiling interceptor which wraps around method calls on classes marked for @Profiling.
 */
@Interceptor
@Profiling
@Priority(Interceptor.Priority.APPLICATION)
public class ProfilingInterceptor {

    /**
     * JMX bean for storing profiling information
     */
    @Inject
    private ProfilingMXBean profilingMXBean;

    /**
     * Logs the execution time for method invocations flagged be @Profiling
     *
     * @param ic the invocation context of the interceptor
     * @return the execution result
     * @throws Exception an exception in case one occurred
     */
    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {

        long start = System.currentTimeMillis();
        Object ret = ic.proceed();
        long duration = System.currentTimeMillis() - start;

        profilingMXBean.addMethodExecutionDuration(
                ic.getTarget().getClass().getName() + "." + ic.getMethod().getName(), duration);

        return ret;

    }

}
