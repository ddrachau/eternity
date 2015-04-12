package com.prodyna.pac.eternity.server.service;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Map;

/**
 * Wrapper service for executing cypher queries against the database.
 */
public interface CypherService {

    /**
     * Takes the given statement, adds the given parameter and executes the query.
     *
     * @param query the query statement to be executed against the database
     * @param params    the optional parameters needed by the query
     * @return the query result
     */
    List<Map<String, Object>> query(String query, Map<Integer, Object> params);


    /**
     * Takes the given statement, adds the given parameter and executes the query while returning
     * only one record or null if none was found.
     *
     * @param query the query statement to be executed against the database
     * @param params    the optional parameters needed by the query
     * @return the query result
     * @throws NonUniqueResultException if more than one record was found
     */
    Map<String, Object> querySingle(String query, Map<Integer, Object> params) throws NonUniqueResultException;

}
