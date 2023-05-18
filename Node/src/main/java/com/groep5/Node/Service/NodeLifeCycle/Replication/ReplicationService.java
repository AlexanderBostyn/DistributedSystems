package com.groep5.Node.Service.NodeLifeCycle.Replication;

import com.groep5.Node.Model.Node;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.Unicast.UnicastSender;
import com.groep5.Node.SpringContext;
import org.apache.juli.logging.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
Node start: scanned zijn eigen files en stuur deze door naar:
    de owner van de file (verkregen door namingServer)
        maar als de owner van de file zichzelf is, moet die deze doorsturen naar diens vorige.
        deze originele node blijft wel owner
    de owner van de file houdt een log bij met waar een file allemaal opgeslagen is
    in een werkend systeem zit elke file op minstens 2 nodes

Nieuwe Node joined: enkel actie nodig als de nieuwe node de nieuwe next wordt
    alle bestanden waarvan deze node eigenaar is scannen (aka /replicated folder)
    Het bestand behoord nog steeds bij ons:
        geen actie nodig
    Het bestand hoord bij de nieuwe node:
        bestand doorsturen
        log doorsturen
        locale log entry verwijderen

Node shutdown:
    onze previous node wordt eigenaar van al onze replicas
    bestanden doorsturen naar previous node
        als previous node het bestand lokaal heeft -> sturen naar zijn previous node
    logs doorsturen naar previous node

    voor al onze locale bestanden
        de owner waarschuwen
        die moeten hun logs dan aanpassen

Lokaal bestand wordt verwijderd:
    De fileOwner waarschuwen
        deze moet dan de replica verwijderen
        alle locaties bijgehouden in de log moeten ook verwijderen

 */
@Service
public class ReplicationService {
    private final Logger logger = Logger.getLogger(this.getClass().getName());


    /**
     * All the functions needed for Replication.
     * Called from main thread.
     */
    public void startReplication() throws UnknownHostException {
        startup();
        new Detection().start();
    }

    public void startup() throws UnknownHostException {
        logger.info("Start up file sharing");
        ArrayList<File> files = listDirectory("src/main/resources/local");

        //sending File
        for (File file : files) {
            Inet4Address ip = findIp(file.getName(), ReplicationState.STARTUP);
            UnicastSender.sendFile(file, ip, false);
        }
    }

    /**
     * This helper function should determine where a file needs to be sent to.
     * This is based on the hash o filename and the namingServer GET /file/hash
     * If the namingServer responds with our own ip, we should send it to our previous.
     * Should be possible from a static context.
     *
     * @param fileName the name of the file.
     * @param state    The {@link ReplicationState state} of replication.
     * @return the ip address where we should send the file.
     */
    public static Inet4Address findIp(String fileName, ReplicationState state) throws UnknownHostException {
        Logger logger = Logger.getLogger("ReplicationService.findIp");
        NamingServerService namingServerService = SpringContext.getBean(NamingServerService.class);
        int fileHash = namingServerService.calculateHash(fileName);
        int currentHash = SpringContext.getBean(Node.class).getNodePropreties().nodeHash;
        int previousHash = NodeApplication.getNodePropreties().getPreviousHash();

        if (state == ReplicationState.SHUTDOWN) {
            logger.info("previousHash: " + previousHash);
            logger.info("fileHash: " + namingServerService.calculateHash(fileName));
            if (isOwner(fileName, previousHash)) {
                logger.severe("Previous node was owner of file: " + fileName + "Sending to their previous instead");
                previousHash = namingServerService.getPreviousHash(previousHash);
            }
            return namingServerService.getIp(previousHash);
        } else {
            if (!isOwner(fileName, currentHash)) {
                return namingServerService.getFileOwner(fileHash);
            }
            return namingServerService.getIp(previousHash);
//            while(namingServerService.getNextHash(currentHash) < fileHash) {
//                if (namingServerService.getNextHash(currentHash) < currentHash) {
//                    currentHash = namingServerService.getNextHash(currentHash);
//                    break;
//                }
//                currentHash = namingServerService.getNextHash(currentHash);
//            }
//            if (namingServerService.getIp(currentHash) == namingServerService.getFileOwner(fileHash)) {
//                currentHash = namingServerService.getPreviousHash(currentHash);
//            }
        }
    }

    /**
     * This helper function determines if a node is the owner of the file.
     * this means we should add/location our file to our log.
     * use {@link com.groep5.Node.Service.NamingServerService#getFileOwner(int)}
     *
     * @param fileName the name of the file we need to determine ownership off.
     * @param nodeHash the hash we want to check ownership againts.
     * @return true if we are the owner.
     */
    public static boolean isOwner(String fileName, int nodeHash) throws UnknownHostException {
        NamingServerService namingServerService = SpringContext.getBean(NamingServerService.class);
        int fileHash = namingServerService.calculateHash(fileName);
        return namingServerService.getFileOwner(fileHash).equals(namingServerService.getIp(nodeHash));
    }

    /**
     * This Function lists all files from a directory.
     *
     * @param pathToDirectory the path to the directory from root of project: "src/..".
     * @return A list of files, list will be empty if directory is empty.
     */
    public static ArrayList<File> listDirectory(String pathToDirectory) {
        File directory = new File(pathToDirectory);
        File[] fileArray = directory.listFiles();
        if (fileArray != null) {
            ArrayList<File> files = new ArrayList<>(List.of(fileArray));
            Logger.getGlobal().fine("Files scanned at " + pathToDirectory + ":" + files);
            return files;
        }
        return new ArrayList<File>();
    }


}
