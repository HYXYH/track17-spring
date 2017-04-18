package track.messenger.database;

import com.sun.istack.internal.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UserStore implements track.messenger.store.UserStore {
    static Logger log = LoggerFactory.getLogger(UserStore.class);

    private DbManager dbManager;

    public UserStore() {
    }

    public UserStore(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public void setDbManager(DbManager dbManager) {
        this.dbManager = dbManager;
    }


    public List<User> getAllByQuerry(String query) throws SQLException {

//        String query = "SELECT * FROM users LIMIT " + limit + ";";
        return QueryExecutor.execQuery(dbManager.getConnection(), query, rs -> {
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setInfo(rs.getString("info"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                userList.add(user);
            }
            return userList;
        });
    }


    public List<Long> getChatMembers(Long chatId) throws SQLException {
        String query = "SELECT * FROM chat_members WHERE chat_id='" + chatId + "';";
        return QueryExecutor.execQuery(dbManager.getConnection(), query, rs -> {
            List<Long> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(rs.getLong("user_id"));
            }
            return userList;
        });
    }



    private User getByQuerry(String query) throws SQLException {
        return QueryExecutor.execQuery(dbManager.getConnection(), query, rs -> {
            rs.next();
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setInfo(rs.getString("info"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    @Override
    public User addUser(User user) throws SQLException {

        String updateQuery = "INSERT INTO users (id,name,info,login,password) VALUES (" +
                "NULL" + "," +
                "'" + user.getName() + "'," +
                "'" + user.getInfo() + "'," +
                "'" + user.getLogin() + "'," +
                "'" + user.getPassword() + "'" +
                ");";

        log.debug(updateQuery);
        QueryExecutor.execQuery(dbManager.getConnection(), updateQuery, rs -> {
            return null;
        });

        return getUser(user.getLogin(), user.getPassword());
    }

    @Override
    public User updateUser(User user) throws SQLException {

        String updateQuery = "UPDATE users SET " +
                "name='" + user.getName() + "'," +
                "info='" + user.getInfo() + "'," +
                "login='" + user.getLogin() + "'," +
                "password='" + user.getPassword() + "'" +
                " WHERE id=" + "'" + user.getId() + "'";

        log.debug(updateQuery);
        QueryExecutor.execUpdateQuery(dbManager.getConnection(), updateQuery, rs -> {
            return null;
        });
        return getUserById(user.getId());
    }

    @Override
    public User getUser(String login, String pass) throws SQLException {
        String query = "SELECT * FROM users WHERE login='" + login + "';";

        User user = getByQuerry(query);

        if (user.getPassword().equals(pass)) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserById(Long id) throws SQLException {
        String query = "SELECT * FROM users WHERE id='" + id + "';";
        return getByQuerry(query);
    }


}
