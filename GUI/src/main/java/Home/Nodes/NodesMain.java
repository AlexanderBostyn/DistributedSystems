package Home.Nodes;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class NodesMain extends JPanel {
    public NodesMain() throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 75 + screenSize.height/4, (screenSize.width-450)/2, screenSize.height/2);
        setBackground(new Color(0, 15, 30));

        setLayout(null);
        add(new NodesTitle());
        add(new NodesList());
    }
}