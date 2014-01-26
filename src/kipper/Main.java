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
                frame.getContentPane().setLayout(new BorderLayout());

                OuterSpacePanel gamePanel = new OuterSpacePanel();
                frame.addKeyListener(gamePanel);
                frame.addComponentListener(new RepaintAfterResize(gamePanel));
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

class RepaintAfterResize extends ComponentAdapter
{
    JComponent component;

    public RepaintAfterResize(JComponent component)
    {
        this.component = component;
    }

    @Override
	public void componentResized(ComponentEvent e)
    {
        component.repaint();
    }
}
