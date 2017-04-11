package track.messenger.messages;

/**
 * Created by Oskar on 11.04.17.
 */
public class LoginMsg extends Message {

    private String login;
    private String password;

    public LoginMsg() {
        this.setType(Type.MSG_LOGIN);
    }
}
