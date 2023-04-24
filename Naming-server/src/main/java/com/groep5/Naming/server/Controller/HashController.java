package com.groep5.Naming.server.Controller;

import com.groep5.Naming.server.Service.SHAHasher;
import com.groep5.Naming.server.Service.Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HashController {

    @Autowired
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

    @PutMapping("/node/{name}")//add a node (with address) and receive hash id
    public int addNode(@PathVariable String name, @RequestBody String strAddress) throws UnknownHostException {
        return hasher.addNode(name,strAddress);
    }

    @DeleteMapping("/node/{name}")//delete a node
    public void deleteNodeByAddress(@PathVariable String name,@RequestBody String strAddress) throws UnknownHostException {
        hasher.deleteNode(name);
    }

    @GetMapping("/hash/{name}")
    public int calcHashValue(@PathVariable String name)
    {
        return hasher.calcHashId(name);
    }
    @GetMapping("/hash/{name}")
    public int calcHashValueFromPath(@PathVariable String name)
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
