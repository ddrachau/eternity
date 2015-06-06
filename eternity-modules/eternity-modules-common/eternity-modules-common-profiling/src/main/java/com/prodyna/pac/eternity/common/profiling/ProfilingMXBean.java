package com.prodyna.pac.eternity.common.profiling;

import java.util.List;

/**
 * MXBean to collect profiling data method executions.
 */
public interface ProfilingMXBean {

    /**
     * Adds a new method execution duration to the profiling data
     *
     * @param fullQualifiedMethodName the executed method including the class name
     * @param duration                the duration of the method execution in milliseconds
     */
    void addMethodExecutionDuration(String fullQualifiedMethodName, long duration);

    /**
     * Clears all profiling data
     */
    void clearAllProfilingData();

    /**
     * Returns all collected profiling data
     *
     * @return all method profiling data
     */
    List<MethodProfiling> getAllProfilings();

    /**
     * Clears the profiling data for given method
     *
     * @param fullQualifiedMethodName the full qualified method name
     */
    void clearProfilingDataBy(String fullQualifiedMethodName);

    /**
     * Get the MethodProfiling data for the given method name
     *
     * @param fullQualifiedMethodName the full qualified method name
     * @return the method profiling data for the requested method
     */
    MethodProfiling getProfilingDataBy(String fullQualifiedMethodName);

    /**
     * Retrieves the minimal execution duration for given method
     *
     * @param fullQualifiedMethodName the full qualified method name
     * @return the minimal execution duration for given method
     */
    long getMethodExecutionDurationMinimum(String fullQualifiedMethodName);

    /**
     * Retrieves the average execution duration for given method
     *
     * @param fullQualifiedMethodName the full qualified method name
     * @return the average execution duration for given method
     */
    long getMethodExecutionDurationAverage(String fullQualifiedMethodName);

    /**
     * Retrieves the maximal execution duration for given method
     *
     * @param fullQualifiedMethodName the full qualified method name
     * @return the maximal execution duration for given method
     */
    long getMethodExecutionDurationMaximum(String fullQualifiedMethodName);

}
