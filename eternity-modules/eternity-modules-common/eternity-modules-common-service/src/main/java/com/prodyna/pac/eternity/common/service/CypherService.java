package com.prodyna.pac.eternity.common.service;

import com.prodyna.pac.eternity.common.model.FilterRequest;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * Wrapper service for executing cypher queries against the database.
 */
@Local
public interface CypherService {

    /**
     * Takes the given statement, adds the given parameter and executes the query.
     *
     * @param query  the query statement to be executed against the database
     * @param params the optional parameters needed by the query
     * @return the query result
     */
    List<Map<String, Object>> query(String query, Map<Integer, Object> params);

    /**
     * Takes the given statement, adds the given parameter and executes the query.
     *
     * @param query         the query statement to be executed against the database
     * @param params        the optional parameters needed by the query
     * @param filterRequest optional filter possibilities
     * @return the query result
     */
    List<Map<String, Object>> query(String query, Map<Integer, Object> params, FilterRequest filterRequest);

    /**
     * Takes the given statement, adds the given parameter and executes the query while returning only one record or
     * null if none was found.
     *
     * @param query  the query statement to be executed against the database
     * @param params the optional parameters needed by the query
     * @return the query result
     */
    Map<String, Object> querySingle(String query, Map<Integer, Object> params);

    /**
     * Takes the given statement, adds the given parameter and executes the query while returning only one record or
     * null if none was found.
     *
     * @param query         the query statement to be executed against the database
     * @param params        the optional parameters needed by the query
     * @param filterRequest optional filter possibilities
     * @return the query result
     */
    Map<String, Object> querySingle(String query, Map<Integer, Object> params, FilterRequest filterRequest);

}
