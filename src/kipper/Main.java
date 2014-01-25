package kipper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main
{
    public static void main(String[] argv)
    {
        JFrame frame = new JFrame("kipper");
        frame.getContentPane().setLayout(new BorderLayout());

        BottomPanel statusBar = new BottomPanel();
        OuterSpacePanel gamePanel = new OuterSpacePanel(statusBar);
        frame.addKeyListener(gamePanel);
        frame.addComponentListener(new RepaintAfterResize(gamePanel));
        frame.getContentPane().add(gamePanel);
        frame.getContentPane().add(statusBar, BorderLayout.NORTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);

        new Thread(gamePanel).start();
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
