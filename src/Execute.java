import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Execute
{
	public static void main(String[] argv)
    {
		new Execute();
	}
    
	public Execute()
    {
		JFrame frame = new JFrame("kipper");

		frame.getContentPane().setLayout(new BorderLayout());

		OuterSpacePanel bg = new OuterSpacePanel();
		frame.addKeyListener(bg);
		frame.getContentPane().add((JPanel)bg);
		frame.getContentPane().add(bg.getBottomPanel(),BorderLayout.NORTH);

		frame.pack();
		frame.setLocation(300,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
