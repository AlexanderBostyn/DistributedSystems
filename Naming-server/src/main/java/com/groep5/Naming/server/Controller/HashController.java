package com.groep5.Naming.server.Controller;

import com.groep5.Naming.server.Service.DummyHasher;
import com.groep5.Naming.server.Service.Hasher;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@RestController
public class HashController {
    private Hasher hasher= new DummyHasher();
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

    @PutMapping("/node")//add a node (with address) and receive hash id
    public int addNode(@RequestBody String strAddress) throws UnknownHostException {
        return hasher.addNode(strAddress);
    }

    @DeleteMapping("/node")//delete a node
    public void deleteNodeByAddress(@RequestBody String strAddress) throws UnknownHostException {
        hasher.deleteNode(strAddress);
    }

    @GetMapping("/hash")
    public int calcHashValue(@RequestBody String name)
    {
        return hasher.calcHashId(name);
    }

    @GetMapping("/hashtest")
    public String testHash(@RequestBody String name)
    {
        String hashVal=test(name);
        return hashVal;
    }


    public String test(String name)
    {
        return name+"test";
    }



}
