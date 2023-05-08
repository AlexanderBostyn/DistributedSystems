package com.groep5.Node.Replication;

import com.groep5.Node.Node;

import java.io.File;
import java.util.logging.Logger;

public class UpdateNewNode {
    public Node node;
    public File[] files;
    public int recievedNodeHash;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    public UpdateNewNode(Node node, int recievedNodeHash) {
        this.node = node;
        this.recievedNodeHash = recievedNodeHash;
        lookForFiles();
        resendFiles();
    }

    public void lookForFiles() {
        File directory = new File("src/main/resources");
        files = directory.listFiles();
        logger.info("Files: " + files);
    }

    public int calcHash(File file) {
        return node.calculateHash(file.getName());
    }

    public void resendFiles() {
        for (File f : files) {
            logger.info("send file " + f.getName() + " to new location");
            int hash = calcHash(f);
            if (node.nodeHash > node.nextHash) {
                if (recievedNodeHash > node.nodeHash && hash > recievedNodeHash) {
                    new SendFile(node, f).start();
                    deleteFile(f);
                }
                else if (recievedNodeHash > node.nodeHash && hash < node.nodeHash)
                {
                    new SendFile(node, f).start();
                    deleteFile(f);
                }
                else if (recievedNodeHash < node.nextHash && hash > recievedNodeHash)
                {
                    new SendFile(node, f).start();
                    deleteFile(f);
                }
            }
            else {
                if (recievedNodeHash > node.nodeHash && hash > recievedNodeHash) {
                    new SendFile(node, f).start();
                    deleteFile(f);
                }
            }
        }
    }

    public void deleteFile(File f) {
        if (f.delete()) {
            logger.info(f.getName() + " is deleted");
        }
    }
}
