package com.prodyna.pac.eternity.common.model.user;

import com.prodyna.pac.eternity.common.model.AbstractNode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Represents a person which can login to the system and book times or administer.
 */
public class User extends AbstractNode {

    /**
     * the functional identifier for the user
     */
    @NotNull
    @Size(min = 1, max = 30)
    private String identifier;

    /**
     * the forename of the user
     */
    @NotNull
    @Size(min = 1, max = 30)
    private String forename;

    /**
     * the surname of the user
     */
    @NotNull
    @Size(min = 1, max = 30)
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
    public User(final String id, final String identifier, final String forename, final String surname,
                final String password, final UserRole role) {

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
    public User(final String id, final String identifier, final String forename, final String surname,
                final String password) {

        this(id, identifier, forename, surname, password, UserRole.USER);
    }

    /**
     * Creates a user and initialize the following properties:
     *
     * @param identifier the functional identifier
     * @param forename   the user's forename
     * @param surname    the user's surname
     * @param password   the user's password
     */
    public User(final String identifier, final String forename, final String surname, final String password) {

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
    public void setIdentifier(final String identifier) {

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
    public void setForename(final String forename) {

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
    public void setSurname(final String surname) {

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
    public void setPassword(final String password) {

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
    public void setRole(final UserRole role) {

        this.role = role;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(identifier, user.identifier) &&
                Objects.equals(forename, user.forename) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(password, user.password) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {

        return Objects.hash(identifier, forename, surname, password, role);

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
