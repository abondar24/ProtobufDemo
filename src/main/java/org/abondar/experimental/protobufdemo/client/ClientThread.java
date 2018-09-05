package org.abondar.experimental.protobufdemo.client;

import org.abondar.experimental.protobufdemo.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

            Person.PersonMsg person = createPerson();
            ByteArrayOutputStream msgStream = new ByteArrayOutputStream();

            try {
                System.out.println("Sending person: "+person.toString());
                person.writeTo(msgStream);
                socket.send(msgStream.toByteArray(),0);

            } catch (IOException ex){
                logger.error(ex.getMessage());
            }

            resp = socket.recv(0);
            if (resp.length!=0) {
                logger.info("Got server message: "+new String(resp, ZMQ.CHARSET));

            }

            request = STOP_MSG;
            socket.send(request.getBytes(ZMQ.CHARSET),0);
            logger.info("Sent stop message");

        }
    }


    private Person.PersonMsg createPerson(){
        Person.PersonMsg.Builder person = Person.PersonMsg.newBuilder();

        person.setId(24);
        person.setFirstName("Alex");
        person.setLastName("Bondar");
        person.setEmail("alex@startup.com");
        person.setEmp(Person.PersonMsg.Employee.DEVELOPER);

        Person.PersonMsg.Department.Builder department =Person.PersonMsg.Department.newBuilder();
        department.setDepartmentName("Advanced and experimental development");
        person.setDepartment(department.build());

        return person.build();
    }
}
