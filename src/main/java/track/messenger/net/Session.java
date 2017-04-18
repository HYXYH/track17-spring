package track.messenger.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import track.messenger.commands.ChatCreateCmd;
import track.messenger.commands.ChatHistCmd;
import track.messenger.commands.ChatListCmd;
import track.messenger.commands.Command;
import track.messenger.commands.CommandException;
import track.messenger.commands.InfoCmd;
import track.messenger.commands.LoginCmd;
import track.messenger.commands.RegisterCmd;
import track.messenger.commands.TextCmd;
import track.messenger.database.User;
import track.messenger.messages.Message;
import track.messenger.messages.StatusMsg;

/**
 * Сессия связывает бизнес-логику и сетевую часть.
 * Бизнес логика представлена объектом юзера - владельца сессии.
 * Сетевая часть привязывает нас к определнному соединению по сети (от клиента)
 */
public class Session {

    private final ByteBuffer recvBuffer = ByteBuffer.allocate(16384);
    private final ByteBuffer sendBuffer = ByteBuffer.allocate(16384);
    private Protocol protocol;

    RegisterCmd registerCmd;
    LoginCmd loginCmd;
    InfoCmd infoCmd;
    ChatListCmd chatListCmd;
    ChatCreateCmd chatCreateCmd;
    ChatHistCmd chatHistCmd;
    TextCmd textCmd;

    /**
     * Пользователь сессии, пока не прошел логин, user == null
     * После логина устанавливается реальный пользователь
     */
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // сокет на клиента
    private SocketChannel socketChannel;


    public Session(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        protocol = new JsonProtocol();

        registerCmd = new RegisterCmd(this);
        loginCmd = new LoginCmd(this);
        infoCmd = new InfoCmd(this);
        chatListCmd = new ChatListCmd(this);
        chatCreateCmd = new ChatCreateCmd(this);
        chatHistCmd = new ChatHistCmd(this);
        textCmd = new TextCmd(this);
    }

    public void send(Message msg) throws ProtocolException, IOException {
        sendBuffer.clear();
        sendBuffer.put(protocol.encode(msg));
        sendBuffer.flip();
        MessengerServer.log.debug("Sending " + sendBuffer.limit() + " bytes: " +
                new String(Arrays.copyOf(sendBuffer.array(), sendBuffer.limit())));
        socketChannel.write(sendBuffer);
    }

    public boolean receive() throws IOException {
        recvBuffer.clear();
        socketChannel.read(recvBuffer);

        // If no data, close the connection
        if (recvBuffer.limit() == 0) {
            return false;
        }

        MessengerServer.log.debug("Recieved " + recvBuffer.position() + " bytes from " +
                socketChannel.getRemoteAddress() + ": " +
                new String(Arrays.copyOf(recvBuffer.array(), recvBuffer.position())));

        try {
            onMessage(protocol.decode(recvBuffer.array()));
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void onMessage(Message msg) {
        // TODO: Пришло некое сообщение от клиента, его нужно обработать

        Command command = null;

        switch (msg.getType()) {
            case MSG_REGISTER:
                command = registerCmd;
                break;
            case MSG_LOGIN:
                command = loginCmd;
                break;
            case MSG_TEXT:
                command = textCmd;
                break;
            case MSG_INFO:
                command = infoCmd;
                break;
            case MSG_CHAT_LIST:
                command = chatListCmd;
                break;
            case MSG_CHAT_CREATE:
                command = chatCreateCmd;
                break;
            case MSG_CHAT_HIST:
                command = chatHistCmd;
                break;

            default:
                break;
        }

        try {
            if (command != null) {
                command.execute(msg);
            } else {
                MessengerServer.log.error("Unknown command");
                StatusMsg result = new StatusMsg(false, "Unknown command");
                send(result);
            }
        } catch (CommandException e) {
            MessengerServer.log.error("Can not execute command", e);
        } catch (Exception e) {
            MessengerServer.log.error("Fatal command error: ", e);
        }
    }

    public void close() {
        // TODO: закрыть in/out каналы и сокет. Освободить другие ресурсы, если необходимо
    }
}