package com.groep5.Naming.server.Controller;

import com.groep5.Naming.server.Service.SHAHasher;
import com.groep5.Naming.server.Service.Hasher;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

@RestController
public class HashController {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Hasher hasher;
    HashController(ApplicationContext context) {
        this.hasher = new SHAHasher(context);
    }


    @RequestMapping({"/","/home"})
    @ResponseBody
    public String showHomepage(){
        return "Hello World";
    }

    @GetMapping("/file/{id}")//locate the node a file is located at
    public String locateFileById(@PathVariable int id) throws UnknownHostException {
        return hasher.locateFileById(id).getHostAddress();
    }
    @GetMapping("/file")//locate the node a file is located at
    public String  locateFileByName(@RequestBody String name) throws UnknownHostException {

        return hasher.locateFileByName(name).getHostAddress();
    }

    @DeleteMapping("/file")
    public String locateFileByNameAndRemoveNode(@RequestBody String name) throws UnknownHostException {
        InetAddress nodeAddress = hasher.locateFileByName(name);
        hasher.deleteNodeByAddress(nodeAddress);
        return nodeAddress.toString();
    }

    @GetMapping("/node/{id}")
    public ResponseEntity<String> locateNodeById(@PathVariable int id) {
        InetAddress address = hasher.locateNodeById(id);
        if (address == null) {
            return new ResponseEntity<String>("Node Not Found", HttpStatusCode.valueOf(404));
        }
        return new ResponseEntity<String>(address.getHostAddress(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/node/{id}/previous")
    public int getPreviousNodeId(@PathVariable int id) {
        return hasher.previousId(id);
    }

    @GetMapping("node/{id}/next")
    public int getNextNodeId(@PathVariable int id) {
        return hasher.nextId(id);
    }

    @PostMapping("/node/{name}")//add a node (with address) and receive hash id
    public int addNode(@PathVariable String name, @RequestBody String strAddress) throws UnknownHostException {
        logger.info("Added Node:" + name);
        return hasher.addNode(name,strAddress);
    }

    @DeleteMapping("/node/{id}")//delete a node
    public void deleteNodeByAddress(@PathVariable int id) throws UnknownHostException {
        logger.info("deleted Node with Id: " + id );
        hasher.deleteNode(id);
    }

    @GetMapping("/hash/{name}")
    public int calcHashValue(@PathVariable String name)
    {
        return hasher.calcHashId(name);
    }

    /*@GetMapping("/hashtest")
    public String testHash(@RequestBody String name)
    {
        String hashVal=test(name);
        return hashVal;
    }*/

    @GetMapping("/discovery")
    public int returnAmountOfNodes(@RequestBody String name, @RequestBody String strAddress) throws UnknownHostException {
        hasher.addNode(name, strAddress);
        int size = hasher.returnAmountOfNodes();
        //multicast networksize to other nodes
        return size;
    }


    public String test(String name)
    {
        return name+"test";
    }


}
