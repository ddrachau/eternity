package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public User create(@NotNull User project) throws ElementAlreadyExistsException {
        return null;
    }

    @Override
    public User get(@NotNull String identifier) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User update(@NotNull User project) throws NoSuchElementException, ElementAlreadyExistsException {
        return null;
    }

    @Override
    public void delete(@NotNull String identifier) throws NoSuchElementException {

    }
}
