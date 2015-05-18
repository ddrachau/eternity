package com.prodyna.pac.eternity.server.model.authentication;

public class ChangePassword {

    private String userIdentifier;

    private String oldPassword;

    private String newPassword;

    public ChangePassword() {

    }

    public String getUserIdentifier() {

        return userIdentifier;
    }

    public void setUserIdentifier(final String userIdentifier) {

        this.userIdentifier = userIdentifier;

    }

    public String getOldPassword() {

        return oldPassword;

    }

    public void setOldPassword(final String oldPassword) {

        this.oldPassword = oldPassword;

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
                "userIdentifier='" + userIdentifier + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

}
