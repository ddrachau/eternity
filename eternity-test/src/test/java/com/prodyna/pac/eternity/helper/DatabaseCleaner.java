package com.prodyna.pac.eternity.helper;

import com.prodyna.pac.eternity.server.service.CypherService;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * A helper class for cleaning up the database. This class is used for testing only and is not part of the actual
 * packaging, because it contains code, that should not even be deployed on any important instance. It is used by the
 * test classes
 */
@Stateless
public class DatabaseCleaner {

    public static String CLEANUP_QUERY = "MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";

    @Inject
    private Logger logger;

    @Inject
    private CypherService cypherService;

    public void deleteAllData() {

        logger.warn("Deleting all nodes");
        cypherService.query(CLEANUP_QUERY, null);

    }

}
