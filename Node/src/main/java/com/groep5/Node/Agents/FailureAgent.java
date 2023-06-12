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
private boolean isNewAgent=false;

    public FailureAgent(int failingNodeHash, boolean isNewAgent) {//called when creating an agent from the controller
        this.failingNodeHash = failingNodeHash;
        this.isNewAgent = isNewAgent;
        initFailureAgent();
    }
    public FailureAgent() {
        initFailureAgent();
    }

    public void initFailureAgent(){//get beans
        this.log = NodeApplication.getLog();
        this.namingServerService = NodeApplication.getNamingServerService();
        this.nodePropreties= NodeApplication.getNodePropreties();
        this.unicastSender=NodeApplication.getUnicastSender();
    }

    public void startFailureAgent(){
        failingNodeHash=nodePropreties.getNodeHash();
        isNewAgent=true;
        run();
    }
    public void sendFilesToOwner(){
        ArrayList<File> files= ReplicationService.listDirectory("src/main/resources/replicated");
        for (Log.LogEntry entry: log.getEntrySet()) {//iterate over logEntries (filenames)
            try {
                //check if we are owner of file
                if (nodePropreties.getNodeAddress().equals(namingServerService.getFileOwner(namingServerService.calculateHash(entry.getFileName()))))
                {
                    //send files to new owner (prev node)
                    for (File file: files) {
                        if (file.getName().equals(entry.getFileName())){//find file corresponding to fileName
                            unicastSender.sendFile(file,namingServerService.getIp(nodePropreties.previousHash),false,"failureAgent");
                        }
                    }
                    //send log
                    Log newLog=new Log();
                    Log.LogEntry clonedEntry = entry.clone();
                    newLog.put(clonedEntry);
                    unicastSender.sendLog(newLog,namingServerService.getIp(nodePropreties.previousHash),"failureAgent");


                }

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public FailureAgentGetDTO createDTO()
    {
        FailureAgentGetDTO dto=new FailureAgentGetDTO(failingNodeHash,isNewAgent);
        return dto;
    }

    @Override
    public void run() {//called when an agent is received through the controller
        if( isNewAgent)//first instance of the failureAgent
        {
            //read current files and send files that this node owns to prev node in ring
            sendFilesToOwner();
        }
        if (isNewAgent || !(failingNodeHash==nodePropreties.getNodeHash()))//check if we're not back at the start, then proceed
        {
            //after running on this node, send agent to prev node
            isNewAgent = false;
            try {
                FailureAgentGetDTO dto= createDTO();
                InetAddress ip = namingServerService.getIp(nodePropreties.previousHash);
                unicastSender.sendFailureAgent(ip,dto);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }


    }
    //TODO: transfer the ownership of the files owned by the failing node to the new owner

    //TODO: On each node, the Failure Agent should check if there are files that are owned by
    // the failing node, and if so, the file(s) should be transferred to the new owner, and
    // the log file should be updated on that change
}
