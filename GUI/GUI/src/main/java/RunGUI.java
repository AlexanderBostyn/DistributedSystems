import MainPanel.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class RunGUI {
    public static void main(String args[]) throws IOException  //static method
    {
        MainFrame mf = new MainFrame();
        mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
