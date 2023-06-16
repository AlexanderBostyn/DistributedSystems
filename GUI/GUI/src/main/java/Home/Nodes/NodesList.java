package Home.Nodes;

import javax.swing.*;
import java.awt.*;

public class NodesList extends JPanel {
    public NodesList() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/2 - 50);
        setOpaque(false);

        setLayout(new GridLayout(5,1, 0,5));

        NodeCellFactory ncf = new NodeCellFactory();

        add(ncf.createFirstCell());
        add(ncf.createCell("Name1", "15", "172.0.0.13", "Online"));
        add(ncf.createCell("Name2", "179", "172.0.0.138", "Offline"));
        add(ncf.createCell("Name3", "45165", "172.0.5.13", "Online"));
        add(ncf.createCell("Name4", "1465", "172.45.0.13", "Online"));
    }
}
