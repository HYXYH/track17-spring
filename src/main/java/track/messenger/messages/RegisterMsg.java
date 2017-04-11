package track.messenger.messages;

/**
 * Created by Oskar on 11.04.17.
 */
public class RegisterMsg extends Message {

    private String login;
    private String password;
    private String info;


    public RegisterMsg() {
        this.setType(Type.MSG_REGISTER);
    }
}
