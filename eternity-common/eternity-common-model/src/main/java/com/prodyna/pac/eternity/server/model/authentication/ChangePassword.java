package com.prodyna.pac.eternity.server.model.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO for changing a password
 */
public class ChangePassword {


    /**
     * the old password for the user
     */
    @NotNull
    @Size(min = 1, max = 30)
    private String oldPassword;

    /**
     * the new password for the user
     */
    @NotNull
    @Size(min = 1, max = 30)
    private String newPassword;


    /**
     * Default getter
     *
     * @return the old password
     */
    public String getOldPassword() {

        return this.oldPassword;

    }

    /**
     * Default setter
     *
     * @param oldPassword to be set
     */
    public void setOldPassword(final String oldPassword) {

        this.oldPassword = oldPassword;

    }

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
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

}
