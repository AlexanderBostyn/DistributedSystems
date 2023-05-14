package com.groep5.Node;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;
import java.util.logging.Logger;

public class FileWatcher {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    @Test
    public void watchFiles() throws IOException, InterruptedException {
        Path directory = Paths.get("src/main/resources/local");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        logger.info("Watching directory: " + directory);
        new FileMaker().start();
        boolean isFound = false;
        while (!isFound) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path fileName = (Path) event.context();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    logger.info("File created: " + fileName);
                    File file = new File("src/main/resources/local/" + fileName);
                    BufferedReader reader = new BufferedReader( new FileReader(file));
                    Assertions.assertEquals(reader.readLine(), "Hello vriendjes");
                    reader.close();
                    System.out.println(file.delete());
                    isFound = true;
                }
            }

            boolean valid = watchKey.reset();
            if (!valid) {
                break;
            }
        }
    }
    static class FileMaker extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
                File newFile = new File("src/main/resources/local/testfile.test");
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(newFile), true);
                printWriter.println("Hello vriendjes");
                printWriter.close();
            } catch (FileNotFoundException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

