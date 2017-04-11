package track.messenger.messages;

import java.util.List;

/**
 * Created by Oskar on 11.04.17.
 */
public class ChatCreateMsg extends Message {
    List<Long> userIds;

    public ChatCreateMsg() {
        this.setType(Type.MSG_CHAT_CREATE);
    }
}
