package track.messenger.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Oskar on 11.04.17.
 */
public class LoginMsg extends Message {

    private String login;
    private String password;

    public LoginMsg() {
        this.setType(Type.MSG_LOGIN);
    }

    @JsonCreator
    public LoginMsg(@JsonProperty("login") String login,
                    @JsonProperty("password") String password
    ) {
        this.setType(Type.MSG_LOGIN);
        this.login = login;
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginMsg loginMsg = (LoginMsg) o;

        if (login != null ? !login.equals(loginMsg.login) : loginMsg.login != null) return false;
        return password != null ? password.equals(loginMsg.password) : loginMsg.password == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LoginMsg{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", Base=" + super.toString() + "}";
    }
}
