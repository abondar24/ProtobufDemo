package org.abondar.experimental.protobufdemo.server;

import com.google.protobuf.InvalidProtocolBufferException;
import org.abondar.experimental.protobufdemo.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static org.abondar.experimental.protobufdemo.util.MsgUtil.RESP_MSG;
import static org.abondar.experimental.protobufdemo.util.MsgUtil.START_MSG;
import static org.abondar.experimental.protobufdemo.util.MsgUtil.STOP_MSG;

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
                    String strRep = new String(reply,ZMQ.CHARSET);
                    if (strRep.equals(START_MSG) || strRep.equals(STOP_MSG)){
                        logger.info("Got message from client: "+ new String(reply,ZMQ.CHARSET));
                        socket.send(RESP_MSG.getBytes(ZMQ.CHARSET),0);
                        if (strRep.equals(STOP_MSG)){
                            logger.info("Demo is over");
                            socket.close();
                            context.close();
                            Thread.currentThread().interrupt();
                        }
                    } else {

                        try {
                            Person.PersonMsg person= Person.PersonMsg.parseFrom(reply);
                            System.out.println("Getting person: "+person.toString());
                            Thread.sleep(10000);
                            socket.send(RESP_MSG.getBytes(ZMQ.CHARSET),0);

                        } catch (InvalidProtocolBufferException|InterruptedException ex){
                            logger.error(ex.getMessage());
                        }

                    }

                }

            }
        }
    }
}
