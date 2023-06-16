package Home.NameServer;

import javax.swing.*;
import java.awt.*;

public class NameServerMain extends JPanel {
    public NameServerMain() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100 + (screenSize.width-450)/4, 25, screenSize.width-400 - (screenSize.width-450)/2, screenSize.height/4);
        setBackground(new Color(0, 15, 30));

        setLayout(null);
        add(new NameServerTitle());
        add(new NameServerList());
    }
}
