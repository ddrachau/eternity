package com.prodyna.pac.eternity.test.helper;

import com.prodyna.pac.eternity.common.service.impl.DatasourceProducer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Abstract base class for Arquillian tests.
 */
public abstract class AbstractArquillianTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "eternity-arq.jar");
        jar.addPackages(true, "com.prodyna.pac");
        jar.deleteClass(DatasourceProducer.class);
        jar.addAsResource("META-INF/beans.xml");

        // System.out.println(jar.toString(true));

        return jar;

    }

}
