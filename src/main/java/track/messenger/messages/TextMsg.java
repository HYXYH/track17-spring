package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class TextMsg extends Message {

    private Long chatId;
    private Long timestamp;
    private String text;

    public TextMsg() {
        this.setType(Type.MSG_TEXT);
    }

    @JsonCreator
    public TextMsg(@JsonProperty("chatId") Long chatId,
                   @JsonProperty("text") String text) {
        this.setType(Type.MSG_TEXT);
        this.chatId = chatId;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        TextMsg message = (TextMsg) other;
        return Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text);
    }

    @Override
    public String toString() {
        return "TextMsg{" +
                "chatId=" + chatId +
                ", timestamp=" + timestamp +
                ", text='" + text + '\'' +
                ", Base=" + super.toString() + "}";
    }
}