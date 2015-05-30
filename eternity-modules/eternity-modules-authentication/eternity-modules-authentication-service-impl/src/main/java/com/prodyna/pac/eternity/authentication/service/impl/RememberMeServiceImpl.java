package com.prodyna.pac.eternity.authentication.service.impl;

import com.prodyna.pac.eternity.authentication.service.RememberMeService;
import com.prodyna.pac.eternity.common.helper.CalendarBuilder;
import com.prodyna.pac.eternity.common.helper.PasswordHashBuilder;
import com.prodyna.pac.eternity.common.helper.QueryMapBuilder;
import com.prodyna.pac.eternity.common.logging.Logging;
import com.prodyna.pac.eternity.common.service.CypherService;
import com.prodyna.pac.eternity.common.model.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.common.model.authentication.RememberMe;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Default implementation for the RememberMeService.
 */
@Logging
@Stateless
public class RememberMeServiceImpl implements RememberMeService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static final String REMEMBER_ME_RETURN_PROPERTIES = "r.id, r.hashedToken, r.createTime";

    @Inject
    private CypherService cypherService;

    @Inject
    private CalendarBuilder calendarBuilder;

    @Inject
    private PasswordHashBuilder passwordHashBuilder;

    @Inject
    private QueryMapBuilder queryMapBuilder;

    @Override
    public RememberMe create(@NotNull final String userIdentifier) {

        String password = UUID.randomUUID().toString();
        String hashedToken = passwordHashBuilder.createHash(password);

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) " +
                        "CREATE (r:RememberMe {id:{2}, hashedToken:{3}, createTime:{4}}) " +
                        "-[:ASSIGNED_TO]->(u) " +
                        "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                queryMapBuilder.map(1, userIdentifier, 2, UUID.randomUUID().toString(),
                        3, hashedToken, 4, calendarBuilder.getNow().getTimeInMillis()));

        if (queryResult == null) {
            throw new NotCreatedRuntimeException();
        }

        RememberMe result = this.getRememberMe(queryResult);
        result.setToken(result.getId() + ":" + password);

        return result;

    }

    @Override
    public RememberMe get(@NotNull final String identifier) {

        RememberMe result = null;

        if (identifier != null) {
            final Map<String, Object> queryResult = cypherService.querySingle(
                    "MATCH (r:RememberMe {id:{1}}) " +
                            "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                    queryMapBuilder.map(1, identifier));

            if (queryResult != null) {
                result = this.getRememberMe(queryResult);
            }
        }

        return result;

    }

    @Override
    public List<RememberMe> getByUser(@NotNull final String userIdentifier) {

        List<RememberMe> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User {identifier:{1}})<-[:ASSIGNED_TO]-(r:RememberMe) " +
                        "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                queryMapBuilder.map(1, userIdentifier));

        for (Map<String, Object> rememberParts : queryResult) {
            result.add(this.getRememberMe(rememberParts));
        }

        return result;

    }

    @Override
    public void delete(@NotNull final String identifier) {

        cypherService.query(
                "MATCH (r:RememberMe {id:{1}})-[a:ASSIGNED_TO]->(:User)" +
                        "DELETE r,a",
                queryMapBuilder.map(1, identifier));

    }

    @Override
    public void deleteByUser(@NotNull final String userIdentifier) {

        cypherService.query(
                "MATCH (r:RememberMe)-[a:ASSIGNED_TO]->(u:User {identifier:{1}})" +
                        "DELETE r,a",
                queryMapBuilder.map(1, userIdentifier));

    }

    /**
     * Helper method to construct a RememberMe from a query response
     *
     * @param values the available values
     * @return a filled RememberMe
     */
    private RememberMe getRememberMe(final Map<String, Object> values) {

        RememberMe result = new RememberMe();

        String readId = (String) values.get("r.id");
        String readHashedToken = (String) values.get("r.hashedToken");
        long readCreateTime = (long) values.get("r.createTime");

        result.setId(readId);
        result.setHashedToken(readHashedToken);
        result.setCreatedTime(calendarBuilder.getCalendar(readCreateTime));

        return result;

    }

}
