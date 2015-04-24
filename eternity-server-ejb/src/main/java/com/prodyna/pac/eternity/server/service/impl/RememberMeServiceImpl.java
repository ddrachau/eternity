package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.common.PasswordHash;
import com.prodyna.pac.eternity.server.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.RememberMe;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.RememberMeService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.server.common.QueryUtils.map;

/**
 * Default implementation for the RememberMeService.
 */
@Logging
@Stateless
public class RememberMeServiceImpl implements RememberMeService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static final String REMEMBER_ME_RETURN_PROPERTIES = "r.id, r.hashedToken";

    @Inject
    private CypherService cypherService;

    @Override
    public RememberMe create(@NotNull String userIdentifier) {

        this.deleteByUser(userIdentifier);

        String password = UUID.randomUUID().toString();
        String hashedToken = PasswordHash.createHash(password);

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) " +
                        "CREATE (r:RememberMe {id:{2}, hashedToken:{3}}) " +
                        "-[:ASSIGNED_TO]->(u) " +
                        "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                map(1, userIdentifier, 2, UUID.randomUUID().toString(), 3, hashedToken));

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
    public RememberMe getByUser(@NotNull String userIdentifier) {

        RememberMe result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}})<-[:ASSIGNED_TO]-(r:RememberMe) " +
                        "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                map(1, userIdentifier));

        if (queryResult != null) {
            result = this.getRememberMe(queryResult);
        }

        return result;

    }

    @Override
    public void deleteByUser(@NotNull String userIdentifier) {

        // Invalidate potential open tokens
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

        result.setId(readId);
        result.setHashedToken(readHashedToken);

        return result;

    }

}
