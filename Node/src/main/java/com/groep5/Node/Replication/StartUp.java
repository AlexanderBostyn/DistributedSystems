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
        File directory = new File("C:\\UAProgrammas\\IntellijProjects\\DIST");
        files = directory.listFiles();
    }
    public void sendFiles() {
        for(File file : files) {
            SendFile sendFile = new SendFile(this.node, file);
            sendFile.run();
        }
    }

    public StartUp(Node node) {
        this.node = node;
        lookForFiles();
        sendFiles();
        logger.info("Start up file sharing");
    }
}

