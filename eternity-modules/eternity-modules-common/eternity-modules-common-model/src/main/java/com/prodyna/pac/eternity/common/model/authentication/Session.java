package com.prodyna.pac.eternity.common.model.authentication;

import com.prodyna.pac.eternity.common.model.AbstractNode;

import java.util.Calendar;
import java.util.Objects;

/**
 * Represents a technical session for the client server communication.
 */
public class Session extends AbstractNode {

    /**
     * The last accessed time.
     */
    private Calendar lastAccessedTime;

    /**
     * The initial creation time.
     */
    private Calendar createdTime;

    /**
     * Basic Getter
     *
     * @return the lastAccessedTime
     */
    public Calendar getLastAccessedTime() {

        return this.lastAccessedTime;

    }

    /**
     * Basic Setter
     *
     * @param lastAccessedTime to be set
     */
    public void setLastAccessedTime(final Calendar lastAccessedTime) {

        this.lastAccessedTime = lastAccessedTime;

    }

    /**
     * Basic Getter
     *
     * @return the createdTime
     */
    public Calendar getCreatedTime() {

        return this.createdTime;

    }

    /**
     * Basic Setter
     *
     * @param createdTime to be set
     */
    public void setCreatedTime(final Calendar createdTime) {

        this.createdTime = createdTime;

    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(lastAccessedTime, session.lastAccessedTime) &&
                Objects.equals(createdTime, session.createdTime);

    }

    @Override
    public int hashCode() {

        return Objects.hash(lastAccessedTime, createdTime);

    }

    @Override
    public String toString() {

        return "Session{" +
                "lastAccessedTime=" + lastAccessedTime +
                ", createdTime=" + createdTime +
                '}';

    }

}
