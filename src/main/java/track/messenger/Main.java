package track.messenger;

import track.messenger.messages.ChatCreateMsg;
import track.messenger.messages.RegisterMsg;
import track.messenger.net.JsonProtocol;
import track.messenger.net.MessengerServer;
import track.messenger.net.Protocol;
import track.messenger.net.ProtocolException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Main {


    public static void main(String[] args) {

        MessengerServer server = new MessengerServer();
        server.setPort(19000);
        server.setPoolSize(4);
        server.init();
        server.start();
    }


}
