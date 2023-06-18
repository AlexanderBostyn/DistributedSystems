package Data;

import java.util.ArrayList;

public class DataContainer {
    public ArrayList<String> nodes;
    public ArrayList<String> files;
    public String nameServer;

    public DataContainer() {
        nodes = new ArrayList<String>();
        for(int i = 1;i<5;i++) {
            String defaultNode = "node" + i + ".6dist;" + i + ";172.0.0."+ i + ";Offline";
            nodes.add(defaultNode);
        }
        files = new ArrayList<>();
        //String defaultFile = "1;Portfolio_dist6;node1.6dist;node2.6dist;Unlocked";
        //files.add(defaultFile);
        nameServer = "Offline;0;Offline;Offline;Offline;Offline";
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

    public void setNameServer(String namingServer) {
        this.nameServer = namingServer;
    }
}
