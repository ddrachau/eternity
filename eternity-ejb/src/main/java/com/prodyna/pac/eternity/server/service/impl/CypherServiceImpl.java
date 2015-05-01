package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.profiling.Profiling;
import com.prodyna.pac.eternity.server.service.CypherService;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NonUniqueResultException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation for the CypherService.
 */
@Logging
@Profiling
@Stateless
public class CypherServiceImpl implements CypherService {

    @Inject
    private Logger logger;

    @Inject
    private DataSource dataSource;

    @Override
    public List<Map<String, Object>> query(final String query, final Map<Integer, Object> params) {

        Connection connection = null;

        try {

            connection = dataSource.getConnection();
            final PreparedStatement statement = connection.prepareStatement(query);

            setParameters(statement, params);

            final ResultSet resultSet = statement.executeQuery();

            List<Map<String, Object>> result = this.prepareQueryResult(resultSet);

            resultSet.close();
            statement.close();

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (Exception exception) {
                // if it cannot be closed, so be it we gave it a try
                logger.error("Cannot close connection", exception);
            }
        }

    }

    @Override
    public Map<String, Object> querySingle(String query, Map<Integer, Object> params) throws NonUniqueResultException {

        List<Map<String, Object>> result = this.query(query, params);

        if (result.size() > 1) {
            throw new NonUniqueResultException();
        } else if (result.size() == 0) {
            return null;
        } else {
            return result.get(0);
        }

    }

    /**
     * Sets all the parameter of the given map and puts them in the given statement.
     *
     * @param statement the target statement for the parameter
     * @param params    the params for the statement, empty or null if no parameters are needed
     * @throws SQLException in case the property could not be set properly
     */
    private void setParameters(final PreparedStatement statement, final Map<Integer, Object> params) throws SQLException {

        if (params != null) {
            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                statement.setObject(entry.getKey(), entry.getValue());
            }
        }

    }

    /**
     * Maps the result from the database to list.
     *
     * @param resultSet the input for the transformation
     * @return the transformed result
     * @throws SQLException in case the some SQL problem occurred while processing
     */
    private List<Map<String, Object>> prepareQueryResult(ResultSet resultSet) throws SQLException {

        List<Map<String, Object>> result = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        List<String> cols = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            cols.add(metaData.getColumnName(i));
        }

        boolean hasNext = resultSet.next();

        while (hasNext) {

            Map<String, Object> map = new LinkedHashMap<>();
            for (String col : cols) {
                Object value = resultSet.getObject(col);
                map.put(col, value);

                logger.debug("col:" + col + " value:" + value);
            }
            result.add(map);

            hasNext = resultSet.next();
        }

        return result;

    }

}