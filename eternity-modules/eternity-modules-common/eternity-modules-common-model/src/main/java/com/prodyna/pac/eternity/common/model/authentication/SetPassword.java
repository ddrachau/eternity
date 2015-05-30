package com.prodyna.pac.eternity.common.model.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO for setting a password
 */
public class SetPassword {

    /**
     * the new password
     */
    @NotNull
    @Size(min = 1, max = 30)
    private String newPassword;


    /**
     * Default getter
     *
     * @return the new password
     */
    public String getNewPassword() {

        return newPassword;

    }

    /**
     * Default setter
     *
     * @param newPassword to be set
     */
    public void setNewPassword(final String newPassword) {

        this.newPassword = newPassword;

    }

    @Override
    public String toString() {

        return "ChangePassword{" +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

}
