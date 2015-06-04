package com.prodyna.pac.eternity.common.service.impl;

import javax.enterprise.inject.Produces;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

/**
 * Produces the MBeanServer
 */
public class MBeanServerProducer {

    /**
     * produces the MBeanServer
     *
     * @return the platform MBean server
     */
    @Produces
    public MBeanServer produceMBeanServer() {

        return ManagementFactory.getPlatformMBeanServer();

    }

}
