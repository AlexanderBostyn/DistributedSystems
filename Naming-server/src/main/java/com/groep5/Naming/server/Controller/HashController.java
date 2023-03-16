package com.groep5.Naming.server.Controller;

import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@RestController
public class HashController {
    @RequestMapping({"/","/home"})
    @ResponseBody
    public String showHomepage(){
        return "Hello World";
    }

    @GetMapping("/file/{id}")//locate the node a file is located at
    public InetAddress locateFileById(@PathVariable int id) throws UnknownHostException {
        return hashLocateFileNode(id);
    }
    @GetMapping("/file")//locate the node a file is located at
    public InetAddress locateFileByName(@RequestBody String name) throws UnknownHostException {
       // fileId=getfileId(name)
        int fileId=123;
        return hashLocateFileNode(fileId);
    }

    @PutMapping("/node")//add a node (with address) and receive hash id
    public int addNode(@RequestBody String strAddress) throws UnknownHostException {
        InetAddress address= InetAddress.getByName(strAddress);
        //add to node list
        //calc hash id for node
        //return hash id
        int hashId = 123;
        return hashId;
    }

    @DeleteMapping("/node")//delete a node
    public void deleteNodeByAddress(@RequestBody String strAddress) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(strAddress);
        //delete node from map
    }

    @GetMapping("/hash")
    public int calcHashValue(@RequestBody String name)
    {
        int hashVal=dummyHashValue(name);
        return hashVal;
    }

    @GetMapping("/hashtest")
    public String testHash(@RequestBody String name)
    {
        String hashVal=test(name);
        return hashVal;
    }

    public int dummyHashValue(String name)
    {
        return name.length()*2;
    }
    public String test(String name)
    {
        return name+"test";
    }
    public InetAddress hashLocateFileNode(int fileId) throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return  addr;
    }
}
