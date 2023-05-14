package com.groep5.Node.Model;

import com.groep5.Node.Service.NodeLifeCycle.Failure;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
@Component
@Data
public class NodePropreties {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private String nodeName;
    public int nodeHash;
    public int previousHash;
    public int nextHash;
    private final Inet4Address nodeAddress;
    private int connectionsFinished = 0;
    public int numberOfNodes = -1;
    private Failure failure;
    public HashMap<File, ArrayList<Inet4Address>> log = new HashMap<>();

    public NodePropreties(){
        try {
            nodeAddress = (Inet4Address) Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            logger.severe("Couldn't fetch own IP");
            throw new RuntimeException(e);
        }
    }

}
