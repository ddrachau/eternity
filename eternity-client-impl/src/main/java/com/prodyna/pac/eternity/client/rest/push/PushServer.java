package com.prodyna.pac.eternity.client.rest.push;

import com.prodyna.pac.eternity.client.rest.push.encoder.EternityEventEncoder;
import com.prodyna.pac.eternity.server.event.EternityEvent;
import org.slf4j.Logger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Startup
@Singleton
@ServerEndpoint(value = "/push", encoders = {EternityEventEncoder.class})
public class PushServer {

    @Inject
    private Logger logger;

    private Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void openConnection(final Session session) throws IOException, EncodeException {

        logger.info("Session ID : " + session.getId() + " - Connection opened");
        sessions.add(session);

    }

    @OnClose
    public void closedConnection(final Session session) {

        logger.info("Session ID : " + session.getId() + "Connection closed.");
        sessions.remove(session);

    }

    @OnError
    public void error(final Session session, final Throwable t) {

        logger.error("Session ID : " + session.getId() + "Connection error: " + t.toString());

    }

    public void listenToEternityEvents(@Observes final EternityEvent event) throws IOException, EncodeException {

        for (Session session : sessions) {
            logger.info("Session ID : " + session.getId() + " - Send event: " + event.toString());
            session.getBasicRemote().sendObject(event);
        }

    }

}
