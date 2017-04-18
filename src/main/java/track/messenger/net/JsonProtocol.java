package track.messenger.net;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import track.messenger.messages.Message;

import java.io.IOException;

/**
 * Created by Oskar on 11.04.17.
 */

public class JsonProtocol implements Protocol {
    @Override
    public Message decode(byte[] bytes) throws ProtocolException {

        ObjectMapper mapper = new ObjectMapper();
        String json = new String(bytes);

        try {
            return mapper.readValue(json, Message.class);
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {

        ObjectMapper mapper = new ObjectMapper();

        try {
//            String json = mapper.writeValueAsString(msg);
//            return json.getBytes();
            return mapper.writeValueAsBytes(msg);
        } catch (JsonProcessingException e) {
            throw new ProtocolException(e);
        }
    }
}
