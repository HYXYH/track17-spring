package track.messenger.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import track.messenger.messages.Message;

/**
 * Created by Oskar on 11.04.17.
 */

public class JsonProtocol implements Protocol {
    @Override
    public Message decode(byte[] bytes) throws ProtocolException {

        ObjectMapper mapper = new ObjectMapper();
        String json = new String(bytes);


//        Message msg = mapper.readValue(json, Message.class);

//        msg.getType().

//        return msg;
        return null;
    }

    @Override
    public byte[] encode(Message msg) throws ProtocolException {



        return new byte[0];
    }
}
