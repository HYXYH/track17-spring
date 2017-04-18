package track.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import track.messenger.database.DbManager;
import track.messenger.database.MessageStore;
import track.messenger.database.UserStore;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */

@Component
public class MessengerServer {
    public static Logger log;

    @Value("${port}")
    private int port;

    @Value("${poolSize}")
    private int poolSize;

    public static UserStore userStore;

    public static MessageStore messageStore;

    private DbManager dbManager;

    private ExecutorService executor;

    private volatile boolean isRunning;

    private static Map<SocketChannel, Session> sessions;

    public MessengerServer() {

    }

    @PostConstruct
    public void init() {
        System.setProperty("logfilename", "server");
        log = LoggerFactory.getLogger(MessengerServer.class);

        log.debug("POST_CONSTRUCT");
        log.debug("Init server on port: " + port);
        log.debug("PoolSize: " + poolSize + "\n");
        sessions = new ConcurrentHashMap<SocketChannel, Session>();
        executor = Executors.newFixedThreadPool(poolSize);

        dbManager = new DbManager();
        dbManager.setUrl("/Users/Oskar/Desktop/Phystech/Technotrack/Java/messenger.sqlite");
        try {
            dbManager.init();
        } catch (Exception e) {
            log.error("Cannot init database manager", e);
            System.exit(1);
        }
        userStore = new UserStore(dbManager);
        messageStore = new MessageStore(dbManager);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public static Map<SocketChannel, Session> getSessions() {
        return sessions;
    }

    public void start() {

        isRunning = true;
        try {
            // create a ServerSocketChannel
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);

            // Get the Socket connected to this channel, and bind it to the listening port
            ServerSocket ss = ssc.socket();
            InetSocketAddress isa = new InetSocketAddress(port);
            ss.bind(isa);

            // Create a new Selector for selecting
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Started, waiting for connection on port " + port);

            while (isRunning) {

                int num = selector.select(100);
                if (num == 0) {
                    continue;
                }


                Set keys = selector.selectedKeys();
                log.debug("New connections: " + keys.size());


                for (Object key : keys) {
                    SelectionKey selectionKey = (SelectionKey) key;

                    if ((selectionKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {

                        Socket socket = ss.accept();
                        log.debug("Got connection from " + socket);

                        SocketChannel sc = socket.getChannel();
                        sc.configureBlocking(false);

                        sc.register(selector, SelectionKey.OP_READ);
                    } else if ((selectionKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        executor.submit(new ConnectionHandler(ss, selector, selectionKey));
                        selectionKey.cancel();
                    }
                }

                keys.clear();

//                /registerCmd me as user
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //IoUtil.closeQuietly(serverSocket);
        }
    }

    public void destroy() throws Exception {
        isRunning = false;
        if (executor != null) {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    class ConnectionHandler extends Thread {

        private ServerSocket serverSocket;
        private Selector selector;
        private SelectionKey key;

        public ConnectionHandler(ServerSocket serverSocket, Selector selector, SelectionKey key) {
            this.serverSocket = serverSocket;
            this.selector = selector;
            this.key = key;
        }

        @Override
        public void run() {
            SocketChannel sc = null;
            try {
                // It's incoming data on a connection, so process it
                sc = (SocketChannel) key.channel();
                Session session = sessions.get(sc);

                if (session == null) {
                    session = new Session(sc);
                    sessions.put(sc, session);
                }

                boolean ok = session.receive();

                // If the connection is dead, then remove it from sessions and selector and close it
                if (ok) {
                    sc.register(selector, SelectionKey.OP_READ);
                }
                if (!ok) {
                    log.info("Closing session for channel " + sc);
                    sessions.remove(sc);
                    sc.close();
                    log.info("Closed " + sc);
                }

            } catch (IOException ie) {
                // On exception, remove this channel from the selector
                log.error("Closing session for channel " + sc, ie);
                sessions.remove(sc);
                try {
                    sc.close();
                    log.info("Closed " + sc);
                } catch (IOException ie2) {
                    log.error("Error closing channel " + sc, ie2);
                }
            }

        }

    }
}
