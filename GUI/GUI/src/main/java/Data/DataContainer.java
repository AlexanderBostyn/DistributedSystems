package Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataContainer {
    public ArrayList<String> nodes;
    public ArrayList<String> files;
    public String nameServer;

    public DataContainer() {
        ArrayList<String> nodeList = new ArrayList<>(Arrays.asList("node1.6dist;1523;172.0.0.1;Online","node2.6dist;1523;172.0.0.2;Online","node3.6dist;1523;172.0.0.3;Offline","node4.6dist;1523;172.0.0.4;Offline"));
        setNodes(nodeList);
        ArrayList<String> fileList = new ArrayList<>(Arrays.asList("1;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","2;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","3;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","4;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","5;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","6;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","7;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","8;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","9;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","10;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","11;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","12;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","13;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","1;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","2;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","3;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","4;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","5;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","6;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","7;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","8;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","9;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","10;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","11;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","12;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked","13;Portfolio_6Dist;node2.6dist;node4.6dist;Unlocked"));
        setFiles(fileList);
        setNameServer("Online;2;Online;Online;Online;Online");
    }

    public ArrayList<String> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<String> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }
}
