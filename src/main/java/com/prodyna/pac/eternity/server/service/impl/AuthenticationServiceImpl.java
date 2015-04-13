package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.AuthenticationService;

public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public void login(String userIdentifier, String password) {
    }

    @Override
    public void logout() {

    }

    @Override
    public User storePassword(User user, String password) {
        // use password hash
        // return user with the password

        return user;
    }

    @Override
    public void changePassword(String userIdentifier, String oldPassword, String newPassword) {

    }

}
