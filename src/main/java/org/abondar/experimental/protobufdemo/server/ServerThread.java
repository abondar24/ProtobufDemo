package org.abondar.experimental.protobufdemo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static org.abondar.experimental.protobufdemo.util.MsgUtil.RESP_MSG;

public class ServerThread extends Thread {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void run(){
        try (ZContext context = new ZContext()){

            ZMQ.Socket socket = context.createSocket(ZMQ.REP);
            socket.bind("tcp://*:5555");

            logger.info("Created server socket on port 5555");
            while (!Thread.currentThread().isInterrupted()){
                byte[] reply = socket.recv(0);

                if (reply.length!=0){
                    logger.info("Got message from client: "+ new String(reply,ZMQ.CHARSET));

                }

                socket.send(RESP_MSG.getBytes(ZMQ.CHARSET),0);
            }
        }
    }
}
