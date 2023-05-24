package com.groep5.Node.Agents;

import com.groep5.Node.Model.Log;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.Service.NamingServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Set;

@Service
public class FailureAgent {
private Log log;
private NamingServerService namingServerService;
private NodePropreties nodePropreties;
    @Autowired
    public FailureAgent(Log log, NamingServerService namingServerService, NodePropreties nodePropreties) {
        this.log = log;
        this.namingServerService = namingServerService;
        this.nodePropreties=nodePropreties;
    }

    //ToDo trigger this method in important exception catchers
    // public void startFailureAgent(){}//when our node fails, send files + log to prev node

    public void sendFilesToOwner(){
        for (Log.LogEntry entrySet: log.getEntrySet()) {
            try {
                //check if we are owner of file
                if (nodePropreties.getNodeAddress().equals(namingServerService.getFileOwner(namingServerService.calculateHash(entrySet.getFileName()))))
                {
                    //send files to new owner

                }

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
