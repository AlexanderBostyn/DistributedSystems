package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
import com.groep5.Node.Service.Unicast.UnicastSender;
import com.groep5.Node.SpringContext;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.file.*;
import java.util.logging.Logger;

/**
 * Detects events.
 * Mainly file creation and deletion.
 */
public class Detection extends Thread {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void lookForFiles() throws IOException, InterruptedException {
        //TODO deletion
        Path directory = Paths.get("src/main/resources/local");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        File latestFile = new File("");
        logger.info("Watching directory: " + directory);

        while (!isInterrupted()) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path fileName = (Path) event.context();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    //possible error here: fileName could be the entire path instead of only the name of the file.
                    File newFile = new File("src/main/resources/local/" + fileName);
                    if (!newFile.equals(latestFile)) {
                        logger.info("File created: " + fileName);

                        Inet4Address ip = Replication.findIp(newFile.getName(), ReplicationState.DETECTION); //using newFile because it ensures only the last part is used.
                        UnicastSender.sendFile(newFile, ip);

                        latestFile = newFile;
                    }
                }
            }
            boolean valid = watchKey.reset();
            if (!valid) {
                break;
            }
        }
    }

    @Override
    public void run() {
        try {
            lookForFiles();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

