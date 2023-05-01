package com.groep5.Naming.server.Service.multicast;

import com.groep5.Naming.server.Service.ApplicationContextProvider;
import com.groep5.Naming.server.Service.Hasher;
import com.groep5.Naming.server.Service.SHAHasher;
import com.groep5.Naming.server.Service.UnicastPublisher;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class MulticastHandler implements Runnable{
    private String message;

    private Hasher hasher;
    //@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")

    //private UnicastPublisher unicastPublisher;

    private Logger logger = Logger.getLogger(MulticastHandler.class.getName());

    public MulticastHandler(String msg){
        message=msg;
        hasher = new SHAHasher(ApplicationContextProvider.getApplicationContext());
    }

    @Override
    public void run() {
        String[] msgSplit= message.split(";");
        if (msgSplit[0].equals("discovery"))
        {
            logger.info("discovery message received:"+message);
            String name = msgSplit[1];
            try {
                InetAddress address = InetAddress.getByName(msgSplit[2]);
                logger.info("name of node: " +name+ " address: "+address);
                int nodeCount=hasher.returnAmountOfNodes();
                logger.info("networksize:"+nodeCount+", address: "+address);
                UnicastPublisher.sendMessage("discovery;namingServer;"+nodeCount,address);
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
