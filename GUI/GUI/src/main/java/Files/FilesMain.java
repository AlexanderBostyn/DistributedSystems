package Files;

import Home.NameServer.NameServerMain;
import Home.Nodes.NodesMain;

import javax.swing.*;
import java.awt.*;

public class FilesMain extends JPanel {
    public FilesMain() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        setBackground(new Color(0, 20, 75));
        setOpaque(true);
        setLayout(null);
        add(FilesPanel());
    }

    public JPanel FilesPanel() {
        JPanel fPanel = new JPanel();
        fPanel.setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fPanel.setBounds(100, 25, (screenSize.width-450) + 50, 3*screenSize.height/4 + 50);
        fPanel.setBackground(new Color(0, 15, 30));
        fPanel.add(new FilesTitle());
        fPanel.add(new FilesCardPanel());

        return fPanel;
    }
}
