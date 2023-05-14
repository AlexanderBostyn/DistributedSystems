package com.groep5.Node.Service.Replication;

import com.groep5.Node.Node;
import com.groep5.Node.NodeApplication;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
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

Lokaal bestand wordt verwijdert:
    De fileOwner waarschuwen
        deze moet dan de replica verwijderen
        alle locaties bijgehouden in de log moeten ook verwijderen

 */
public class Replication {
    private final Node node = NodeApplication.getNode();
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * All the functions needed for Replication.
     * Called from main thread.
     */
    public static void start() {
        new StartUp().start(); //Not a thread
        new Detection().start();
    }

    /**
     * This helper function should determine where a file needs to be sent to.
     * This is based on the hash o filename and the namingServer GET /file/hash
     * If the namingServer responds with our own ip, we should send it to our previous.
     * Should be possible from a static context.
     * @param fileName the name of the file.
     * @param state The {@link ReplicationState state} of replication.
     * @return the ip address where we should send the file.
     */
    public static Inet4Address findIp(String fileName, ReplicationState state) {
        //TODO
        return null;
    }

    /**
     * This helper function determines if we are the owner of the file.
     * this means we should at our file to our log
     * @param fileName the name of the file we need to determine ownership off.
     * @return true if we are the owner.
     */
    public boolean isOwner(String fileName) {
        //TODO
        return false;
    }

    /**
     * This Function lists all files from a directory.
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
