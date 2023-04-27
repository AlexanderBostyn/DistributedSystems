package com.groep5.Node.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@RestController
public class Controller {
    @PutMapping("/shutdown")//shutdown
    public void shtudown() throws UnknownHostException {
        //node.shutodwn
    }
}
