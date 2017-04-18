package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import track.messenger.messages.Message;
import track.messenger.messages.Type;

import java.util.List;

/**
 * Created by Oskar on 13.04.17.
 */
public class InfoResultMsg extends StatusMsg {
    private String info;

    @JsonCreator
    public InfoResultMsg(@JsonProperty("info") String info) {
        super(true, "info");
        this.setType(Type.MSG_INFO_RESULT);
        this.info = info;
    }

    @JsonCreator
    public InfoResultMsg(@JsonProperty("status") Boolean status,
                         @JsonProperty("statusMessage") String statusMessage,
                         @JsonProperty("info") String info) {
        super(status, statusMessage);
        this.setType(Type.MSG_INFO_RESULT);
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "InfoResultMsg{" +
                "info='" + info + '\'' +
                ", Base=" + super.toString() + "}";
    }

}
