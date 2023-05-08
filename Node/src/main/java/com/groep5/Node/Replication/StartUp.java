package com.groep5.Node.Replication;

import com.groep5.Node.Node;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public class StartUp {
    public Node node;
    public File[] files;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void lookForFiles() {
        File directory = new File("src/main/resources");
        files = directory.listFiles();
        logger.info("Files: " + files);
    }
    public void sendFiles() {
        for(File file : files) {
            new SendFile(this.node, file).start();
        }
    }

    public StartUp(Node node) {
        this.node = node;
        logger.info("Start up file sharing");
        lookForFiles();
        sendFiles();
    }
}


