package track.messenger.messages;

/**
 * Created by Oskar on 11.04.17.
 */
public class ChatListMsg extends Message {

    public ChatListMsg() {
        this.setType(Type.MSG_CHAT_LIST);
    }

    @Override
    public String toString() {
        return "ChatListMsg{} " + ", Base=" + super.toString() + "}";
    }
}
