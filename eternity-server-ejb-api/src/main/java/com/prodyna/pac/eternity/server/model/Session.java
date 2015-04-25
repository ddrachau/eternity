package com.prodyna.pac.eternity.server.model;

import java.util.Calendar;

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
     * Empty default constructor *
     */
    public Session() {

    }

    /**
     * Creates a session and initialize the following properties:
     *
     * @param id               the technical identifier
     * @param lastAccessedTime the last accessed time
     * @param createdTime      the creation time
     */
    public Session(String id, Calendar lastAccessedTime, Calendar createdTime) {
        super(id);
        this.lastAccessedTime = lastAccessedTime;
        this.createdTime = createdTime;
    }

    /**
     * Creates a session and initialize the following properties:
     *
     * @param lastAccessedTime the last accessed time
     * @param createdTime      the creation time
     */
    public Session(Calendar lastAccessedTime, Calendar createdTime) {
        super(null);
        this.lastAccessedTime = lastAccessedTime;
        this.createdTime = createdTime;
    }


    /**
     * Basic Getter
     *
     * @return the lastAccessedTime
     */
    public Calendar getLastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     * Basic Setter
     *
     * @param lastAccessedTime to be set
     */
    public void setLastAccessedTime(Calendar lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    /**
     * Basic Getter
     *
     * @return the createdTime
     */
    public Calendar getCreatedTime() {
        return createdTime;
    }

    /**
     * Basic Setter
     *
     * @param createdTime to be set
     */
    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Session session = (Session) o;

        if (lastAccessedTime != null ? !lastAccessedTime.equals(session.lastAccessedTime) : session.lastAccessedTime != null)
            return false;
        return !(createdTime != null ? !createdTime.equals(session.createdTime) : session.createdTime != null);

    }

    @Override
    public String toString() {
        return "Session{" +
                "lastAccessedTime=" + lastAccessedTime +
                ", createdTime=" + createdTime +
                '}';
    }

}
