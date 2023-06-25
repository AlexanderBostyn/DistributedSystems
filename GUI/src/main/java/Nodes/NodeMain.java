package Nodes;

import Data.DataContainer;
import Home.Nodes.NodesList;
import Home.Nodes.NodesTitle;
import MainPanel.MainPanel;
import Nodes.Node.NodeButtons;
import Nodes.Node.NodeList;
import Nodes.Node.NodeTitle;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class NodeMain extends JPanel{
    public MainPanel mainPanel;
        public NodeMain(MainPanel mp, ArrayList<Boolean> status) throws IOException {
            mainPanel = mp;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(0, 0, screenSize.width, screenSize.height);
            setBackground(new Color(0, 20, 75));
            setOpaque(true);
            setLayout(null);

            DataContainer dataContainer = new DataContainer();
            int socket = 8080;
            for (int i = 1; i<5; i++) {
                String s = dataContainer.getNode(String.valueOf(socket));
                add(createNode(i, s, status));
                socket++;
            }
        }

        private JPanel createNode(int i, String s, ArrayList<Boolean> status) {
            JPanel nodePanel = new JPanel();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width - 200;
            nodePanel.setBounds(50 + ((i-1) * ((width - 250)/4 + 50)), 150, (width - 250)/4, screenSize.height/3 + 150);
            nodePanel.setBackground(new Color(0, 15, 30));
            nodePanel.setLayout(null);
            nodePanel.add(new NodeTitle("node" + i + ".6dist"));
            nodePanel.add(new NodeButtons(mainPanel, i, status));
            nodePanel.add(new NodeList(s));

            return nodePanel;
        }
}
