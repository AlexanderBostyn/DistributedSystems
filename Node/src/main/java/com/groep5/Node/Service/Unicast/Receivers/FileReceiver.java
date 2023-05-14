package com.groep5.Node.Service.Unicast.Receivers;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;


/**
 * This class handles the socket stream as a file.
 */
public class FileReceiver {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final String[] message;
    private final Socket socket;

    public FileReceiver(String[] message, Socket socket) {
        this.message = message;
        this.socket = socket;

    }

    public File receive() {
        String filename = message[1];
        long size = Long.parseLong(message[2]);
        File file = new File("src/main/resources/replicated/" + filename);
        int bytes = 0;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            byte[] buffer = new byte[4*1024];
            while (size > 0 && (bytes = dis.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0,bytes);
                size -= bytes;
            }
            dis.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
