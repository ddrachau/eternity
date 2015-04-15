package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.common.logging.Logging;
import com.prodyna.pac.eternity.server.exception.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsRuntimeException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.exception.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.server.model.Booking;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.BookingService;
import com.prodyna.pac.eternity.server.service.CypherService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

@Logging
@Stateless
public class BookingServiceImpl implements BookingService {

    @Inject
    private CypherService cypherService;

    @Override
    public Booking create(@NotNull Booking booking, @NotNull User user, @NotNull Project project) throws NoSuchElementRuntimeException, DuplicateTimeBookingException, UserNotAssignedToProjectException {
        return null;
    }

    @Override
    public Booking get(String id) {
        return null;
    }

    @Override
    public List<Booking> findByUser(@NotNull User user) {
        return null;
    }

    @Override
    public List<Booking> findByProject(@NotNull Project project) {
        return null;
    }

    @Override
    public List<Booking> findByUserAndProject(@NotNull User user, @NotNull Project project) {
        return null;
    }

    @Override
    public Booking update(@NotNull Booking booking) throws NoSuchElementRuntimeException, DuplicateTimeBookingException {
        return null;
    }

    @Override
    public void delete(@NotNull String id) throws NoSuchElementRuntimeException {

    }
}
