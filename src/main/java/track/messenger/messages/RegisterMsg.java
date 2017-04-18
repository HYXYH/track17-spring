package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonCreator
    public RegisterMsg(@JsonProperty("login") String login,
                       @JsonProperty("password") String password,
                       @JsonProperty("statusMessage") String info
    ) {
        this.setType(Type.MSG_REGISTER);
        this.login = login;
        this.password = password;
        this.info = info;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterMsg that = (RegisterMsg) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return info != null ? info.equals(that.info) : that.info == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RegisterMsg{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", statusMessage='" + info + '\'' +
                ", Base=" + super.toString() + "}";
    }
}
