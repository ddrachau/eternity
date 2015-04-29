//package com.prodyna.pac.eternity.server.push;
//
//import com.prodyna.pac.eternity.server.push.decoders.ChatMessageDecoder;
//import com.prodyna.pac.eternity.server.push.encoders.ChatMessageEncoder;
//import com.prodyna.pac.eternity.server.push.messages.ChatMessage;
//import com.prodyna.pac.eternity.server.push.messages.ChatMessageType;
//import com.prodyna.pac.eternity.server.push.messages.Message;
//import com.prodyna.pac.eternity.server.service.ProjectService;
//
//import javax.ejb.Schedule;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import javax.inject.Inject;
//import javax.json.Json;
//import javax.json.stream.JsonParser;
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Startup
//@Singleton
//@ServerEndpoint(value = "/chatserver", encoders = {ChatMessageEncoder.class}, decoders = {ChatMessageDecoder.class})
//public class ChatServer {
//
//    private static final Logger logger = Logger.getLogger("ChatServer");
//    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
//    @Inject
//    private ProjectService service;
//
//    private void send(Message msg) {
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
//
//    @OnMessage
//    public void message(final Session session, String msg) {
//        HashMap<String, String> messageMap = new HashMap<>();
//        JsonParser parser = Json.createParser(new StringReader(msg));
//        while (parser.hasNext()) {
//            if (parser.next() == JsonParser.Event.KEY_NAME) {
//                String key = parser.getString();
//                parser.next();
//                String value = parser.getString();
//                messageMap.put(key, value);
//            }
//        }
//
//        String string = messageMap.get("data");
//        send(new ChatMessage(string, ChatMessageType.CHAT));
//    }
//
//    @OnOpen
//    public void openConnection(Session session) throws IOException, EncodeException {
//        logger.log(Level.INFO, "Session ID : " + session.getId() + " - Connection opened");
//        peers.add(session);
//    }
//
//    @OnClose
//    public void closedConnection(Session session) {
//        logger.log(Level.INFO, "Connection closed.");
//        peers.remove(session);
//    }
//
//    @OnError
//    public void error(Session session, Throwable t) {
//        logger.log(Level.SEVERE, t.toString());
//        logger.log(Level.SEVERE, "Connection error.");
//    }
//
//    @Schedule(second = "*/20", minute = "*", hour = "*", persistent = false)
//    public void play() {
//        //logger.log(Level.INFO, "timetolog" + this.toString());
//        //logger.log(Level.INFO, "Find all Projects" + service.findAll().size());
////        send(new ChatMessage("" + System.currentTimeMillis(),
////                ChatMessageType.CHAT));
//    }
//}
