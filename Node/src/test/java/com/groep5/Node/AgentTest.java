package com.groep5.Node;

import com.groep5.Node.Agents.SyncAgent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SpringBootTest
public class AgentTest {
    @Autowired
    private SyncAgent syncAgent;
    private final Logger logger = Logger.getLogger(this.getClass().getName());


    @Test
    public void createLogTest() throws FileNotFoundException {
        HashSet<File> fileSet = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            File file = new File("src/main/resources/local/file" + i + ".txt");
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("hello");
            printWriter.flush();
            printWriter.close();
            fileSet.add(file);
        }
        for (int i = 4; i < 8; i++) {
            File file = new File("src/main/resources/replicated/file" + i + ".txt");
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("hello");
            printWriter.flush();
            printWriter.close();
            fileSet.add(file);
        }
        HashMap<String, Boolean> result = syncAgent.createLog();
        logger.info(result.toString());
        Assertions.assertEquals(fileSet.stream().map(File::getName).collect(Collectors.toCollection(HashSet::new)), result.keySet());
        for (File file: fileSet) {
            Assertions.assertTrue(file.delete());
        }
    }

}

