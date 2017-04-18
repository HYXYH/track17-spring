package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Oskar on 11.04.17.
 */
public class InfoMsg extends Message {
    Long searchId;

    public InfoMsg() {
        this.setType(Type.MSG_INFO);
    }

    @JsonCreator
    public InfoMsg(@JsonProperty("searchId") Long searchId) {
        this.setType(Type.MSG_INFO);
        this.searchId = searchId;
    }

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfoMsg infoMsg = (InfoMsg) o;

        return searchId != null ? searchId.equals(infoMsg.searchId) : infoMsg.searchId == null;
    }

    @Override
    public int hashCode() {
        return searchId != null ? searchId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "InfoMsg{" +
                "searchId=" + searchId +
                ", Base=" + super.toString() + "}";
    }
}
