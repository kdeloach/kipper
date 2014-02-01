package kipper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main
{
    public static void main(String[] argv)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("kipper");
                Container pane = frame.getContentPane();
                OuterSpacePanel gamePanel = new OuterSpacePanel();
                frame.getContentPane().add(gamePanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(true);
                frame.setVisible(true);
                new Thread(gamePanel).start();
            }
        });
    }
}
