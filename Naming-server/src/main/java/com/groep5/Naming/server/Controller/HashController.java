package com.groep5.Naming.server.Controller;

import com.groep5.Naming.server.Model.Node;
import com.groep5.Naming.server.Persistence;
import com.groep5.Naming.server.Service.SHAHasher;
import com.groep5.Naming.server.Service.Hasher;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
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
    private boolean printRequests=true;
    HashController(ApplicationContext context) {
        this.hasher = new SHAHasher(context);
    }


    @RequestMapping({"/","/home"})
    @ResponseBody
    public String showHomepage(){
        if (printRequests){
            logger.info("incoming request /home");}
        return "Hello World, naming server here";
    }

    @PutMapping("/setPrintRequests/{bool}")
    public ResponseEntity<String> setPrintRequests(@PathVariable int bool){
        if (bool==0){
            printRequests=false;
        } else if (bool==1) {
            printRequests=true;
        }
        return new ResponseEntity<String>("printRequests set to "+bool, HttpStatus.OK);
    }

    @GetMapping("/status")
    public String returnStatus() {
        logger.info("incoming GET /status");
        return "Online";
    }

    @GetMapping("/file/{id}")//locate the node a file is located at
    public String locateFileById(@PathVariable int id) throws UnknownHostException {
        String location=hasher.locateFileById(id).getHostAddress();
        if (printRequests){
            logger.info("incoming GET /file/"+id);
            logger.info("file "+id+" owner is: "+location);
        }
        return location;
    }
    @GetMapping("/file")//locate the node a file is located at
    public String  locateFileByName(@RequestBody String name) throws UnknownHostException {
        if (printRequests){
            logger.info("incoming GET /file");
        }
        return hasher.locateFileByName(name).getHostAddress();
    }

    @DeleteMapping("/file")
    public String locateFileByNameAndRemoveNode(@RequestBody String name) throws UnknownHostException {
        if (printRequests){
            logger.info("incoming DELETE /file");
        }
        InetAddress nodeAddress = hasher.locateFileByName(name);
        hasher.deleteNodeByAddress(nodeAddress);
        return nodeAddress.toString();
    }

    @GetMapping("/node/{id}")
    public ResponseEntity<String> locateNodeById(@PathVariable int id) {
        if (printRequests){
            logger.info("incoming GET /node/"+id);
        }
        InetAddress address = hasher.locateNodeById(id);
        if (address == null) {
            return new ResponseEntity<String>("Node Not Found", HttpStatusCode.valueOf(404));
        }
        return new ResponseEntity<String>(address.getHostAddress(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/node/{id}/previous")
    public int getPreviousNodeId(@PathVariable int id) {
        if (printRequests){
            logger.info("incoming GET /node/"+id+"/previous");
        }
        return hasher.previousId(id);
    }

    @GetMapping("node/{id}/next")
    public int getNextNodeId(@PathVariable int id) {
        if (printRequests){
            logger.info("incoming GET /node/"+id+"/next");
        }
        return hasher.nextId(id);
    }

    @PostMapping("/node/{name}")//add a node (with address) and receive hash id
    public int addNode(@PathVariable String name, @RequestBody String strAddress) throws UnknownHostException {
        if (printRequests){
            logger.info("incoming POST /node/"+name);
        }
        logger.info("Added Node:" + name);
        return hasher.addNode(name,strAddress);
    }

    @DeleteMapping("/node/{id}")//delete a node
    public void deleteNodeByAddress(@PathVariable int id) throws UnknownHostException {
        if (printRequests){
            logger.info("incoming DELETE /node/"+id);
        }
        logger.info("deleted Node with Id: " + id );
        hasher.deleteNode(id);
    }

    @GetMapping("/hash/{name}")
    public int calcHashValue(@PathVariable String name)
    {
        int hashId=hasher.calcHashId(name);
        if (printRequests){
            logger.info("incoming GET /hash/"+name);
            logger.info(("hash for "+name+" is: "+hashId));
        }

        return hashId;
    }

    @GetMapping("/nodes")
    public String getAllNodes()
    {
        return hasher.getAllNodes();
    }


    @GetMapping("/discovery")
    public int returnAmountOfNodes(@RequestBody String name, @RequestBody String strAddress) throws UnknownHostException {
        if (printRequests){
            logger.info("incoming GET /discovery");
        }
        hasher.addNode(name, strAddress);
        int size = hasher.returnAmountOfNodes();
        //multicast networksize to other nodes
        return size;
    }

}
