package com.prodyna.pac.eternity.server.model.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SetPassword {

    @NotNull
    @Size(min = 1, max = 30)
    private String newPassword;

    public SetPassword() {

    }

    public String getNewPassword() {

        return newPassword;

    }

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
