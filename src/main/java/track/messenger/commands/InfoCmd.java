package track.messenger.commands;

import track.messenger.database.User;
import track.messenger.messages.InfoMsg;
import track.messenger.messages.InfoResultMsg;
import track.messenger.messages.Message;
import track.messenger.messages.StatusMsg;
import track.messenger.net.MessengerServer;
import track.messenger.net.ProtocolException;
import track.messenger.net.Session;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Oskar on 15.04.17.
 */
public class InfoCmd extends Command {

    public InfoCmd(Session session) {
        this.session = session;
    }

    @Override
    public void execute(Message message) throws CommandException {
        InfoMsg msg = (InfoMsg) message;
        MessengerServer.log.info("Searching info about: " + msg.getSearchId());

        //test: /info

        User user = session.getUser();
        StatusMsg infoResultMsg;

        if (user == null) {
            infoResultMsg = new StatusMsg(false, "Login first!");
        } else {
            if (msg.getSearchId() == -1L) {
                infoResultMsg = new InfoResultMsg(true, "Info about \'" + user.getName() + "\'", user.getInfo());
            } else {
                try {
                    user = MessengerServer.userStore.getUserById(msg.getSearchId());
                    infoResultMsg = new InfoResultMsg(true, "Info about \'" + user.getName() + "\'", user.getInfo());
                } catch (SQLException e) {
                    e.printStackTrace();
                    infoResultMsg = new StatusMsg(false, "Info not found: " + e.getMessage());
                } catch (Exception e) {
                    throw new CommandException(e);
                }
            }
        }

        try {
            session.send(infoResultMsg);
        } catch (ProtocolException | IOException e) {
            throw new CommandException(e);
        }
    }
}
