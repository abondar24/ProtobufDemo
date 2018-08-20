package org.abondar.experimental.protobufdemo;

import org.abondar.experimental.protobufdemo.client.ClientThread;
import org.abondar.experimental.protobufdemo.server.ServerThread;

import java.util.logging.Logger;

public class Main {

    //private static Logger logger = Logger.getLogger();
    public static void main(String[] args) {

        ServerThread server = new ServerThread();
        server.run();

        ClientThread client = new ClientThread();
        client.run();
    }
}
