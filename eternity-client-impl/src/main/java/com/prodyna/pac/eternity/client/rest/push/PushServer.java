package com.prodyna.pac.eternity.client.rest.push;


import com.prodyna.pac.eternity.server.event.BookingEvent;
import org.slf4j.Logger;

import javax.ejb.Schedule;
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

//    private void send(Message msg) {
//
//        try {
//            for (Session session : peers) {
//                if (session.isOpen()) {
//                    msg.setSession(session);
//                    session.getBasicRemote().sendObject(msg);
//                    logger.log(Level.INFO, "send " + msg);
//                }
//            }
//        } catch (IOException | EncodeException e) {
//            logger.log(Level.SEVERE, e.toString());
//        }
//    }

    @OnMessage
    public void message(final Session session, String msg) throws IOException, EncodeException {

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
    public void openConnection(Session session) throws IOException, EncodeException {

        logger.info("Session ID : " + session.getId() + " - Connection opened");
        sessions.add(session);

    }

    @OnClose
    public void closedConnection(Session session) {

        logger.info("Session ID : " + session.getId() + "Connection closed.");
        sessions.remove(session);

    }

    @OnError
    public void error(Session session, Throwable t) {

        logger.error(t.toString());
        logger.error("Session ID : " + session.getId() + "Connection error.");

    }

    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void play() throws IOException {

        // TODO add filter for project, bookings and user screens
        // take the user role in consideration, users do not need every update
        // for now, send every message to every client

        String message = "{\"event\":\"pong\",\"data\":\"hi listening websocket server\"}";
        String message2 = "{\"event\":\"project\",\"data\":\"hi listening websocket server\"}";

        for (Session session : sessions) {
            logger.info("Session ID : " + session.getId() + " - Send message");
            logger.info(message);
            session.getBasicRemote().sendText(message);
            logger.info("Session ID : " + session.getId() + " - Send message");
            logger.info(message2);
            session.getBasicRemote().sendText(message2);
        }

    }

    public void listenToBookingEvents(@Observes BookingEvent event) throws IOException {

        String message = "{\"event\":\"booking\",\"data\":\"hi listening websocket server\"}";

        for (Session session : sessions) {
            logger.info("Session ID : " + session.getId() + " - Send message");
            logger.info(message);
            session.getBasicRemote().sendText(message);
        }

    }
}

