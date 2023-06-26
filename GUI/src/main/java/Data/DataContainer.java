package Data;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class DataContainer {
    public DataContainer() throws IOException {}

    public String getNode(String socket) throws IOException {
        int i = Integer.parseInt(socket) - 8080;
        String nodeInfo = "node" + i + ".6dist;/;/;/;/;Offline";
        try {
            nodeInfo = WebClient.create("http://" + InetAddress.getByName("localhost").getHostAddress() + ":" + socket+1)
                    .get()
                    .uri("/getNodeInfo")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientRequestException ignored) {
        }
        return nodeInfo;
    }

    public String getFiles() {
        String files = "";
        int nodeSocket = 8081;
        for (int i = 0;i<4;i++) {
            try {
                files = WebClient.create("http://" + InetAddress.getByName("localhost").getHostAddress() + ":" + nodeSocket++)
                        .get()
                        .uri("/getFiles")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }
            catch (WebClientRequestException | WebClientResponseException | UnknownHostException ignored) {
            }
        }
        return files;
    }

    public String getNameServer(String socket) throws IOException {
        int numberOfNodes = 0;
        String nameServerInfo = "Offline";
        try {
            nameServerInfo = WebClient.create("http://" + InetAddress.getByName("localhost").getHostAddress() + ":" + socket)
                    .get()
                    .uri("/status")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        catch (WebClientRequestException ignored) {
        }
        String nodeInfo = null;
        int nodeSocket = 8082;


        for (int i = 0;i<5;i++) {
            nodeInfo = null;
            try {
                nodeInfo = WebClient.create("http://" + InetAddress.getByName("localhost").getHostAddress() + ":" + nodeSocket++)
                        .get()
                        .uri("/getGUIInfo")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }
            catch (WebClientRequestException ignored) {
            }
            if (nodeInfo != null) {
                numberOfNodes++;
            }
        }
        return nameServerInfo + ";" + numberOfNodes;
    }
}
