package com.groep5.Naming.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;

import java.net.InetAddress;
import java.util.HashMap;

public class Persistence {

    /**
     * Saves data to a file in the resources folder and returns that file
     * @param map hashes of addresses we want to save
     * @return saved file, null if error.
     */
    public static File SaveMap (HashMap<Integer, InetAddress> map, String fileName) {
            try {
                File file = new File("src/main/resources/" + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter writer = new FileWriter("src/main/resources/" + fileName);
                String mapJson = new ObjectMapper().writeValueAsString(map);
                writer.write(mapJson);
                writer.close();
                return file;

            } catch (Exception e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        return null;
    }

    public static HashMap<Integer, InetAddress> LoadMap(File file) {
        
    }



}
