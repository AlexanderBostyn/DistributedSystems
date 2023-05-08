package com.groep5.Node.Replication;

import com.groep5.Node.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

public class Detection extends Thread {
    public Node node;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public Detection(Node node) {
        this.node = node;
    }

    public void lookForFiles() throws IOException, InterruptedException {
        Path directory = Paths.get("src/main/resources/local");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        logger.info("Watching directory: " + directory);

        while (true) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path fileName = (Path) event.context();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    File newFile = new File("src/main/resources/local/" + fileName);
                    if (newFile != node.latestFile) {
                        logger.info("File created: " + fileName);
                        new SendFile(this.node, newFile).start();
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

