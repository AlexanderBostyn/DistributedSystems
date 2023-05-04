package com.groep5.Node.Replication;

import com.groep5.Node.Node;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public class StartUp implements Runnable {
    public Node node;
    public File[] files;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    public StartUp(Node node) {
        this.node = node;
    }

    public void lookForFiles() {
        File directory = new File("C:\\UAProgrammas\\IntellijProjects\\DIST");
        files = directory.listFiles();
    }
    public void SendFiles() {
        for(File file : files) {
            SendFile sendFile = new SendFile(this.node, file);
            sendFile.run();
        }
    }
    @Override
    public void run() {
        lookForFiles();
        SendFiles();
        logger.info("Start up file sharing");
    }
}

