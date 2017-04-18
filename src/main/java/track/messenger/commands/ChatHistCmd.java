package track.messenger.commands;

import track.messenger.database.User;
import track.messenger.messages.*;
import track.messenger.net.MessengerServer;
import track.messenger.net.ProtocolException;
import track.messenger.net.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 15.04.17.
 */
public class ChatHistCmd extends Command {

    public ChatHistCmd(Session session) {
        this.session = session;
    }

    @Override
    public void execute(Message message) throws CommandException {
        ChatHistMsg msg = (ChatHistMsg) message;

        //test: /info

        User user = session.getUser();
        StatusMsg resultMsg;

        if (user == null) {
            resultMsg = new StatusMsg(false, "Login first!");
        } else {
            MessengerServer.log.info("Getting chat history for: " + user.getName());

            try {
                List<Long> messagesIds = MessengerServer.messageStore.getMessagesFromChat(msg.getChatId());
                MessengerServer.log.debug("messagesIds: " + messagesIds.toString());
                List<String> messages = new ArrayList<String>();
                for (Long id: messagesIds) {
                    MessengerServer.log.debug("Searching message with id: " + id.toString());
                    TextMsg msgFromHist = (TextMsg)MessengerServer.messageStore.getMessageById(id);
                    User usrFromHist = MessengerServer.userStore.getUserById(msgFromHist.getSenderId());
                    messages.add("[" + msgFromHist.getTimestamp() + "]" +
                            usrFromHist.getName() + ": " +
                            msgFromHist.getText());
                }
                resultMsg = new ChatHistResultMsg(messages);
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
