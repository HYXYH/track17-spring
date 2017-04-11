package track.messenger.messages;

/**
 * Created by Oskar on 11.04.17.
 */
public class InfoMsg extends Message {
    Long id;

    public InfoMsg() {
        this.setType(Type.MSG_INFO);
    }
}
