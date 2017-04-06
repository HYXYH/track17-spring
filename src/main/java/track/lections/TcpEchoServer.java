package track.lections;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpEchoServer {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter: <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        ServerSocketChannel serverSocket = ServerSocketChannel.open();//(servPort);
        serverSocket.socket().bind(new InetSocketAddress(servPort));

        ExecutorService pool = Executors.newFixedThreadPool(4);

        while (true) {

            ClientProcessor proc = new ClientProcessor();
            proc.setClntSock(serverSocket.accept());

            pool.submit(proc);
        }
    }
}


class ClientProcessor implements Runnable {
    private static final int BUFSIZE = 32;
    private SocketChannel clntSock;

    public void setClntSock(SocketChannel clntSock) {
        this.clntSock = clntSock;
    }

    public void run() {
        try {
            clntSock.configureBlocking(false);
            SocketAddress clientAddress = clntSock.getRemoteAddress();
            System.out.println("Handling client at " + clientAddress + " in thread " + Thread.currentThread().getId());

            ByteBuffer recieveBuf = ByteBuffer.allocate(BUFSIZE);
            int recvMsgSize = clntSock.read(recieveBuf);

            recieveBuf.flip();

            while (recieveBuf.hasRemaining()) {
                System.out.print((char) recieveBuf.get()); // read 1 byte at a time
            }

            clntSock.write(recieveBuf);
            recieveBuf.clear();
//            recvMsgSize = clntSock.read(recieveBuf);


            clntSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}