import MainPanel.MainFrame;

import javax.swing.*;

public class RunGUI {
    public static void main(String args[])  //static method
    {
        MainFrame mf = new MainFrame();
        mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
