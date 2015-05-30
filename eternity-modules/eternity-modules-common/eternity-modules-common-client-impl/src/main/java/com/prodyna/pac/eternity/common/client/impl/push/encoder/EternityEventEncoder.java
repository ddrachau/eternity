package com.prodyna.pac.eternity.common.client.impl.push.encoder;

import com.prodyna.pac.eternity.common.model.event.EternityEvent;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.StringWriter;
import java.util.Map;

/**
 * Encodes an EternityEvent for the client.
 */
public class EternityEventEncoder implements Encoder.Text<EternityEvent> {

    @Override
    public void init(final EndpointConfig ec) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(final EternityEvent event) throws EncodeException {

        StringWriter stringWriter = new StringWriter();

        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {

            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("event", event.getType().getMessageKey());

            if (event.getPayload() != null) {

                for (Map.Entry<String, String> load : event.getPayload().entrySet()) {
                    builder.add(load.getKey(), load.getValue());
                }

            }

            jsonWriter.writeObject(builder.build());

        }

        return stringWriter.toString();

    }

}
