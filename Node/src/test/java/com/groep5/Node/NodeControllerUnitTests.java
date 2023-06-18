package com.groep5.Node;

import com.groep5.Node.Agents.SyncAgent;
import com.groep5.Node.Controller.NodeController;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class NodeControllerUnitTests {
    @Autowired
    NodeController nodeController;

    @MockBean
    SyncAgent syncAgent;

    @BeforeEach
    public void init(){
        
    }
}
