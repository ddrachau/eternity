package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    private CypherService cypherService;

    @Override
    public void login(@NotNull User user, @NotNull String plainPassword) {
        // return session? Invalidate potential open session
        //        Retrieve the user's salt and hash from the database.
        //        Prepend the salt to the given password and hash it using the same hash function.
        //        Compare the hash of the given password with the hash from the database. If they match, the password is correct. Otherwise, the password is incorrect.
        // build session
        throw new UnsupportedOperationException();
    }

    @Override
    public void logout() {
        //// destroy the session for the user in the context
        // invalidate only the session in the interceptor context?
        throw new UnsupportedOperationException();
    }

    @Override
    public User storePassword(@NotNull User user, @NotNull String plainPassword) {
        // only admins can set the pw, normal users have to ask admin or use change
        return user;
    }

    @Override
    public User changePassword(@NotNull User user, @NotNull String oldPlainPassword, @NotNull String newPlainPassword) {
        throw new UnsupportedOperationException();
    }

}
