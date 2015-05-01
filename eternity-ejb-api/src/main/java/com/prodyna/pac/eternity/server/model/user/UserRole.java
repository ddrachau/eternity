package com.prodyna.pac.eternity.server.model.user;

/**
 * Defines the roles a user can take in the system.
 */
public enum UserRole {

    /**
     * Default role, can manage his bookings
     */
    USER,
    /**
     * Same as user, but can assign users to projects and see other bookings
     */
    MANAGER,
    /**
     * Has no restrictions.
     */
    ADMINISTRATOR

}
