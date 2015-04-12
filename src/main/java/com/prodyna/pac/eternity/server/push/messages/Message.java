package com.prodyna.pac.eternity.server.push.messages;

import javax.websocket.Session;

public abstract class Message {

    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
