package com.prodyna.pac.eternity.test.client.helper;

import com.prodyna.pac.eternity.test.helper.AbstractArquillianTest;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URL;

/**
 * Extends the @see FullDeployment and offers methods for creating Dynamic Proxies for REST clients.
 */
public abstract class AbstractRESTTest extends AbstractArquillianTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @ArquillianResource
    protected URL url;

    protected Client createClient() {

        final Client client = ClientBuilder.newClient();
        return client;

    }

    protected ResteasyWebTarget createWebTarget() {

        String fullPath = url.toString() + "rest";
        log.info("URL = " + fullPath);
        ResteasyWebTarget target = (ResteasyWebTarget) createClient().target(fullPath);
        return target;

    }

    protected <C> C createService(Class<C> ifaceType) {

        return this.createWebTarget().proxy(ifaceType);

    }

}
