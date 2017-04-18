package track.messenger.commands;

import track.messenger.database.User;
import track.messenger.messages.Message;
import track.messenger.messages.RegisterMsg;
import track.messenger.messages.StatusMsg;
import track.messenger.net.MessengerServer;
import track.messenger.net.ProtocolException;
import track.messenger.net.Session;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Oskar on 11.04.17.
 */
public class RegisterCmd extends Command {

    public RegisterCmd(Session session) {
        this.session = session;
    }

    @Override
    public void execute(Message message) throws CommandException {

        RegisterMsg msg = (RegisterMsg) message;
        MessengerServer.log.info("Registering user: " + msg.getLogin());

        //test: /register HYXYH aqwerty Admin

        User user = new User();
        user.setName(msg.getLogin());
        user.setInfo(msg.getInfo());
        user.setLogin(msg.getLogin());
        user.setPassword(msg.getPassword());

        StatusMsg status;

        try {
            user = MessengerServer.userStore.addUser(user);
            status = new StatusMsg(true, "registred user \'" + msg.getLogin() + "\'");
        } catch (SQLException e) {
            e.printStackTrace();
            status = new StatusMsg(false, "Registration error: " + e.getMessage());
        } catch (Exception e) {
            throw new CommandException(e);
        }

        try {
            session.send(status);
        } catch (ProtocolException | IOException e) {
            throw new CommandException(e);
        }
    }
}
