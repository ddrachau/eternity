package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.authentication.Session;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.authentication.SessionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.components.common.DateUtils.getCalendar;
import static com.prodyna.pac.eternity.components.common.DateUtils.getNow;
import static com.prodyna.pac.eternity.components.common.QueryUtils.map;

/**
 * Default implementation for the SessionService.
 */
@Logging
@Stateless
public class SessionServiceImpl implements SessionService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static final String SESSION_RETURN_PROPERTIES = "s.id, s.lastAccessedTime, s.createTime";

    @Inject
    private CypherService cypherService;

    @Override
    public Session create(@NotNull String userIdentifier) {

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) " +
                        "CREATE (s:Session {id:{2}, lastAccessedTime:{3}, createTime:{3}}) " +
                        "-[:ASSIGNED_TO]->(u) " +
                        "RETURN " + SESSION_RETURN_PROPERTIES,
                map(1, userIdentifier, 2, UUID.randomUUID().toString(), 3, getNow().getTimeInMillis()));

        if (queryResult == null) {

            throw new NotCreatedRuntimeException();

        } else {

            return this.getSession(queryResult);

        }

    }

    @Override
    public Session get(String sessionId) {

        Session result = null;

        if (sessionId != null) {
            final Map<String, Object> queryResult = cypherService.querySingle(
                    "MATCH (s:Session {id:{1}}) SET s.lastAccessedTime={2} " +
                            "RETURN " + SESSION_RETURN_PROPERTIES,
                    map(1, sessionId, 2, getNow().getTimeInMillis()));

            if (queryResult != null) {
                result = this.getSession(queryResult);
            }
        }

        return result;

    }

    @Override
    public List<Session> getByUser(@NotNull String userIdentifier) {

        List<Session> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User {identifier:{1}})<-[:ASSIGNED_TO]-(s:Session) " +
                        "RETURN " + SESSION_RETURN_PROPERTIES,
                map(1, userIdentifier));

        for (Map<String, Object> sessionParts : queryResult) {
            result.add(this.getSession(sessionParts));
        }

        return result;

    }

    @Override
    public void delete(@NotNull String identifier) {

        cypherService.query(
                "MATCH (s:Session {id:{1}})-[a:ASSIGNED_TO]->(:User)" +
                        "DELETE s,a",
                map(1, identifier));

    }

    @Override
    public void deleteByUser(@NotNull String userIdentifier) {

        cypherService.query(
                "MATCH (s:Session)-[a:ASSIGNED_TO]->(u:User {identifier:{1}})" +
                        "DELETE s,a",
                map(1, userIdentifier));

    }

    /**
     * Helper method to construct a Session from a query response
     *
     * @param values the available values
     * @return a filled Session
     */
    private Session getSession(Map<String, Object> values) {

        Session result = new Session();

        String readId = (String) values.get("s.id");
        long readLastAccessedTime = (long) values.get("s.lastAccessedTime");
        long readCreateTime = (long) values.get("s.createTime");

        result.setId(readId);
        result.setLastAccessedTime(getCalendar(readLastAccessedTime));
        result.setCreatedTime(getCalendar(readCreateTime));

        return result;

    }

}
