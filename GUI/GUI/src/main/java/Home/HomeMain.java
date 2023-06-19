package Home;

import Home.Files.FilesMain;
import Home.NameServer.NameServerMain;
import Home.Nodes.NodesMain;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HomeMain extends JPanel {
    public HomeMain() throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        setBackground(new Color(0, 20, 75));
        setOpaque(true);
        setLayout(null);

        add(new NodesMain());
        add(new FilesMain());
        add(new NameServerMain());
    }
}
