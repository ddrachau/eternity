package com.prodyna.pac.eternity.server.service.authentication.impl;

import com.prodyna.pac.eternity.components.common.DateUtils;
import com.prodyna.pac.eternity.components.common.PasswordHash;
import com.prodyna.pac.eternity.server.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.authentication.RememberMe;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.authentication.RememberMeService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.components.common.QueryUtils.map;

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

    @Override
    public RememberMe create(@NotNull String userIdentifier) {

        String password = UUID.randomUUID().toString();
        String hashedToken = PasswordHash.createHash(password);

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) " +
                        "CREATE (r:RememberMe {id:{2}, hashedToken:{3}, createTime:{4}}) " +
                        "-[:ASSIGNED_TO]->(u) " +
                        "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                map(1, userIdentifier, 2, UUID.randomUUID().toString(),
                        3, hashedToken, 4, DateUtils.getNow().getTimeInMillis()));

        if (queryResult == null) {
            throw new NotCreatedRuntimeException();
        }

        RememberMe result = this.getRememberMe(queryResult);
        result.setToken(result.getId() + ":" + password);

        return result;

    }

    @Override
    public RememberMe get(@NotNull String identifier) {

        RememberMe result = null;

        if (identifier != null) {
            final Map<String, Object> queryResult = cypherService.querySingle(
                    "MATCH (r:RememberMe {id:{1}}) " +
                            "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                    map(1, identifier));

            if (queryResult != null) {
                result = this.getRememberMe(queryResult);
            }
        }

        return result;

    }

    @Override
    public List<RememberMe> getByUser(@NotNull String userIdentifier) {

        List<RememberMe> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User {identifier:{1}})<-[:ASSIGNED_TO]-(r:RememberMe) " +
                        "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                map(1, userIdentifier));

        for (Map<String, Object> rememberParts : queryResult) {
            result.add(this.getRememberMe(rememberParts));
        }

        return result;

    }

    @Override
    public void delete(@NotNull String identifier) {

        cypherService.query(
                "MATCH (r:RememberMe {id:{1}})-[a:ASSIGNED_TO]->(:User)" +
                        "DELETE r,a",
                map(1, identifier));

    }

    @Override
    public void deleteByUser(@NotNull String userIdentifier) {

        cypherService.query(
                "MATCH (r:RememberMe)-[a:ASSIGNED_TO]->(u:User {identifier:{1}})" +
                        "DELETE r,a",
                map(1, userIdentifier));

    }

    /**
     * Helper method to construct a RememberMe from a query response
     *
     * @param values the available values
     * @return a filled RememberMe
     */
    private RememberMe getRememberMe(Map<String, Object> values) {

        RememberMe result = new RememberMe();

        String readId = (String) values.get("r.id");
        String readHashedToken = (String) values.get("r.hashedToken");
        long readCreateTime = (long) values.get("r.createTime");

        result.setId(readId);
        result.setHashedToken(readHashedToken);
        result.setCreatedTime(DateUtils.getCalendar(readCreateTime));

        return result;

    }

}
