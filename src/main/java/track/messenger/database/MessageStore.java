package track.messenger.database;

import track.messenger.messages.Message;
import track.messenger.messages.TextMsg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 14.04.17.
 */
public class MessageStore implements track.messenger.store.MessageStore {

    private DbManager dbManager;

    public MessageStore(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public void setDbManager(DbManager dbManager) {
        this.dbManager = dbManager;
    }


    @Override
    public List<Long> getChatsByUserId(Long userId) throws SQLException {
        String query = "SELECT * FROM chat_members WHERE user_id='" + userId + "';";
        return QueryExecutor.execQuery(dbManager.getConnection(), query, rs -> {
            List<Long> chatList = new ArrayList<>();
            while (rs.next()) {
                chatList.add(rs.getLong("chat_id"));
            }
            return chatList;
        });
    }

    @Override
    public List<Long> getMessagesFromChat(Long chatId) throws SQLException {
        String query = "SELECT * FROM messages WHERE chat_id='" + chatId + "';";
        return QueryExecutor.execQuery(dbManager.getConnection(), query, rs -> {
            List<Long> messagesList = new ArrayList<>();
            while (rs.next()) {
                messagesList.add(rs.getLong("id"));
            }
            return messagesList;
        });
    }

    @Override
    public Message getMessageById(Long messageId) throws SQLException {
        String query = "SELECT * FROM messages WHERE id='" + messageId + "';";
        return QueryExecutor.execQuery(dbManager.getConnection(), query, rs -> {
            rs.next();
            TextMsg textMsg = new TextMsg();
            textMsg.setId(rs.getLong("id"));
            textMsg.setSenderId(rs.getLong("owner_id"));
            textMsg.setText(rs.getString("text"));
            textMsg.setTimestamp(rs.getLong("timestamp"));

            return textMsg;
        });
    }

    @Override
    public void addMessage(Long chatId, Message message) throws SQLException {
        String addQuery = "INSERT INTO messages (owner_id,chat_id,text,timestamp) VALUES (" +
                "'" + message.getSenderId() + "'," +
                "'" + ((TextMsg)message).getChatId() + "'," +
                "'" + ((TextMsg)message).getText() + "'," +
                "'" + ((TextMsg)message).getTimestamp() + "'" +
                ");";

        QueryExecutor.execQuery(dbManager.getConnection(), addQuery, rs -> {
            return null;
        });
    }

    @Override
    public void addUserToChat(Long userId, Long chatId) throws SQLException {
        String addQuery = "INSERT INTO chat_members (chat_id,user_id) VALUES (" +
                "'" + chatId + "'," +
                "'" + userId + "'" +
                ");";
        QueryExecutor.execQuery(dbManager.getConnection(), addQuery, rs -> {
            return null;
        });
    }

    public Long addChat(String name) throws SQLException {
        String addQuery = "INSERT INTO chats (name) VALUES (" +
                "'" + name + "'" +
                ") OUTPUT chats.id;";
        return QueryExecutor.execQuery(dbManager.getConnection(), addQuery, rs -> {
            rs.next();
            return rs.getLong("id");
        });
    }
}
