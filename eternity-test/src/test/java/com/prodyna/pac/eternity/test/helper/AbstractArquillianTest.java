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

//        System.out.println(jar.toString(true));

        return jar;

    }

//    @Deployment
//    public static Archive<?> createArchive() {
//        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war");
//        war.addAsWebInfResource(new File("src/test/resources/META-INF/persistence-test.xml"), "classes/META-INF/persistence.xml");
//        war.addPackages(true, "com.prodyna.conference");
//        war.addClass(com.prodyna.conference.web.rest.RESTEnabler.class);
//
//        /* Get all from the webapp in the web project, the pathes are relative */
//        war.as(ExplodedImporter.class).importDirectory("../conference-web/src/main/webapp");
//
//        /* Delete the beans.xml and add the local one */
//        war.delete("WEB-INF/beans.xml");
//        war.addAsWebInfResource(new File("src/test/resources/META-INF/beans.xml"), "classes/META-INF/beans.xml");
//        // System.out.println(war.toString(true));
//        return war;
//    }
}
