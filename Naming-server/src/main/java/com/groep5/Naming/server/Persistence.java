package com.groep5.Naming.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Persistence {

    /**
     * Saves data to a file in the resources folder and returns that file
     * @param map hashes of addresses we want to save
     * @return saved file, null if error.
     */
    public static File SaveMap (HashMap<Integer, InetAddress> map, String fileName) {
        File file = new File("src/main/resources/" + fileName);
        try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter writer = new FileWriter("src/main/resources/" + fileName);
                String mapJson = new ObjectMapper().writeValueAsString(map);
                writer.write(mapJson);
                writer.close();
            } catch (Exception e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        return file;
    }

    public static HashMap<Integer, InetAddress> LoadMap(File file) {
        HashMap<Integer, InetAddress> map = new HashMap<>();
        try {
            if (file.exists()) {
                String mapJson = Files.readString(Path.of(file.getPath()));
                HashMap<String, String> tempMap = new ObjectMapper().readValue(file, HashMap.class);
                for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                    map.put(Integer.parseInt(entry.getKey()), InetAddress.getByName(entry.getValue()));
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        return map;
    }



}
