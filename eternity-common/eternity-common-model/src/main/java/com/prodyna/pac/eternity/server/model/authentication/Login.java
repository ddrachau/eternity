package com.prodyna.pac.eternity.server.model.authentication;

import com.prodyna.pac.eternity.server.model.user.User;

/**
 * Wrapper class for logging user in.
 */
public class Login {

    /**
     * The user identifier.
     */
    private String username;

    /**
     * The user password.
     */
    private String password;

    /**
     * Should the login create a rememberMe?
     */
    private boolean remember;

    /**
     * The session token.
     */
    private String xsrfToken;

    /**
     * The optional rememberMeToken.
     */
    private String rememberMeToken;

    /**
     * The matching user.
     */
    private User user;

    /**
     * Empty default constructor.
     */
    public Login() {

    }

    /**
     * Creates a login and initialize the following properties:
     *
     * @param username the user identifier
     * @param password the login password
     * @param remember should the login generate a rememberMe?
     */
    public Login(String username, String password, boolean remember) {

        this.username = username;
        this.password = password;
        this.remember = remember;
    }

    /**
     * Creates a login and initialize the following properties:
     *
     * @param username the user identifier
     * @param password the login password
     */
    public Login(String username, String password) {

        this(username, password, false);
    }

    /**
     * Basic Getter
     *
     * @return the username
     */
    public String getUsername() {

        return username;
    }

    /**
     * Basic Setter
     *
     * @param username to be set
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Basic Getter
     *
     * @return the password
     */
    public String getPassword() {

        return password;
    }

    /**
     * Basic Setter
     *
     * @param password to be set
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * Basic Getter
     *
     * @return if remember
     */
    public boolean isRemember() {

        return remember;
    }

    /**
     * Basic Setter
     *
     * @param remember to be set
     */
    public void setRemember(boolean remember) {

        this.remember = remember;
    }

    /**
     * Basic Getter
     *
     * @return the rememberMeToken
     */
    public String getRememberMeToken() {

        return rememberMeToken;
    }

    /**
     * Basic Setter
     *
     * @param rememberMeToken to be set
     */
    public void setRememberMeToken(String rememberMeToken) {

        this.rememberMeToken = rememberMeToken;
    }

    /**
     * Basic Getter
     *
     * @return the xsrfToken
     */
    public String getXsrfToken() {

        return xsrfToken;
    }

    /**
     * Basic Setter
     *
     * @param xsrfToken to be set
     */
    public void setXsrfToken(String xsrfToken) {

        this.xsrfToken = xsrfToken;
    }

    /**
     * Basic Getter
     *
     * @return the user
     */
    public User getUser() {

        return user;
    }

    /**
     * Basic Setter
     *
     * @param user to be set
     */
    public void setUser(User user) {

        this.user = user;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Login login = (Login) o;

        if (remember != login.remember) return false;
        if (username != null ? !username.equals(login.username) : login.username != null) return false;
        if (password != null ? !password.equals(login.password) : login.password != null) return false;
        if (xsrfToken != null ? !xsrfToken.equals(login.xsrfToken) : login.xsrfToken != null) return false;
        if (rememberMeToken != null ? !rememberMeToken.equals(login.rememberMeToken) : login.rememberMeToken != null)
            return false;
        return !(user != null ? !user.equals(login.user) : login.user != null);

    }

    @Override
    public int hashCode() {

        return super.hashCode();
    }

    @Override
    public String toString() {

        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", remember=" + remember +
                ", xsrfToken='" + xsrfToken + '\'' +
                ", rememberMeToken='" + rememberMeToken + '\'' +
                ", user=" + user +
                '}';
    }

}
