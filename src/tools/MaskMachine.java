package kipper.tools;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import kipper.*;

// This utility generates the code for a "mask" used in collision detection
// The code goes in your Ship object
// To load an image of your ship in this utility, rename the image "target.gif"
//     and it must be the same directory as this file
// Important to have an accurate mask, but not too many points to keep speeds enjoyable
// I could make this better but it would work just as well; I wrote this in a few minutes

public class MaskMachine {

	JFrame frame;
	DrawPanel drawpanel;
	JTextArea txt;
	JButton btn;

	public MaskMachine(){
		frame = new JFrame("masking madness");
		GridBagLayout layout = new GridBagLayout();
		frame.setLayout(layout);

		GridBagConstraints gc = new GridBagConstraints();
		gc.gridwidth=1;
		gc.gridheight=1;
		gc.weightx=0;
		gc.weighty=0;

		gc.gridx=0;
		gc.gridy=1;
		txt = new JTextArea("");
		txt.setRows(10);
		txt.setColumns(50);
		layout.setConstraints(txt,gc);
		frame.add(txt);

		gc.gridx=1;
		gc.gridy=1;
		btn = new JButton("clear");
		btn.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					txt.setText("");
					drawpanel.p=new Polygon();
					drawpanel.repaint();
				}
			}
		);
		layout.setConstraints(btn,gc);
		frame.add(btn);

		gc.gridx=0;
		gc.gridy=0;
		gc.gridwidth=2;
		gc.weightx=1;
		gc.weighty=1;
		drawpanel = new DrawPanel(txt);
		layout.setConstraints(drawpanel,gc);
		frame.add(drawpanel);

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public static void main(String[] argv){
		new MaskMachine();
	}
}

class DrawPanel extends JPanel implements MouseListener {
	Image img;
	Polygon p;
	JTextArea txt;
	int factor = 10;
	int w = -1, h = -1;

	public DrawPanel(JTextArea t){
		addMouseListener(this);

		img = Util.instance.loadImage("assets/target.gif");
		p=new Polygon();
		this.txt=t;
	}
	public Dimension getMinimumSize(){
		return new Dimension(800,600);
	}
	public Dimension getPreferredSize(){
		return new Dimension(800,600);
	}
	public void paint(Graphics g){
		super.paint(g);

		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(Color.decode("#dddddd"));
		for(int x=0;x<getWidth();x+=factor/2)
			g.drawLine(x,0,x,getHeight());
		for(int y=0;y<getHeight();y+=factor/2)
			g.drawLine(0,y,getWidth(),y);

		if(w<=-1){
			w = img.getWidth(this)*factor;
			h = img.getHeight(this)*factor;
		}

		g.drawImage(img, getWidth()/2-w/2, getHeight()/2-h/2, w, h, this);

		g.setColor(Color.GRAY);
		g.drawPolygon(p);
		g.setColor(Color.BLACK);
		for(int i=0;i<p.npoints;i++)
			g.drawOval(p.xpoints[i]-10,p.ypoints[i]-10,20,20);
	}
	public void mousePressed(MouseEvent evt){
		//if(evt.getButton()==MouseEvent.BUTTON3)
		p.addPoint(evt.getX(), evt.getY());
	}
	public void mouseReleased(MouseEvent e){
		// mask output
		String str=new String();
		str+="// ** generated with MaskMachine ** //\n";
		str+="public Polygon getDefaultMask(){\n";
		str+="\treturn new Polygon(\n";
		// x values
		str+="\t\tnew int[]{";
		for(int i=0;i<p.npoints;i++){
			str+=(p.xpoints[i]-getWidth()/2+w/2)/factor;
			if(i+1<p.npoints) str+=",";
		}
		str+="},\n";
		// y values
		str+="\t\tnew int[]{";
		for(int i=0;i<p.npoints;i++){
			str+=(p.ypoints[i]-getHeight()/2+h/2)/factor;
			if(i+1<p.npoints) str+=",";
		}
		str+="},\n";
		str+="\t\t"+p.npoints+"\n";
		str+="\t);\n";
		str+="}";


		txt.setText(str);

		repaint();
	}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
}