package Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

public class DataContainer {
    private ServerSocket socket;
    public ArrayList<String> nodes;
    public ArrayList<String> files;
    public String nameServer;

    public DataContainer() throws IOException {
        nodes = new ArrayList<String>();
        for(int i = 1;i<5;i++) {
            String defaultNode = "node" + i + ".6dist;" + i + ";172.0.0."+ i + ";Offline";
            nodes.add(defaultNode);
        }
        files = new ArrayList<>();;
    }

    public ArrayList<String> getNodes() throws IOException {
        return new ArrayList<>();

        /*this.socket = new ServerSocket(8080);
        String address = String.valueOf(socket.getInetAddress());
        return WebClient.create("http://" + address + ":8080")
                .get()
                .uri("/status")
                .retrieve()
                .bodyToMono(String.class)
                .block();*/
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public String getNameServer() throws IOException {
        this.socket = new ServerSocket(54321);
        String address = String.valueOf(socket.getInetAddress());
        return WebClient.create("http://" + address + ":54321")
                .get()
                .uri("/status")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
