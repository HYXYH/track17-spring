package track.messenger.commands;

import track.messenger.database.User;
import track.messenger.messages.Message;
import track.messenger.messages.StatusMsg;
import track.messenger.messages.TextMsg;
import track.messenger.net.MessengerServer;
import track.messenger.net.ProtocolException;
import track.messenger.net.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Oskar on 15.04.17.
 */
public class TextCmd extends Command {

    public TextCmd(Session session) {
        this.session = session;
    }

    @Override
    public void execute(Message message) throws CommandException {
        TextMsg msg = (TextMsg) message;


        //test: /info

        User user = session.getUser();
        StatusMsg resultMsg;

        if (user == null) {
            resultMsg = new StatusMsg(false, "Login first!");
        } else {

            MessengerServer.log.info("Processing <text message> from: " + user.getName());
            msg.setSenderId(user.getId());
            //fixme: timestamp
            msg.setTimestamp(0L);

            try {
                MessengerServer.messageStore.addMessage(msg.getChatId(), msg);
                List<Long> usrs = MessengerServer.userStore.getChatMembers(msg.getChatId());

                for (Session s: MessengerServer.getSessions().values()) {
                    if (usrs.contains(s.getUser().getId())) {
                        s.send(msg);
                    }
                }

                resultMsg = new StatusMsg(true, "message is ok");
            } catch (SQLException e) {
                e.printStackTrace();
                resultMsg = new StatusMsg(false, "Error processing text message: " + e.getMessage());
            } catch (Exception e) {
                throw new CommandException(e);
            }
        }

        try {
            session.send(resultMsg);
        } catch (ProtocolException | IOException e) {
            throw new CommandException(e);
        }
    }
}
