package com.groep5.Node;

import com.groep5.Node.Agents.FailureAgentGetDTO;
import com.groep5.Node.Controller.NodeController;
import com.groep5.Node.Service.Unicast.UnicastSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class FailureAgentTests {
    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    UnicastSender unicastSender;

    @Test
    public void testSendAgent1() throws UnknownHostException {
        FailureAgentGetDTO dto = new FailureAgentGetDTO(1,false);
        //System.out.println(InetAddress.getByName("localhost").getHostAddress());
        //UnicastSender.sendFailureAgent(InetAddress.getByName("localhost").getHostAddress(),dto);
        //WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + 8080).build();

        webTestClient.post()
                .uri("/failureAgentTest")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    // Assert or perform further checks on the response body
                    System.out.println(responseBody);
                });
    }
    //run this method when a local NodeApplication is running, uncomment the @Test
    //@Test
    public void testSendAgent2() throws UnknownHostException {
        FailureAgentGetDTO dto = new FailureAgentGetDTO(1, false);
        System.out.println(InetAddress.getByName("localhost").getHostAddress());
        UnicastSender.sendFailureAgent(InetAddress.getByName("localhost").getHostAddress(), dto);
    }

}
