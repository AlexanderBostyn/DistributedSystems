package com.groep5.Node.Agents;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
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
    private HashMap<String, Boolean> agentList;
    private NodePropreties nodePropreties;
    private static NamingServerService namingServerService;
    private static final Logger logger = Logger.getLogger(String.valueOf(SyncAgent.class));
    private HashMap<String, Boolean> agentList = new HashMap<>();
    @Autowired
    public SyncAgent(NodePropreties nodePropreties, NamingServerService namingServerService) {
        logger.info("Agent is starting");
        this.agentList.putAll(createLog());
        logger.info("this is our log: " + fileArrayList);
        logger.info("Start looking at next node for updates");
        new UpdateLog().start();
        new FileLocking().start();
    }

    public HashMap<String, Boolean> getFileArrayList() {
        return fileArrayList;
    }

    public HashMap<String, Boolean> createLog() {
        logger.info("Creating agentList");
        fileArrayList = listDirectory("src/main/resources/replicated");
        fileArrayList.addAll(listDirectory("src/main/resources/local"));
        HashMap<String, Boolean> result = new HashMap<>();
        fileArrayList.stream().distinct().forEach(file -> result.put(file.getName(), false));
        return result;
    }

    public static ArrayList<File> listDirectory(String pathToDirectory) {
        File directory = new File(pathToDirectory);
        File[] fileArray = directory.listFiles();
        if (fileArray != null) {
            ArrayList<File> files = new ArrayList<>(List.of(fileArray));
            Logger.getGlobal().fine("Files scanned at " + pathToDirectory + ":" + files);
            return files;
        }
        return new ArrayList<File>();
    }

    public static class UpdateLog extends Thread {
        private NodePropreties nodePropreties;
        private NamingServerService namingServerService;
        private void updateLog(NodePropreties nodePropreties, NamingServerService namingServerService) throws InterruptedException, UnknownHostException {
            this.nodePropreties = nodePropreties;
            this.namingServerService = namingServerService;
            /**
             * Every 5 seconds the agent will contact the next node
             * It will look at the log of the agent
             * Any files that aren't in his log yet will be added
            */
            while(true) {
                logger.info("Look at next node");
                Thread.sleep(5000L);
                int nextHash = this.nodePropreties.nextHash;
//                Node node = namingServerService.getNode(nextHash);
//                ArrayList<File> files = node.sAgent.getFileArrayList();
                //Compare files
//                for(File f : files) {
//                    if(!fileArrayList.contains(f)) {
//                        fileArrayList.add(f);
//                    }
//                }
            }
        }

        @Override
        public void run() {
            try {
                updateLog(nodePropreties, namingServerService);
            } catch (InterruptedException | UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class FileLocking extends Thread {
        private static long lastModifiedTime = 0L;
        public void isFileBeingEdited(String filename) throws InterruptedException {
            File file = new File("src/main/resources/local/" + filename);
            long currentModifiedTime = file.lastModified();
            while(currentModifiedTime != lastModifiedTime) {
                lockFile(filename);
                lastModifiedTime = currentModifiedTime;
                Thread.sleep(1000L);
            }
            unlockFile(filename);
        }

        public void FileWatching() throws InterruptedException {
            while(true) {
                for(Map.Entry<String, Boolean> f : agentList.entrySet()) {
                    isFileBeingEdited(String.valueOf(f));
                }
            }
        }

        @Override
        public void run() {
            try {
                FileWatching();
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
        logger.info("unlock file " + fileName);
        if(agentList.containsKey(fileName)) {
            agentList.replace(fileName, false);
        }
    }
}