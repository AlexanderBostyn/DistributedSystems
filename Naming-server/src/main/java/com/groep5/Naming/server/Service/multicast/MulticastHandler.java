package com.groep5.Naming.server.Service.multicast;

import com.groep5.Naming.server.Service.SHAHasher;
import com.groep5.Naming.server.Service.UnicastPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class MulticastHandler implements Runnable{
    private String message;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    SHAHasher hasher;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    UnicastPublisher unicastPublisher;

    Logger logger = Logger.getLogger(MulticastHandler.class.getName());

    public MulticastHandler(String msg){
        message=msg;
    }

    @Override
    public void run() {
        String[] msgSplit= message.split(" ");
        if (msgSplit[0].equals("discovery"))
        {
            String name = msgSplit[1];
            try {
                InetAddress address = InetAddress.getByName(msgSplit[2]);
                logger.info("name of node: " +name+ " address: "+address);
                int nodeCount=hasher.returnAmountOfNodes();
                unicastPublisher.sendMessage("NamingServer;"+nodeCount,address.toString());
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

        }




        /*
        String firstWordOfMsg = message.split(" ")[0];

        switch (firstWordOfMsg){
            case "test":
                logger.info("test");

            case "notTest":
                logger.info("not a test");
        }

         */
    }
}
