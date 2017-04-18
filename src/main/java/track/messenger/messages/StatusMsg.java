package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Oskar on 13.04.17.
 */

public class StatusMsg extends Message {
    private Boolean status;
    private String statusMessage;

    @JsonCreator
    public StatusMsg(@JsonProperty("status") Boolean status, @JsonProperty("statusMessage") String statusMessage) {
        this.setType(Type.MSG_STATUS);
        this.status = status;
        this.statusMessage = statusMessage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusMsg statusMsg = (StatusMsg) o;

        if (status != null ? !status.equals(statusMsg.status) : statusMsg.status != null) return false;
        return statusMessage != null ? statusMessage.equals(statusMsg.statusMessage) : statusMsg.statusMessage == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (statusMessage != null ? statusMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatusMsg{" +
                "status=" + status +
                ", statusMessage='" + statusMessage + '\'' +
                ", Base=" + super.toString() + "}";
    }
}
