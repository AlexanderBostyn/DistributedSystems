package Home.Nodes;

import Data.DataContainer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class NodesList extends JPanel {
    public NodesList() throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/2 - 50);
        setOpaque(false);

        setLayout(new GridLayout(5,1, 0,5));

        NodeCellFactory ncf = new NodeCellFactory();
        add(ncf.createFirstCell());
        DataContainer dataContainer = new DataContainer();
        int socket = 8080;
        for (int i = 0; i<4; i++) {
            String s = dataContainer.getNode(String.valueOf(socket));
            String[] strings = s.split(";");
            add(ncf.createCell(strings[0], strings[1], strings[2], strings[5]));
            socket++;
        }
    }
}
