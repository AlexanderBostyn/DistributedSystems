package com.groep5.Node.Agents;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

public class SyncAgent {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Start!");
    }

    private void createLog() {
        //TODO: to create a list of  all the files the node owns
    }

    private void updateLog() {
        //TODO: update the list based on the state of the next node (sync them)
    }

    private void lockFile() {
        //TODO: to lock a file so it can be downloaded safely
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
