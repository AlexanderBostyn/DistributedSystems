package com.groep5.Node.Controller;

import com.groep5.Node.Agents.SyncAgent;
import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {
    @Autowired
    private  Node node;
    @Autowired
    private NodePropreties nodePropreties;

    @Autowired
    private SyncAgent syncAgent;

    @PutMapping("/shutdown")//shutdown
    public void shutdownNode() throws IOException {
        node.shutdownNode();
    }

    @GetMapping("/agentlist")
    public ResponseEntity<Map<String, Boolean>> getAgentList()
    {
        HashMap<String,Boolean> agentlist=syncAgent.getAgentList();
        return new  ResponseEntity<Map<String,Boolean>>(agentlist, HttpStatus.OK);

    }



    @GetMapping("/log")//returns log in serialized form
    public String getLog(){
        return NodeApplication.getLog().toString();
    }
}
