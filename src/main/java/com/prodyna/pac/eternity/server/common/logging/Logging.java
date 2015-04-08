package com.prodyna.pac.eternity.server.common.logging;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
}
