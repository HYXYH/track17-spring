package track.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */

@Component
public class MessengerServer {
    static Logger log = LoggerFactory.getLogger(MessengerServer.class);

    @Value("${port}")
    private int port;

    @Value("${poolSize}")
    private int poolSize;

    private ExecutorService executor;

    private volatile boolean isRunning;

    private Map<SocketChannel, Session> sessions;

    public MessengerServer() {

    }

    @PostConstruct
    public void init() {
        System.out.println("POST_CONSTRUCT");
        System.out.println("Init server on port: " + port);
        System.out.printf("PoolSize: " + poolSize + "\n");
        sessions = new HashMap<SocketChannel, Session>();
        executor = Executors.newFixedThreadPool(poolSize);
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

                int num = selector.select();
                if (num == 0) {
                    continue;
                }

                Set keys = selector.selectedKeys();

                for (Object key : keys) {
                    executor.submit(new ConnectionHandler(ss, selector, (SelectionKey) key));
                }

                keys.clear();
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
            try {
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {

                    Socket socket = serverSocket.accept();
                    System.out.println("Got connection from " + socket);

                    SocketChannel sc = socket.getChannel();
                    sc.configureBlocking(false);

                    //fixme: must be thread safe
                    // Register it with the selector, for reading
                    sc.register(selector, SelectionKey.OP_READ);
                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {

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
                        if (!ok) {
                            sessions.remove(sc);

                            key.cancel();
                            Socket socket = null;
                            try {
                                socket = sc.socket();
                                socket.close();
                            } catch (IOException ie) {
                                System.err.println("Error closing serverSocket " + socket + ": " + ie);
                            }
                        }

                    } catch (IOException ie) {
                        // On exception, remove this channel from the selector
                        sessions.remove(sc);
                        key.cancel();
                        try {
                            sc.close();
                        } catch (IOException ie2) {
                            ie2.printStackTrace();
                        }
                        System.out.println("Closed " + sc);
                    }
                }
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
