package com.prodyna.pac.eternity.common.client.impl.push;

import com.prodyna.pac.eternity.common.client.impl.push.encoder.EternityEventEncoder;
import com.prodyna.pac.eternity.common.model.event.EternityEvent;
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

/**
 * The push implementation updating listening clients
 */
@Startup
@Singleton
@ServerEndpoint(value = "/push", encoders = {EternityEventEncoder.class})
public class PushServer {

    @Inject
    private Logger logger;

    /**
     * Store the currently open sessions for notification
     */
    private Set<Session> sessions = new HashSet<>();

    /**
     * Remember the  session if a new one opens
     *
     * @param session the new session
     */
    @OnOpen
    public void openConnection(final Session session) {

        logger.info("Session ID : " + session.getId() + " - Connection opened");
        sessions.add(session);

    }

    /**
     * Forget the session if the session closes
     *
     * @param session the session which was closed
     */
    @OnClose
    public void closedConnection(final Session session) {

        logger.info("Session ID : " + session.getId() + "Connection closed.");
        sessions.remove(session);

    }

    /**
     * Debug call if a session has an error
     *
     * @param session the session with the problem
     * @param t       the exception
     */
    @OnError
    public void error(final Session session, final Throwable t) {

        logger.error("Session ID : " + session.getId() + "Connection error: " + t.toString());

    }

    /**
     * Listen to EternityEvents and notifiy the clients.
     *
     * @param event the event which occurred
     * @throws IOException     if a session has io problems
     * @throws EncodeException if the event cannot be encoded
     */
    public void listenToEternityEvents(@Observes final EternityEvent event) throws IOException, EncodeException {

        for (Session session : sessions) {
            logger.info("Session ID : " + session.getId() + " - Send event: " + event.toString());
            session.getBasicRemote().sendObject(event);
        }

    }

}
