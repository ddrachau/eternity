package com.prodyna.pac.eternity.common.logging.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Produces a logger instance for the requesting class.
 */
public class LoggerProducer {

    /**
     * Produces a logger instance for the requesting class.
     *
     * @param ip the InjectionPoint of the requesting class
     * @return the logger for the requesting class
     */
    @Produces
    public Logger loggerProducer(final InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
    }

}
