package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
import com.groep5.Node.SpringContext;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public class StartUp {
    public Node node;
    public File[] files;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void lookForFiles() {
        File directory = new File("src/main/resources/local");
        files = directory.listFiles();
        logger.info("Files: " + Arrays.toString(files));
    }
    public void sendFiles() {
        for(File file : files) {
            new SendFile(file).start();
        }
    }

    public StartUp() {
        this.node = getNode();
        logger.info("Start up file sharing");
        lookForFiles();
        sendFiles();
    }
    private Node getNode() {
        return SpringContext.getBean(Node.class);
    }
}


