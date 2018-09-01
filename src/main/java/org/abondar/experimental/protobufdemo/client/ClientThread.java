package org.abondar.experimental.protobufdemo.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static org.abondar.experimental.protobufdemo.util.MsgUtil.START_MSG;
import static org.abondar.experimental.protobufdemo.util.MsgUtil.STOP_MSG;

public class ClientThread extends Thread {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void run(){
        try (ZContext context = new ZContext()){

            logger.info("Connecting to server");
            ZMQ.Socket socket = context.createSocket(ZMQ.REQ);
            socket.connect("tcp://localhost:5555");

            String request = START_MSG;
            socket.send(request.getBytes(ZMQ.CHARSET),0);
            logger.info("Sent start message");

            byte [] resp = socket.recv(0);
            if (resp.length!=0) {
                logger.info("Got server message: "+new String(resp, ZMQ.CHARSET));

            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex){
                logger.error(ex.getMessage());
            }

            request = STOP_MSG;
            socket.send(request.getBytes(ZMQ.CHARSET),0);
            logger.info("Sent stop message");



        }
    }
}
