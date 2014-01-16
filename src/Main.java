package kipper;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Main
{
	public static void main(String[] argv)
    {
		JFrame frame = new JFrame("kipper");
		frame.getContentPane().setLayout(new BorderLayout());

        BottomPanel statusBar = new BottomPanel();
		OuterSpacePanel gamePanel = new OuterSpacePanel(statusBar);
		frame.addKeyListener(gamePanel);
		frame.getContentPane().add(gamePanel);
		frame.getContentPane().add(statusBar, BorderLayout.NORTH);

		frame.pack();
		frame.setLocation(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
