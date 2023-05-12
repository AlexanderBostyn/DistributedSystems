package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
import com.groep5.Node.SpringContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * The method class should be called when our nextNode is changed
 * Then it should resend the files with hashes greater than the new nextNode.
 */
public class UpdateNewNode {
    public Node node;
    public ArrayList<File> files = new ArrayList<>();
    public int receivedNodeHash;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    public UpdateNewNode( int recievedNodeHash) {
        this.node = getNode();
        this.receivedNodeHash = recievedNodeHash;
        lookForFiles();
        resendFiles();
    }

    public void lookForFiles() {
        File directory = new File("src/main/resources/replicated");
        File[] fileArray = directory.listFiles();
        if (fileArray != null) {
            files.addAll(List.of(fileArray));
            logger.info("Files: " + files);
        }
    }

    private int calcHash(File file) {
        return node.calculateHash(file.getName());
    }

    private void resendFiles() {
        for (File f : files) {
           int fileHash = calcHash(f);
           int newNextNodeHash = receivedNodeHash;
           // The new next NodeHash is smaller than our hash, this means we are at the end of our ring.
            // This creates problems for linearity that can be fixed if we just add 32768 to all the hashes that are at the beginning of the ring
           if (newNextNodeHash < node.nodeHash) {
               newNextNodeHash += 32768;

               // The file is also located at the beginning of the ring, adding 32768 will make the ring linear again.
               if (fileHash < node.nodeHash) {
                   fileHash += 32768;
               }
           }
           if (fileHash > newNextNodeHash) {
               logger.info("file (" + f.getName() + ") is send to node with hash:" + receivedNodeHash);
               new SendFile(f).start();
               deleteFile(f);
           }
//            logger.info("send file " + f.getName() + " to new location");
//            int fileHash = calcHash(f);
//            if (node.nodeHash > node.nextHash) {
//                if (receivedNodeHash > node.nodeHash && fileHash > receivedNodeHash) {
//                    //new SendFile(node, f).start();
//                    new SendFile(f).start();
//                    deleteFile(f);
//                }
//                else if (receivedNodeHash > node.nodeHash && fileHash < node.nodeHash)
//                {
//                    //new SendFile(node, f).start();
//                    new SendFile( f).start();
//                    deleteFile(f);
//                }
//                else if (receivedNodeHash < node.nextHash && fileHash > receivedNodeHash)
//                {
//                    //new SendFile(node, f).start();
//                    new SendFile( f).start();
//                    deleteFile(f);
//                }
//            }
//            else {
//                if (receivedNodeHash > node.nodeHash && fileHash > receivedNodeHash) {
//                    new SendFile( f).start();
//                    //new SendFile(node, f).start();
//
//                    deleteFile(f);
//                }
//            }
        }
    }

    private void deleteFile(File f) {
        if (f.delete()) {
            node.dellLog(f);
            logger.info(f.getName() + " is deleted");
        }
    }

    private Node getNode() {
        return SpringContext.getBean(Node.class);
    }
}
