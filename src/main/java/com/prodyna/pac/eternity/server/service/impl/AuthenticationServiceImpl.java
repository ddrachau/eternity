package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.CypherService;

import javax.inject.Inject;

public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    private CypherService cypherService;

    @Override
    public void login(String userIdentifier, String password) {

//        Retrieve the user's salt and hash from the database.
//        Prepend the salt to the given password and hash it using the same hash function.
//        Compare the hash of the given password with the hash from the database. If they match, the password is correct. Otherwise, the password is incorrect.

        // build session
    }

    @Override
    public void logout() {
// destroy the session for the user in the context
    }

    @Override
    public User storePassword(User user, String password) {

//        Generate a long random salt using a CSPRNG.
//        Prepend the salt to the password and hash it with a standard cryptographic hash function such as SHA256.
//        Save both the salt and the hash in the user's database record.
        // use password hash
        // return user with the password
        // store the pw

        return user;
    }

    @Override
    public void changePassword(String userIdentifier, String oldPassword, String newPassword) {

    }

}
