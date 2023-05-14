package com.groep5.Node.Service.Replication;

import com.groep5.Node.Service.Unicast.UnicastSender;

import java.io.File;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.logging.Logger;

public class StartUp {
    public ArrayList<File> files;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public StartUp() {
    }

    public void start() {
        logger.info("Start up file sharing");
        this.files = Replication.listDirectory("src/main/resources/local");

        //sending File
        for (File file: files) {
            Inet4Address ip = Replication.findIp(file.getName(), ReplicationState.STARTUP);
            UnicastSender.sendFile(file, ip);
        }
    }
}


