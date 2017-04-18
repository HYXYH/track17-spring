package track.messenger.commands;

import track.messenger.database.User;
import track.messenger.messages.LoginMsg;
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
public class LoginCmd extends Command {

    public LoginCmd(Session session) {
        this.session = session;
    }

    @Override
    public void execute(Message message) throws CommandException {

        LoginMsg msg = (LoginMsg) message;
        MessengerServer.log.info("Logging in: " + msg.getLogin());

        //test: /login HYXYH aqwerty

        User user;
        StatusMsg status;

        try {
            user = MessengerServer.userStore.getUser(msg.getLogin(), msg.getPassword());

            session.setUser(user);

            status = new StatusMsg(true, "Success login as \'" + msg.getLogin() + "\'");
        } catch (SQLException e) {
            e.printStackTrace();
            status = new StatusMsg(false, "Failed to login: " + e.getMessage());
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

// what server should do with text message? send to all users or just save in db?  //send to all
// message for each command? // yep
// need command register? //yep
// bean for client? // yep, use spring bean
// architecture ok? (nio instead of sockets)
// is select.register thread safe?
// если логин не указан, то авторизоваться <- что это значит? в чём разница между залогиниться и авторизоваться?