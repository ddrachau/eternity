//package com.prodyna.pac.eternity.server.push.encoders;
//
//import com.prodyna.pac.eternity.server.push.messages.ChatMessage;
//
//import javax.json.Json;
//import javax.json.JsonObjectBuilder;
//import javax.json.JsonWriter;
//import javax.websocket.EncodeException;
//import javax.websocket.Encoder;
//import javax.websocket.EndpointConfig;
//import java.io.StringWriter;
//
//public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {
//
//    @Override
//    public void init(EndpointConfig ec) {
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    @Override
//    public String encode(ChatMessage chatMsg) throws EncodeException {
//
//        StringWriter swriter = new StringWriter();
//        try (JsonWriter jsonWrite = Json.createWriter(swriter)) {
//
//            JsonObjectBuilder builder = Json.createObjectBuilder();
//            builder.add("type", chatMsg.getType().toString().toLowerCase())
//                    .add("message", chatMsg.getMessage())
//                    .add("session", chatMsg.getSession().getId());
//            jsonWrite.writeObject(builder.build());
//
//        }
//        return swriter.toString();
//
//    }
//}
