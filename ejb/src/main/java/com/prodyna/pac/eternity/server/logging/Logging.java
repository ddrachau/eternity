package com.prodyna.pac.eternity.server.logging;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation interface for marking a class to be logged.
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
}
