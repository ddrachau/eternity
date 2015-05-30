package com.prodyna.pac.eternity.common.profiling;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation interface for marking a class to be profiled.
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Profiling {
}
