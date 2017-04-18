package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Oskar on 13.04.17.
 */
public class ChatHistResultMsg extends StatusMsg {
    private List<String> messages;

    @JsonCreator
    public ChatHistResultMsg(@JsonProperty("messages") List<String> messages) {
        super(true,"chat history");
        this.setType(Type.MSG_CHAT_HIST_RESULT);
        this.messages = messages;
    }

    @JsonCreator
    public ChatHistResultMsg(@JsonProperty("status") Boolean status,
                             @JsonProperty("statusMessage") String statusMessage,
                             @JsonProperty("messages") List<String> messages) {
        super(status, statusMessage);
        this.setType(Type.MSG_CHAT_HIST_RESULT);
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatHistResultMsg that = (ChatHistResultMsg) o;

        return messages != null ? messages.equals(that.messages) : that.messages == null;
    }

    @Override
    public int hashCode() {
        return messages != null ? messages.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChatHistResultMsg{" +
                "messages=" + messages +
                ", Base=" + super.toString() + "}";
    }
}
