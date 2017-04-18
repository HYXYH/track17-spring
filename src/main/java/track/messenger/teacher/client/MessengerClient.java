package track.messenger.teacher.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.messenger.messages.ChatCreateMsg;
import track.messenger.messages.ChatHistMsg;
import track.messenger.messages.ChatListMsg;
import track.messenger.messages.InfoMsg;
import track.messenger.messages.LoginMsg;
import track.messenger.messages.Message;
import track.messenger.messages.RegisterMsg;
import track.messenger.messages.TextMsg;
import track.messenger.net.JsonProtocol;
import track.messenger.net.Protocol;
import track.messenger.net.ProtocolException;
import track.messenger.net.StringProtocol;


/**
 *
 */
public class MessengerClient {

    /**
     * Механизм логирования позволяет более гибко управлять записью данных в лог (консоль, файл и тд)
     */
    static Logger log;

    /**
     * Протокол, хост и порт инициализируются из конфига
     */
    private Protocol protocol;
    private int port;
    private String host;


    private Thread socketListenerThread;
    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;

    private MessengerClient() {
        System.setProperty("logfilename", "client");
        log = LoggerFactory.getLogger(MessengerClient.class);
    }


    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void initSocket() throws IOException {
        Socket socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();

        /*
      Тред "слушает" сокет на наличие входящих сообщений от сервера
     */
        socketListenerThread = new Thread(() -> {
            final byte[] buf = new byte[1024 * 64];
            log.info("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Здесь поток блокируется на ожидании данных
                    int read = in.read(buf);
                    if (read > 0) {

                        // По сети передается поток байт, его нужно раскодировать с помощью протокола
                        Message msg = protocol.decode(Arrays.copyOf(buf, read));
                        onMessage(msg);
                    }
                } catch (Exception e) {
                    log.error("Failed to process connection: {}", e);
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        socketListenerThread.start();
    }

    /**
     * Реагируем на входящее сообщение
     */
    public void onMessage(Message msg) {
        log.info("Message received: {}", msg);
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException {
        String invalidInputInfo = "Invalid input: " + line + "\n print /help for usage info.";
        String[] tokens = line.split(" ");
        log.debug("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {
            case "/register":
                if (tokens.length < 4) {
                    System.out.println(invalidInputInfo);
                    return;
                }
                StringBuilder info = new StringBuilder();
                for (int i = 3; i < tokens.length; i++) {
                    info.append(" ").append(tokens[i]);
                }
                RegisterMsg regMsg = new RegisterMsg(tokens[1], tokens[2], info.toString());
                send(regMsg);
                break;

            case "/login":
                if (tokens.length < 3) {
                    System.out.println(invalidInputInfo);
                    return;
                }
                LoginMsg loginMsg = new LoginMsg(tokens[1], tokens[2]);
                send(loginMsg);
                break;

            case "/info":
                if (tokens.length == 1) {
                    InfoMsg infoMsg = new InfoMsg(-1L);
                    send(infoMsg);
                } else if (tokens.length == 2) {
                    try {
                        Long id = Long.parseLong(tokens[1]);
                        InfoMsg infoMsg = new InfoMsg(id);
                        send(infoMsg);
                    } catch (NumberFormatException e) {
                        System.out.println(invalidInputInfo);
                    }
                } else {
                    System.out.println(invalidInputInfo);
                    return;
                }
                break;

            case "/chat_list":
                ChatListMsg chatListMsg = new ChatListMsg();
                send(chatListMsg);
                break;

            case "/chat_create":
                if (tokens.length > 1) {
                    List<Long> userIds = new ArrayList<Long>();
                    String[] usersStrs = line.split(",");
                    for (String str : usersStrs) {
                        try {
                            Long id = Long.parseLong(tokens[1]);
                            userIds.add(id);
                        } catch (NumberFormatException e) {
                            System.out.println(invalidInputInfo);
                            return;
                        }
                    }
                    String chatName = "Chat";
                    if (tokens.length > 2) {
                        StringBuilder nameBuilder = new StringBuilder();
                        for (int i = 2; i < tokens.length; i++) {
                            nameBuilder.append(" ").append(tokens[i]);
                        }
                        chatName = nameBuilder.toString();
                    }

                    ChatCreateMsg chatCreateMsg = new ChatCreateMsg(userIds, chatName);
                    send(chatCreateMsg);
                }
                break;

            case "/chat_history":
                if (tokens.length > 1) {
                    try {
                        Long id = Long.parseLong(tokens[1]);
                        ChatHistMsg chatHistMsg = new ChatHistMsg(id);
                        send(chatHistMsg);
                    } catch (NumberFormatException e) {
                        System.out.println(invalidInputInfo);
                        return;
                    }
                } else {
                    System.out.println(invalidInputInfo);
                    return;
                }
                break;

            case "/text":
                if (tokens.length > 2) {
                    try {
                        Long id = Long.parseLong(tokens[1]);
                        StringBuilder textBuilder = new StringBuilder();
                        for (int i = 2; i < tokens.length; i++) {
                            textBuilder.append(" ").append(tokens[i]);
                        }
                        String text = textBuilder.toString();

                        TextMsg sendMessage = new TextMsg(id, text);
                        send(sendMessage);
                    } catch (NumberFormatException e) {
                        System.out.println(invalidInputInfo);
                        return;
                    }
                } else {
                    System.out.println(invalidInputInfo);
                    return;
                }
                break;

            case "/help":
                String helpInfo =
                        "\n\tHelp:" +
                        "\n/register <логин_пользователя> <пароль> <информация о пользователе> - создать пользователя" +
                        "\n/login <логин_пользователя> <пароль>  - залогиниться" +
                        "\n\tтолько для залогиненных пользователей:" +
                        "\n/info [id] - получить всю информацию о пользователе, без аргументов - о себе" +
                        "\n/chat_list - получить список чатов пользователя" +
                        "\n/chat_create <user_id list> - создать новый чат, список пользователей приглашенных в чат" +
                        "\n/chat_history <chat_id> - список сообщений из указанного чата" +
                        "\n/text <id> <message> - отправить сообщение в заданный чат из списка чатов пользователя" +
                        "\nq - выход";
                System.out.print(helpInfo);
                break;

            default:
                log.error(invalidInputInfo);
        }
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    public void send(Message msg) throws IOException, ProtocolException {
        log.info("Sending to Server: " + msg.toString());
        out.write(protocol.encode(msg));
        out.flush(); // принудительно проталкиваем буфер с данными
    }

    public static void main(String[] args) throws Exception {

        MessengerClient client = new MessengerClient();
        client.setHost("localhost");
        client.setPort(19000);
        client.setProtocol(new JsonProtocol());

        try {
            client.initSocket();

            // Цикл чтения с консоли
            Scanner scanner = new Scanner(System.in);
            System.out.println("$");
            while (true) {
                String input = scanner.nextLine();
                if ("q".equals(input)) {
                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException e) {
                    log.error("Failed to process user input", e);
                }
            }
        } catch (Exception e) {
            log.error("Application failed.", e);
        } finally {
            if (client != null) {
                // TODO
                client.socketListenerThread.interrupt();
                log.info("Quit the client");
//                client.close();
            }
        }
    }
}