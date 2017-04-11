package track.messenger.messages;

/**
 * Created by Oskar on 11.04.17.
 */
public class ChatHistMsg extends Message {

    private Long chatId;

    public ChatHistMsg() {
        this.setType(Type.MSG_CHAT_HIST);
    }
}
