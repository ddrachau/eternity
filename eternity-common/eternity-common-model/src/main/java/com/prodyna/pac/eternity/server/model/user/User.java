package com.prodyna.pac.eternity.server.model.user;

import com.prodyna.pac.eternity.server.model.AbstractNode;

/**
 * Represents a person which can login to the system and book times or administer.
 */
public class User extends AbstractNode {

    /**
     * the functional identifier for the user
     */
    private String identifier;
    /**
     * the forename of the user
     */
    private String forename;
    /**
     * the surname of the user
     */
    private String surname;
    /**
     * the password of the user
     */
    private String password;
    /**
     * the role this user has in the system
     */
    private UserRole role = UserRole.USER;

    /**
     * Empty default constructor *
     */
    public User() {

    }

    /**
     * Creates a user and initialize the following properties:
     *
     * @param id         the technical identifier
     * @param identifier the functional identifier
     * @param forename   the user's forename
     * @param surname    the user's surname
     * @param password   the user's password
     * @param role       the user's role
     */
    public User(String id, String identifier, String forename, String surname, String password, UserRole role) {
        super(id);
        this.identifier = identifier;
        this.forename = forename;
        this.surname = surname;
        this.password = password;
        this.role = role;
    }

    /**
     * Creates a user and initialize the following properties:
     *
     * @param id         the technical identifier
     * @param identifier the functional identifier
     * @param forename   the user's forename
     * @param surname    the user's surname
     * @param password   the user's password
     */
    public User(String id, String identifier, String forename, String surname, String password) {
        this(null, identifier, forename, surname, password,UserRole.USER);
    }

    /**
     * Creates a user and initialize the following properties:
     *
     * @param identifier the functional identifier
     * @param forename   the user's forename
     * @param surname    the user's surname
     * @param password   the user's password
     */
    public User(String identifier, String forename, String surname, String password) {
        this(null, identifier, forename, surname, password);
    }

    /**
     * Basic Getter
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Basic Setter
     *
     * @param identifier to be set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Basic Getter
     *
     * @return the forename
     */
    public String getForename() {
        return forename;
    }

    /**
     * Basic Setter
     *
     * @param forename to be set
     */
    public void setForename(String forename) {
        this.forename = forename;
    }

    /**
     * Basic Getter
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Basic Setter
     *
     * @param surname to be set
     */
    public void setSurname(String surname) {
        this.surname = surname;
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
     * @return the role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Basic Setter
     *
     * @param role to be set
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (identifier != null ? !identifier.equals(user.identifier) : user.identifier != null) return false;
        if (forename != null ? !forename.equals(user.forename) : user.forename != null) return false;
        if (surname != null ? !surname.equals(user.surname) : user.surname != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;

        return role == user.role;

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "identifier='" + identifier + '\'' +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

}