package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Oskar on 13.04.17.
 */
public class ChatListResultMsg extends StatusMsg {
    private List<Long> chatIds;

    @JsonCreator
    public ChatListResultMsg(@JsonProperty("chatIds") List<Long> chatIds) {
        super(true, "chat list");
        this.setType(Type.MSG_CHAT_LIST_RESULT);
        this.chatIds = chatIds;
    }

    @JsonCreator
    public ChatListResultMsg(@JsonProperty("status") Boolean status,
                             @JsonProperty("statusMessage") String statusMessage,
                             @JsonProperty("chatIds") List<Long> chatIds) {
        super(status,statusMessage);
        this.setType(Type.MSG_CHAT_LIST_RESULT);
        this.chatIds = chatIds;
    }

    public List<Long> getChatIds() {
        return chatIds;
    }

    public void setChatIds(List<Long> chatIds) {
        this.chatIds = chatIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatListResultMsg that = (ChatListResultMsg) o;

        return chatIds != null ? chatIds.equals(that.chatIds) : that.chatIds == null;
    }

    @Override
    public int hashCode() {
        return chatIds != null ? chatIds.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChatListResultMsg{" +
                "chatIds=" + chatIds +
                ", Base=" + super.toString() + "}";
    }
}
