package com.groep5.Node.Controller;

import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private  Node node;
    @Autowired
    private NodePropreties nodePropreties;

    @PutMapping("/shutdown")//shutdown
    public void shutdownNode() throws IOException {
        node.shutdownNode();
    }

    @GetMapping("/log")//returns log in serialized form
    public String getLog(){
        return NodeApplication.getLog().toString();
    }
}
