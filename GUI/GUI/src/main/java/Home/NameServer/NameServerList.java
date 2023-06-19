package Home.NameServer;

import Data.DataContainer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class NameServerList extends JPanel {
    public NameServerList() throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,50,screenSize.width, screenSize.height/4 - 50);
        setOpaque(false);

        setLayout(new GridLayout(6,1, 0,5));

        NameServerCellFactory nscf = new NameServerCellFactory();

        DataContainer dataContainer = new DataContainer();
        String s = dataContainer.getNameServer();

        if(s != null) {
            add(nscf.createCell("Status", s));
            add(nscf.createCell("Nodes", "0"));
            add(nscf.createCell("Discovery", s));
            add(nscf.createCell("Replication", s));
            add(nscf.createCell("Sync Agent", s));
            add(nscf.createCell("Failure Agent", s));
        }
        else {
            add(nscf.createCell("Status", "Offline"));
            add(nscf.createCell("Nodes", "0"));
            add(nscf.createCell("Discovery", "Offline"));
            add(nscf.createCell("Replication", "Offline"));
            add(nscf.createCell("Sync Agent", "Offline"));
            add(nscf.createCell("Failure Agent", "Offline"));
        }
    }
}
