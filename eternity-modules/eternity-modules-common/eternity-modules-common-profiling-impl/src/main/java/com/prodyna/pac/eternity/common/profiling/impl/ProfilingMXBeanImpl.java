package com.prodyna.pac.eternity.common.profiling.impl;

import com.prodyna.pac.eternity.common.profiling.MethodProfiling;
import com.prodyna.pac.eternity.common.profiling.ProfilingMXBean;

import javax.ejb.Singleton;
import javax.management.MalformedObjectNameException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The default implementation for ProfilingMXBean.
 */
@Singleton
public class ProfilingMXBeanImpl implements ProfilingMXBean {

    /**
     * map containing the collected profiling values for each method.
     */
    private final Map<String, MethodProfiling> methodProfilingHashMap;

    /**
     * Default constructor
     *
     * @throws MalformedObjectNameException if the object name is maformated
     */
    public ProfilingMXBeanImpl() throws MalformedObjectNameException {

        this.methodProfilingHashMap = new HashMap<>();

    }

    @Override
    public void addMethodExecutionDuration(final String fullQualifiedMethodName, final long duration) {

        final String cleanedMethodName = fullQualifiedMethodName.replace("$Proxy$_$$_WeldSubclass", "");
        this.getMethodProfiling(cleanedMethodName).addNewMethodExecutionDuration(duration);

    }

    @Override
    public void clearAllProfilingData() {

        this.methodProfilingHashMap.clear();

    }

    @Override
    public List<MethodProfiling> getAllProfilings() {

        final List<MethodProfiling> result = new ArrayList<>();
        result.addAll(this.methodProfilingHashMap.values());

        return result;

    }

    @Override
    public void clearProfilingDataBy(final String fullQualifiedMethodName) {

        this.methodProfilingHashMap.remove(fullQualifiedMethodName);

    }

    @Override
    public MethodProfiling getProfilingDataBy(final String fullQualifiedMethodName) {

        return this.methodProfilingHashMap.get(fullQualifiedMethodName);

    }

    @Override
    public long getMethodExecutionDurationMinimum(final String fullQualifiedMethodName) {

        return this.getMethodProfiling(fullQualifiedMethodName).getDurationMinimum();

    }

    @Override
    public long getMethodExecutionDurationAverage(final String fullQualifiedMethodName) {

        return this.getMethodProfiling(fullQualifiedMethodName).getDurationAverage();

    }

    @Override
    public long getMethodExecutionDurationMaximum(final String fullQualifiedMethodName) {

        return this.getMethodProfiling(fullQualifiedMethodName).getDurationMaximum();

    }

    /**
     * Gets or creates the MethodProfiling for the given fullQualifiedMethodName
     *
     * @param fullQualifiedMethodName the name of the method
     * @return the know profiling data
     */
    private MethodProfiling getMethodProfiling(final String fullQualifiedMethodName) {

        MethodProfiling methodProfiling = this.methodProfilingHashMap.get(fullQualifiedMethodName);
        if (methodProfiling == null) {
            methodProfiling = new MethodProfiling(fullQualifiedMethodName);
            this.methodProfilingHashMap.put(fullQualifiedMethodName, methodProfiling);
        }

        return methodProfiling;

    }

}
