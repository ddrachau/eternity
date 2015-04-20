package com.prodyna.pac.eternity.server.service.arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Abstract base class for Arquillian tests.
 */
public abstract class AbstractArquillianTest {

    public static String CLEANUP_QUERY = "MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";
    private static JavaArchive archive = null;

    @Deployment
    public static JavaArchive createDeployment() {
        return AbstractArquillianTest.getArchive();
    }

    /**
     * Creates the default deployment if it is not already created
     *
     * @return the deployment
     */
    private static JavaArchive getArchive() {

        if (AbstractArquillianTest.archive == null) {
            JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "eternity-arq.jar").addPackages(true, "com.prodyna.pac");
            jar.addAsResource("META-INF/beans.xml");
            jar.addAsResource("META-INF/persistence.xml");
            System.out.println(jar.toString(true));
            AbstractArquillianTest.archive = jar;
        }

        return AbstractArquillianTest.archive;

    }

}
