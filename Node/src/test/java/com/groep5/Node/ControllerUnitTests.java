package com.groep5.Node;

import com.groep5.Node.Agents.SyncAgent;
import com.groep5.Node.Controller.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ControllerUnitTests {
    @Autowired
    Controller controller;

    @MockBean
    SyncAgent syncAgent;

    @BeforeEach
    public void init(){
        
    }
}
