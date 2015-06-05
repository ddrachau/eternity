package com.prodyna.pac.eternity.common.profiling;

/**
 * Wrapper for profiling information for a method call duration
 */
public class MethodProfiling {

    /**
     * the method name of the method the profiling data ist collected for
     */
    private final String methodName;

    /**
     * the amount of method calls
     */
    private long invocationCount = 0;

    /**
     * total time spent executing this method
     */
    private long durationTotal = 0;

    /**
     * the fastest execution for this method
     */
    private long durationMinimum = Long.MAX_VALUE;

    /**
     * the longest execution for this method
     */
    private long durationMaximum = 0;

    /**
     * Default constructor
     *
     * @param methodName the name this instance is data collecting for
     */
    public MethodProfiling(final String methodName) {

        this.methodName = methodName;

    }

    /**
     * Default getter
     *
     * @return the methodName
     */
    public String getMethodName() {

        return this.methodName;

    }

    /**
     * Default getter
     *
     * @return the invocationCount
     */
    public long getInvocationCount() {

        return this.invocationCount;

    }

    /**
     * Default getter
     *
     * @return the durationTotal
     */
    public long getDurationTotal() {

        return this.durationTotal;

    }

    /**
     * Default getter
     *
     * @return the durationMinimum
     */
    public long getDurationMinimum() {

        return this.durationMinimum;

    }

    /**
     * Default getter
     *
     * @return the durationMaximum
     */
    public long getDurationMaximum() {

        return this.durationMaximum;

    }

    /**
     * @return the average duration
     */
    public final long getDurationAverage() {

        if (this.invocationCount == 0) {
            return 0;
        } else {
            return (long) ((double) this.durationTotal / (double) this.invocationCount);
        }

    }

    /**
     * adds new method execution duration
     *
     * @param duration the time the execution took
     */
    public final void addNewMethodExecutionDuration(final long duration) {

        this.durationMinimum = Math.min(duration, this.durationMinimum);
        this.durationMaximum = Math.max(duration, this.durationMaximum);
        this.durationTotal += duration;
        this.invocationCount++;

    }

    @Override
    public String toString() {

        return "MethodProfiling{" +
                "methodName='" + methodName + '\'' +
                ", invocationCount=" + invocationCount +
                ", durationAverage=" + getDurationAverage() +
                ", durationTotal=" + durationTotal +
                ", durationMinimum=" + durationMinimum +
                ", durationMaximum=" + durationMaximum +
                '}';
    }

}
