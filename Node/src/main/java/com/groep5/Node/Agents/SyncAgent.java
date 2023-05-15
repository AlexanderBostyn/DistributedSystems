package com.groep5.Node.Agents;

import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.SpringContext;
import org.apache.logging.log4j.simple.SimpleLogger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.instrument.Instrumentation;
import java.net.ContentHandler;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Logger;

public class SyncAgent {
    private static final Logger logger = Logger.getLogger(String.valueOf(SyncAgent.class));
    public static void premain(String agentArgs, Instrumentation inst) {
        logger.info("create log");
        createLog();
        logger.info("start looking at next node for updates");
        new UpdateLog().start();
    }

    private static void createLog() {
        NodePropreties nodePropreties = SpringContext.getBean(NodePropreties.class);
        //TODO: to create a list of  all the files the node owns
        //Gewoon al aanmaken en vullen met zen eigen.
    }

    public static class UpdateLog extends Thread {
        private static void updateLog() {
            //TODO: update the list based on the state of the next node (sync them)
            //Waardes aanpassen. Hoe?
        }

        @Override
        public void run() {
            updateLog();
        }
    }

    public void lockFile(File file) {
        //TODO: to lock a file so it can be downloaded safely
        try {
            logger.info("lock file " + file);
            RandomAccessFile newFile = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = newFile.getChannel();
            FileLock lock = fileChannel.lock();
            //lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
