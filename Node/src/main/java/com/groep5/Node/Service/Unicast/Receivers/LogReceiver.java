package com.groep5.Node.Service.Unicast.Receivers;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This class handles the socket stream as a log Hashmap(File, ArrayList(Inet4Address))
 */
public class LogReceiver {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * has the form: ["log", entrySet.length]
     */
    private final String[] message;
    private final Socket socket;

    public LogReceiver(String[] message, Socket socket) {
        this.message = message;
        this.socket = socket;
    }

    public HashMap<File, ArrayList<Inet4Address>> receive() {
        //TODO
        int size = Integer.parseInt(message[1]);
        logger.info("log size is: "+size);
        //File file = new File("src/main/resources/replicated/" + filename);
        int bytes = 0;
        try {

//            DataInputStream dis = new DataInputStream(socket.getInputStream());
//
//            int hashMapSize = dis.readInt();
//            // Receive the HashMap data
//            byte[] hashMapBytes = new byte[375];
//            logger.info("hashmapSize: " + hashMapSize);
//            dis.readFully(hashMapBytes);
//            //byte[] buffer = new byte[4*1024];
//            /*while (size > 0 && (bytes = dis.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
//                fileOutputStream.write(buffer, 0,bytes);
//                size -= bytes;
//            }
//             */
//            // Deserialize the HashMap
//            logger.info(Arrays.toString(socket.getInputStream().readAllBytes()));
            /*while (socket.getInputStream().read() != 10)
            {

            }

             */
            //logger.info(Arrays.toString(socket.getInputStream().readNBytes(8)));
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


//            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            HashMap<File, ArrayList<Inet4Address>> log = (HashMap<File, ArrayList<Inet4Address>>) ois.readObject();
//            socket.close();
            return log;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
