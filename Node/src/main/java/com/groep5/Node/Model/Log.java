package com.groep5.Node.Model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class contains all the data for the files we are owner of and where they are stored.
 * Also, all the logic in this class should be here.
 */
@Data
@Component
public class Log implements Cloneable, Serializable {
    /**
     * The set that contains all {@link LogEntry entries};
     */
    private transient final Logger logger = Logger.getLogger(this.getClass().getName());
    private volatile Set<LogEntry> entrySet = new HashSet<>();//LogEntry has a fileName and a Set of addresses

    public boolean contains(String fileName) {
        return entrySet.stream().map(LogEntry::getFileName).anyMatch(s -> s.equals(fileName));
    }

    /**
     * Get an {@link LogEntry entry} by fileName.
     * @param fileName the fileName you want to retrieve the entry of.
     * @return null if entrySet does not contain the fileName.
     */
    public LogEntry get(String fileName) {
        return entrySet.stream().filter(logEntry -> logEntry.getFileName().equals(fileName)).findFirst().orElse(null);
    }

    public void put(LogEntry entry) {
        entrySet.add(entry);
    }

    /**
     * Add an empty {@link LogEntry entry} with the fileName.
     * @param fileName the name of the file.
     * @return true if entry wasn't already present.
     */
    public boolean add(String fileName) {
        if (get(fileName) != null) {
            logger.fine("Tried to add file: " + fileName + " to the entry set, but the entry already existed, no action was taken instead.");
            return false;
        }
        LogEntry entry = new LogEntry();
        entry.setFileName(fileName);
        entrySet.add(entry);
        return true;
    }

    /**
     * Add the address to the fileName, if the fileName doesn't exist yet the entry is created.
     * @param fileName the name of the file.
     * @param address the address you want to add to the file
     * @return false if new entry had to be created.
     */
    public boolean add(String fileName, Inet4Address address) {
        LogEntry entry = get(fileName);
        if (entry == null) {
            entry = new LogEntry();
            entry.setFileName(fileName);
            entry.add(address);
            put(entry);
            return false;
        }
        entry.add(address);
        return true;
    }

    /**
     * Add an entry to a log in a non-destructive way
     * @param entry the entry you want to add.
     * @return true if any changes where made.
     */
    public boolean add(LogEntry entry) {
        if (contains(entry.fileName)) {
            return get(entry.fileName).add(entry);

        }
        return entrySet.add(entry);

    }


    /**
     * Delete an {@link LogEntry entry} by fileName.
     * @param fileName the name of the File
     * @return false if entrySet didn't contain the fileName;
     */
    public synchronized boolean  delete(String fileName) {
        LogEntry entry = get(fileName);
        logger.info("fetching " + fileName + ":" + entry + "/" );
        if (entry != null) {
            logger.info( Integer.toString(entry.hashCode()));
        }
        logger.info("hashCodes: " + entrySet.stream().map(LogEntry::hashCode).collect(Collectors.toCollection(ArrayList::new)));
        logger.info("contains: " + entrySet.contains(get(fileName)));
        return entrySet.remove(get(fileName));
    }

    /**
     * Delete a specific address for a specific entry.
     * @param fileName the name of the file from which you want to remove a location.
     * @param address the location of the address.
     * @return true if entrySet contained file and entry contained the address.
     */
    public boolean delete(String fileName, Inet4Address address) {
        LogEntry entry = get(fileName);
        if (entry == null) {
            return false;
        }
        return entry.delete(address);
    }

    public int size() {
        return entrySet.size();
    }

    @Override
    public Log clone() {
        try {
            Log clone = (Log) super.clone();
            clone.setEntrySet(new HashSet<>(entrySet));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * An entry in {@link Log log}.
     */
    @Data
    public static class LogEntry implements Cloneable, Serializable{
        /**
         * The key of the entry, it's the last part of the filePath.
         * e.g: "src/main/replicated/file1.txt" -> "file1.txt"
         */
        private String fileName;

        /**
         * The values of an entry, contains all the Inet4Addresses
         */
        private Set<Inet4Address> addresses = new HashSet<>();

        /**
         * Checks if an entry contains a certain address.
         * We check equality based on {@link Inet4Address#getAddress()}.
         * @param address the address we want to check
         * @return true of is entry contains the Ip address
         */
        public boolean contains(Inet4Address address) {
            return addresses.contains(address);
        }

        /**
         * Deletes the address of the addressSet.
         * @param address the address you want to remove.
         * @return true if set contained the element.
         */
        public boolean delete(Inet4Address address) {
            return addresses.remove(address);
        }

        /**
         * Add an address to the addressSet.
         * @param address the address you want to add.
         * @return true if the set did not already contain the address.
         */
        public boolean add(Inet4Address address) {
            return addresses.add(address);
        }

        /**
         * Combine the addresses from the entry with our addresses.
         * @param entry the entry we want to add to our own addressSet
         * @return true if the addressSet was changed.
         */
        public boolean add(LogEntry entry) {
            return addresses.addAll(entry.addresses);
        }

        /**
         * get the size of the AddressSet.
         * @return AddressSet.size()
         */
        public int size() {
            return addresses.size();
        }

        @Override
        public LogEntry clone() {
            try {
                LogEntry clone = (LogEntry) super.clone();
                clone.setAddresses(new HashSet<>(addresses));
                clone.setFileName(fileName);
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        @Override
        public int hashCode() {
            return fileName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
}
