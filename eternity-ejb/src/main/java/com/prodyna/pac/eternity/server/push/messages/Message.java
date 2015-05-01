package com.prodyna.pac.eternity.server.push.messages;

import javax.websocket.Session;

public abstract class Message {

    private Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
