package track.messenger.commands;

import track.messenger.database.User;
import track.messenger.messages.ChatListMsg;
import track.messenger.messages.ChatListResultMsg;
import track.messenger.messages.Message;
import track.messenger.messages.StatusMsg;
import track.messenger.net.MessengerServer;
import track.messenger.net.ProtocolException;
import track.messenger.net.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Oskar on 15.04.17.
 */
public class ChatListCmd extends Command {

    public ChatListCmd(Session session) {
        this.session = session;
    }

    @Override
    public void execute(Message message) throws CommandException {
        ChatListMsg msg = (ChatListMsg) message;

        //test: /info

        User user = session.getUser();
        StatusMsg resultMsg;

        if (user == null) {
            resultMsg = new StatusMsg(false, "Login first!");
        } else {
            MessengerServer.log.info("Getting chats for: " + user.getName());

            try {
                List<Long> chats = MessengerServer.messageStore.getChatsByUserId(user.getId());
                resultMsg = new ChatListResultMsg(chats);
            } catch (SQLException e) {
                e.printStackTrace();
                resultMsg = new StatusMsg(false, "Info not found: " + e.getMessage());
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
