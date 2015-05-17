package com.prodyna.pac.eternity.client.rest.push;

import com.prodyna.pac.eternity.server.event.BookingEvent;
import com.prodyna.pac.eternity.server.event.ProjectEvent;
import com.prodyna.pac.eternity.server.event.UserEvent;
import org.slf4j.Logger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Startup
@Singleton
@ServerEndpoint(value = "/push")
public class PushServer {

    @Inject
    private Logger logger;

    private Set<Session> sessions = new HashSet<>();

    @OnMessage
    public void message(final Session session, final String msg) throws IOException, EncodeException {

        logger.info("Session ID : " + session.getId() + " - Received message");
        logger.info(msg);

        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()) {
                s.getBasicRemote().sendObject(msg);
                logger.info("Session ID : " + s.getId() + " - Send message");
                logger.info(msg);
            }
        }

    }

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

    public void listenToBookingEvents(@Observes final BookingEvent event) throws IOException {

        String message = "{\"event\":\"booking\",\"data\":\"hi listening websocket server\"}";

        for (Session session : sessions) {
            logger.info("Session ID : " + session.getId() + " - Send message");
            logger.info(message);
            session.getBasicRemote().sendText(message);
        }

    }

    public void listenToProjectEvents(@Observes final ProjectEvent event) throws IOException {

        String message = "{\"event\":\"project\",\"data\":\"hi listening websocket server\"}";

        for (Session session : sessions) {
            logger.info("Session ID : " + session.getId() + " - Send message");
            logger.info(message);
            session.getBasicRemote().sendText(message);
        }

    }

    public void listenToUserEvents(@Observes final UserEvent event) throws IOException {

        String message = "{\"event\":\"user\",\"data\":\"hi listening websocket server\"}";

        for (Session session : sessions) {
            logger.info("Session ID : " + session.getId() + " - Send message");
            logger.info(message);
            session.getBasicRemote().sendText(message);
        }

    }

}
