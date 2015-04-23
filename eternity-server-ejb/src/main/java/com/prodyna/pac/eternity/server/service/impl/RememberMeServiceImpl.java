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

@Logging
@Stateless
public class RememberMeServiceImpl implements RememberMeService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static String REMEMBER_ME_RETURN_PROPERTIES = "r.id, r.token";

    @Inject
    private CypherService cypherService;

    @Override
    public RememberMe create(@NotNull String userIdentifier) {

        this.deleteByUser(userIdentifier);

        String token = UUID.randomUUID().toString();
        String password = PasswordHash.createHash(token);

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) " +
                        "CREATE (r:RememberMe {id:{2}, password:{3}}) " +
                        "-[:ASSIGNED_TO]->(u) " +
                        "RETURN " + REMEMBER_ME_RETURN_PROPERTIES,
                map(1, userIdentifier, 2, UUID.randomUUID().toString(), 3, password));

        if (queryResult == null) {
            throw new NotCreatedRuntimeException();
        }

        RememberMe result = new RememberMe();

        String readId = (String) queryResult.get("r.id");
        result.setId(readId);
        result.setToken(token);

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

}
