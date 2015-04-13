package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Authentication service provides service for operating with passwords and logins.
 */
@Local
public interface AuthenticationService {

    // return session? Invalidate potential open session
    void login(String userIdentifier, String password);

    // invalidate only the session in the interceptor context?
    void logout();

    // only admins can set the pw, normal users have to ask admin or use change
    User storePassword(User user, String password);

    void changePassword(String userIdentifier, String oldPassword, String newPassword);

}
