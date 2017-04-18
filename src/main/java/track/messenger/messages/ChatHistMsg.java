package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Oskar on 11.04.17.
 */
public class ChatHistMsg extends Message {

    private Long chatId;

    public ChatHistMsg() {
        this.setType(Type.MSG_CHAT_HIST);
    }

    @JsonCreator
    public ChatHistMsg(@JsonProperty("chatId") Long chatId) {
        this.setType(Type.MSG_CHAT_HIST);
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatHistMsg that = (ChatHistMsg) o;

        return chatId != null ? chatId.equals(that.chatId) : that.chatId == null;
    }

    @Override
    public int hashCode() {
        return chatId != null ? chatId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChatHistMsg{" +
                "chatId=" + chatId +
                ", Base=" + super.toString() + "}";
    }
}
