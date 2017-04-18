package track.messenger.commands;

import track.messenger.database.User;
import track.messenger.messages.ChatCreateMsg;
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
public class ChatCreateCmd extends Command {

    public ChatCreateCmd(Session session) {
        this.session = session;
    }

    @Override
    public void execute(Message message) throws CommandException {
        ChatCreateMsg msg = (ChatCreateMsg) message;

        //test: /chat_create <user_id list>

        User user = session.getUser();
        StatusMsg resultMsg;

        if (user == null) {
            resultMsg = new StatusMsg(false, "Login first!");
        } else {
            MessengerServer.log.info("Creating chat for: " + user.getName());

            try {
                List<Long> usrs = msg.getUserIds();
                usrs.add(user.getId());

                if (usrs.size() < 2) {
                    throw new CommandException("chats with 1 member unsupported");
                }

//                if exist return founded. else create.
                if (usrs.size() == 2) {
                    //todo: implement. Как найти такой чат? запрос на чат у которого только 2 конкретных юзера и не больше?
                }

                List<Long> chats = MessengerServer.messageStore.getChatsByUserId(user.getId());

                //fixme: OUTPUT doesnot work in sql. Как вернуть результат из INSERT?
                Long chatId = MessengerServer.messageStore.addChat(msg.getChatName());


                //fixme: if fail here, we will have partly created chat
                for (Long usrId: usrs) {
                    MessengerServer.messageStore.addUserToChat(chatId, usrId);
                }

                resultMsg = new StatusMsg(true, "chat created: " + chatId.toString());
            } catch (CommandException | SQLException e) {
                e.printStackTrace();
                resultMsg = new StatusMsg(false, "Failed creating chat: " + e.getMessage());
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
