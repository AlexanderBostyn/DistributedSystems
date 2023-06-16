package Data;

import java.util.ArrayList;

public class DataContainer {
    public ArrayList<NodeData> nodes;
    public ArrayList<FileData> files;

    public DataContainer() {
    }

    public ArrayList<NodeData> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<NodeData> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<FileData> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileData> files) {
        this.files = files;
    }
}
