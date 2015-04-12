package com.prodyna.pac.eternity.server.push.decoders;

import com.prodyna.pac.eternity.server.push.messages.ChatMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.Map;

/* Decode a JSON message into a message.
 */
public class ChatMessageDecoder implements Decoder.Text<ChatMessage> {

    /* Stores the name-value pairs from a JSON message as a Map */
    private Map<String, String> messageMap;

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }

    /* Create a new Message object if the message can be decoded */
    @Override
    public ChatMessage decode(String string) throws DecodeException {
//        BetMessage msg = null;
//        if (willDecode(string)) {
//            switch (messageMap.get("type")) {
//                case "betMatchWinner":
//                    msg = new BetMessage(messageMap.get("name"));
//                    break;
//            }
//        } else {
//            throw new DecodeException(string, "[Message] Can't decode.");
//        }
        return null;
    }

    /* Decode a JSON message into a Map and check if it contains
     * all the required fields according to its type. */
    @Override
    public boolean willDecode(String string) {
//        boolean decodes = false;
//        /* Convert the message into a map */
//        messageMap = new HashMap<>();
//        JsonParser parser = Json.createParser(new StringReader(string));
//        while (parser.hasNext()) {
//            if (parser.next() == JsonParser.Event.KEY_NAME) {
//                String key = parser.getString();
//                parser.next();
//                String value = parser.getString();
//                messageMap.put(key, value);
//            }
//        }
//        /* Check the kind of message and if all fields are included */
//        Set keys = messageMap.keySet();
//        if (keys.contains("type")) {
//            switch (messageMap.get("type")) {
//                case "betMatchWinner":
//                    if (keys.contains("name"))
//                        decodes = true;
//                    break;
//            }
//        }
        return false;
    }
}
