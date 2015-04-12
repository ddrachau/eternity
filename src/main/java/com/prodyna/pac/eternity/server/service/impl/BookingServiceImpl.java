package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Booking;
import com.prodyna.pac.eternity.server.service.BookingService;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    @Override
    public Booking create(@NotNull Booking project) throws ElementAlreadyExistsException {
        return null;
    }

    @Override
    public Booking get(@NotNull String identifier) {
        return null;
    }

    @Override
    public List<Booking> findAll() {
        return null;
    }

    @Override
    public Booking update(@NotNull Booking project) throws NoSuchElementException, ElementAlreadyExistsException {
        return null;
    }

    @Override
    public void delete(@NotNull String identifier) throws NoSuchElementException {

    }
}
