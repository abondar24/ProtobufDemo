package org.abondar.experimental.protobufdemo;

import org.abondar.experimental.protobufdemo.client.ClientThread;
import org.abondar.experimental.protobufdemo.server.ServerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {

    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {

        logger.info("Starting demo");
        ServerThread server = new ServerThread();
        server.start();

        ClientThread client = new ClientThread();
        client.start();


    }
}
