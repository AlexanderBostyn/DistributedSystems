package com.groep5.Node.Agents;

import com.groep5.Node.Model.Log;
import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.NodeLifeCycle.Replication.ReplicationService;
import com.groep5.Node.Service.Unicast.UnicastSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;


public class FailureAgent implements Runnable , Serializable {
private Log log;
private NamingServerService namingServerService;
private NodePropreties nodePropreties;
private UnicastSender unicastSender;
private int failingNodeHash;
    public FailureAgent(int failingNodeHash) {
        this.failingNodeHash=failingNodeHash;

        this.log = NodeApplication.getLog();
        this.namingServerService = NodeApplication.getNamingServerService();
        this.nodePropreties= NodeApplication.getNodePropreties();
        this.unicastSender=NodeApplication.getUnicastSender();
    }

    //ToDo trigger this method in important exception catchers
    public void startFailureAgent(){//when our node fails, send files + log to prev node

    }

    public void sendFilesToOwner(){
        ArrayList<File> files= ReplicationService.listDirectory("src/main/resources/replicated");
        for (Log.LogEntry entrySet: log.getEntrySet()) {//iterate over files
            try {
                //check if we are owner of file
                if (nodePropreties.getNodeAddress().equals(namingServerService.getFileOwner(namingServerService.calculateHash(entrySet.getFileName()))))
                {
                    //send files to new owner (prev node)
                    for (File file: files) {
                        if (file.getName().equals(entrySet.getFileName())){//find file corresponding to fileName
                            unicastSender.sendFile(file,namingServerService.getIp(nodePropreties.previousHash),false);
                        }
                    }
                    //send log



                }

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void run() {
        //read current files and send files that this node owns to prev
        sendFilesToOwner();
        //after running on this node, send agent to prev node
        try {
            InetAddress ip = namingServerService.getIp(nodePropreties.previousHash);
            unicastSender.sendFailureAgent(ip,this);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }


    }
    //TODO: transfer the ownership of the files owned by the failing node to the new owner

    //TODO: On each node, the Failure Agent should check if there are files that are owned by
    // the failing node, and if so, the file(s) should be transferred to the new owner, and
    // the log file should be updated on that change
}
