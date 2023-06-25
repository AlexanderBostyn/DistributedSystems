package com.groep5.Node.Controller;

import com.groep5.Node.Agents.FailureAgent;
import com.groep5.Node.Agents.FailureAgentGetDTO;
import com.groep5.Node.Agents.SyncAgent;
import com.groep5.Node.Model.Node;
import com.groep5.Node.Model.NodePropreties;
import com.groep5.Node.NodeApplication;
import com.groep5.Node.Service.NamingServerService;
import com.groep5.Node.Service.NodeLifeCycle.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class NodeController {
    @Autowired
    private  Node node;
    @Autowired
    private NodePropreties nodePropreties;
    @Autowired
    NamingServerService namingServerService;
    @Autowired
    private SyncAgent syncAgent;
    @Autowired
    private DiscoveryService discoveryService;
    private String nodeName;
    private  final Logger logger = Logger.getLogger(this.getClass().getName());

    public void setNodeName(String name){
        nodeName=name;
    }
    @RequestMapping({"/","/home"})
    @ResponseBody
    public String showHomepage(){
        logger.info("incoming request /home");
        return "Hello World, "+nodeName+ " here";
    }

    @PutMapping("/shutdown")//shutdown
    public ResponseEntity<String> shutdownNode() throws IOException {
        logger.info("incoming PUT /shutdown");
        if (nodePropreties.isActive()){
            logger.info("shutting down node: "+nodeName);
            syncAgent.setActive(false);
            discoveryService.setActive(false);
            nodePropreties.setActive(false);
            node.shutdownNode();
            return new  ResponseEntity<String>("shutting down "+nodeName, HttpStatus.OK);
        }else{
            return new  ResponseEntity<String>(nodeName+ " is not active", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/activate")
    public ResponseEntity<String> activateNode() throws UnknownHostException {
        logger.info("incoming PUT /activate");
        //start Node object here
        if (!nodePropreties.isActive()){
            logger.info("Starting node: "+nodeName);
            nodePropreties.setActive(true);
            syncAgent.setActive(true);
            discoveryService.setActive(true);
            //syncAgent.startSyncAgent();
            return new  ResponseEntity<String>("activated "+nodeName, HttpStatus.OK);
        }else{
            return new  ResponseEntity<String>(nodeName+" is already active", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/status")
    public String getStatus(){
        logger.info("incoming GET /status");
        return "ok";//return ok or fail
    }


    @PutMapping("/failureAgent")//receive agent from next node
    public void startFailureAgent(HttpEntity<FailureAgentGetDTO> request) throws IOException {
        logger.info("incoming PUT /failureAgent");
        FailureAgentGetDTO dto = request.getBody();
        FailureAgent failureAgent = new FailureAgent(dto.getFailingNodeHash(),dto.isNewAgent());
        failureAgent.run();
    }

    @GetMapping("/getNodeInfo")
    public String getNodeInfo()//nodeName,nodeHash,nodeAddress,prevNode,nextNode
    {
        logger.info("incoming GET /getNodeInfo");
        String info="";
        info+=nodePropreties.getNodeName()+";";
        info+= nodePropreties.getNodeHash()+";";
        info+= nodePropreties.getNodeAddress()+";";
        info+= nodePropreties.getPreviousHash()+";";
        info+= nodePropreties.getNextHash();
        return info;
    }

    @GetMapping("/getGUIInfo")
    public String getGUIInfo()//nodeName,nodeHash,nodeAddress, prevNode,nextNode
    {
        logger.info("incoming GET /getGUIInfo");
        String info="";
        info+=nodePropreties.getNodeName()+";";
        info+= nodePropreties.getNodeHash()+";";
        info+= nodePropreties.getNodeAddress()+";";
        info+= nodePropreties.getPreviousHash()+";";
        info+= nodePropreties.getNextHash()+";";
        info+= "Online";
        return info;
    }


    @GetMapping("/agentlist")
    public ResponseEntity<Map<String, Boolean>> getAgentList()
    {
        logger.info("incoming GET /agentlist");
        HashMap<String,Boolean> agentlist=syncAgent.getAgentList();
        return new  ResponseEntity<Map<String,Boolean>>(agentlist, HttpStatus.OK);
    }

    @GetMapping("/getFiles")
    public String testgetAgentList()
    {
        logger.info("incoming test GET /agentlist");
        HashMap<String,Boolean> agentlist=syncAgent.getAgentList();
        System.out.println(agentlist);
        agentlist.put("1;testfile1;node1.6dist;node2.6dist", true);
        agentlist.put("20;testfile2;node3.6dist;node4.6dist", false);
        String files = "";
        for(String s : agentlist.keySet()) {
            String file = s + ";" + agentlist.get(s);
            files = files + file + ":";
        }
        files = files.substring(0, files.length() - 1);
        System.out.println(files);
        return files;
    }

    @GetMapping("/log")//returns log in serialized form
    public String getLog(){
        logger.info("incoming GET /log");
        return NodeApplication.getLog().toString();
    }
}
