package com.groep5.Node.Agents;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.NodeLifeCycle.Replication.ReplicationService;
import lombok.Data;
import net.officefloor.plugin.variable.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Data
public class SyncAgent{
    private NodePropreties nodePropreties;
    private NamingServerService namingServerService;
    private static final Logger logger = Logger.getLogger(String.valueOf(SyncAgent.class));
    private HashMap<String, Boolean> agentList = new HashMap<>();
    private boolean isActive=false;

    public void setActive(boolean active){
        isActive=active;
    }
    @Autowired
    public SyncAgent(NodePropreties nodePropreties, NamingServerService namingServerService) {
        this.nodePropreties = nodePropreties;
        this.namingServerService = namingServerService;

    }
    public void startSyncAgent()
    {
        logger.info("Agent is starting");
        this.agentList.putAll(createLog());
        logger.info("Start looking at next node for updates");
        nodePropreties.startNewUpdateLogTask();
        try {
            fileWatching();
        } catch (InterruptedException e) {
            new FailureAgent().startFailureAgent();
            throw new RuntimeException(e);
        }
    }


    public HashMap<String, Boolean> createLog() {
        logger.info("Creating agentList");
        ArrayList<File> fileArrayList = ReplicationService.listDirectory("src/main/resources/replicated");
        fileArrayList.addAll(ReplicationService.listDirectory("src/main/resources/local"));
        HashMap<String, Boolean> result = new HashMap<>();
        fileArrayList.stream().distinct().forEach(file -> result.put(file.getName(), false));
        return result;
    }


    public void putInAgentList(String filename, Boolean isLocked)
    {
        agentList.put(filename,isLocked);
    }
    public void replaceInAgentList(String filename, Boolean isLocked)
    {
        agentList.replace(filename,isLocked);
    }

    public static class UpdateLog extends Thread {
        private NodePropreties nodePropreties;
        private NamingServerService namingServerService;
        private SyncAgent syncAgent;

        public UpdateLog() {
            this.nodePropreties = NodeApplication.getNodePropreties();
            this.namingServerService = NodeApplication.getNamingServerService();
            this.syncAgent = NodeApplication.getSyncAgent();
        }

        private void updateLog() throws Exception {
            /**
             * Every 5 seconds the agent will contact the next node
             * It will look at the log of the agent
             * Any files that aren't in his log yet will be added
             * status of files is synced
            */
            while(true) {
                HashMap<String,Boolean> nextNodeList=getAgentListFromNextNode();
                for (Map.Entry<String, Boolean> entry : nextNodeList.entrySet()) {
                    String fileName = entry.getKey();
                    Boolean isLocked = entry.getValue();
                    if (!syncAgent.getAgentList().containsKey(fileName)){//this file is not present on our node
                        syncAgent.putInAgentList(fileName,isLocked);
                    }else{//sync status of files
                        syncAgent.replaceInAgentList(fileName,isLocked);
                    }
                }
            }
        }
        public HashMap<String,Boolean> getAgentListFromNextNode() throws Exception {
            logger.info("Look at next node");
            Thread.sleep(5000L);
            int nextHash = this.nodePropreties.nextHash;
            //call controller on next node
            InetAddress ip= namingServerService.getIp(nextHash);
            WebClient client=WebClient.create("http://" + ip.getHostAddress() + ":8080");
            Mono<HashMap<String, Boolean>> responseMono =client.get()
                    .uri("/agentlist")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<HashMap<String, Boolean>>() {});
            return responseMono.block();

        }

        @Override
        public void run() {
            try {
                updateLog();
            } catch (Exception e) {
                new FailureAgent().startFailureAgent();
                throw new RuntimeException(e);
            }
        }
    }

    public void fileWatching() throws InterruptedException {
        for(String f : agentList.keySet()) {
            new FileLocking(f).start();
        }
    }

    public class FileLocking extends Thread {
        private String fileName;
        private long lastModifiedTime;
        private File file;
        public FileLocking(String fileName) {
            this.fileName = fileName;
            this.file = new File("src/main/resources/local/" + fileName);
            lastModifiedTime = this.file.lastModified();
        }

        public boolean isFileBeingEdited() throws InterruptedException {
            long currentModifiedTime = file.lastModified();
            if(currentModifiedTime != lastModifiedTime) {
                lastModifiedTime = currentModifiedTime;
                return true;
            }
            return false;
        }

        @Override
        public void run() {
            try {
                while (isActive) {
                    if(isFileBeingEdited()) {
                        lockFile(fileName);
                    }
                    else {
                        unlockFile(fileName);
                    }
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void lockFile(String fileName) {
        logger.info("lock file " + fileName);
        if(agentList.containsKey(fileName)) {
            agentList.replace(fileName, true);
        }
    }

    public void unlockFile(String fileName) {
        if (agentList.get(fileName)){//true means locked
            logger.info("unlock file " + fileName);
            if(agentList.containsKey(fileName)) {
                agentList.replace(fileName, false);
            }
        }

    }
}