package com.groep5.Node.Controller;

import com.groep5.Node.Model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {
    @Autowired
    private  Node node;

    @PutMapping("/shutdown")//shutdown
    public void shutdownNode() throws IOException {
        node.shutdownNode();
    }
}
