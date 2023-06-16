package Nodes;

import Home.Nodes.NodesList;
import Home.Nodes.NodesTitle;
import MainPanel.MainPanel;
import Nodes.Node.NodeButtons;
import Nodes.Node.NodeList;
import Nodes.Node.NodeTitle;

import javax.swing.*;
import java.awt.*;

public class NodeMain extends JPanel{
    public MainPanel mainPanel;
        public NodeMain(MainPanel mp) {
            mainPanel = mp;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(0, 0, screenSize.width, screenSize.height);
            setBackground(new Color(0, 20, 75));
            setOpaque(true);
            setLayout(null);
            for(int i = 1;i<5;i++) {
                add(createNode(i));
            }
        }

        private JPanel createNode(int i) {
            JPanel nodePanel = new JPanel();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width - 200;
            nodePanel.setBounds(50 + ((i-1) * ((width - 250)/4 + 50)), 250, (width - 250)/4, screenSize.height/3);
            nodePanel.setBackground(new Color(0, 15, 30));
            nodePanel.setLayout(null);
            nodePanel.add(new NodeTitle("node" + i + ".6dist"));
            nodePanel.add(new NodeButtons(mainPanel));
            nodePanel.add(new NodeList());

            return nodePanel;
        }
}
