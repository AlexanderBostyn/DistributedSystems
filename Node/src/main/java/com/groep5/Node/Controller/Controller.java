package com.groep5.Node.Controller;

import com.groep5.Node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.UnknownHostException;

@RestController
public class Controller {

    private final Node node;

    public Controller(Node node) {
        this.node = node;
    }

    @PutMapping("/shutdown")//shutdown
    public void shtudown() throws IOException {
        node.shutdown();
    }
}
