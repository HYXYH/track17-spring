package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Oskar on 11.04.17.
 */
public class ChatCreateMsg extends Message {
    List<Long> userIds;
    String chatName;

    public ChatCreateMsg() {
        this.setType(Type.MSG_CHAT_CREATE);
    }

    @JsonCreator
    public ChatCreateMsg(@JsonProperty("userIds") List<Long> userIds,
                         @JsonProperty("chatName") String chatName) {
        this.setType(Type.MSG_CHAT_CREATE);
        this.userIds = userIds;
        this.chatName = chatName;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }


    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatCreateMsg that = (ChatCreateMsg) o;

        if (userIds != null ? !userIds.equals(that.userIds) : that.userIds != null) return false;
        return chatName != null ? chatName.equals(that.chatName) : that.chatName == null;
    }

    @Override
    public int hashCode() {
        int result = userIds != null ? userIds.hashCode() : 0;
        result = 31 * result + (chatName != null ? chatName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChatCreateMsg{" +
                "userIds=" + userIds +
                ", chatName='" + chatName + '\'' +
                ", Base=" + super.toString() + "}";
    }
}
