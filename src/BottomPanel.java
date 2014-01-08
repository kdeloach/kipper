package kipper;

import java.awt.*;
import javax.swing.*;
import java.util.*;

// BottomPanel really isn't a fitting name, should change to...StatusBar later or something

public class BottomPanel extends JPanel implements Runnable {
		OuterSpacePanel osp;

		Font bigFont = new Font("Lucida Console",Font.PLAIN,13);
		Font smallFont = new Font("Lucida Console",Font.PLAIN,11);

		public BottomPanel(OuterSpacePanel c){
			setSize(getMinumumSize());
			this.osp=c;

			new Thread(this).start();
		}

		public void run(){
			while(true){
				repaint();
				try{Thread.sleep(10);}catch(Exception ie){}
			}
		}

		public void drawBar(int x, int y, double perc, Color c, Graphics g){
			g.setColor(c);
			g.fillRect(x, y, (int)(75*perc), 8);
		}
		public void drawCooldown(int x, int y, Weapon w, Graphics g){
			if(w!=null){
				// if this weapon is selected
				if(w==w.ship.getWeapon()){
					g.drawRect(x-1,y-1,21,21);
				}
				g.drawImage(w.getIcon(), x, y, this);
				g.fillArc(x, y, 20, 20, 90, (int)( -360*w.percentCooled() ));
			}

		}

		public void paint(Graphics g){
			g.setColor(Color.BLACK);// change to #333333 maybe
			g.fillRect(0,0,getWidth(),getHeight());

			Ship player=null, target=null;

			try {
				player = osp.getPlayer();
				target = player.getTarget();
			} catch (NullPointerException ie){}

			if(player!=null && player.isAlive()){
				// text
				g.setColor(Color.GRAY);
				g.setFont(smallFont);
				g.drawString(player.getName(), 3, 9);
				g.drawString("life", 3, 21);
				g.drawString("exp", 3, 29);

				// bars
				drawBar(40, 13, player.percentHealth(), Color.GRAY, g);
				drawBar(40, 23, 1, Color.LIGHT_GRAY, g);

				g.setColor(Color.DARK_GRAY);
				g.drawLine(124, 12, 124, getHeight()-4);

				// cooldowns
				g.setColor(Color.GRAY);
				drawCooldown(135,          11, player.getWeapon(0), g);
				drawCooldown(135+(20+3)*1, 11, player.getWeapon(1), g);
				drawCooldown(135+(20+3)*2, 11, player.getWeapon(2), g);
				drawCooldown(135+(20+3)*3, 11, player.getWeapon(3), g);
				drawCooldown(135+(20+3)*4, 11, player.getWeapon(4), g);
			}

			if(target!=null && target.isAlive()){
				// offset
				int xoff=getWidth()-250;

				// text
				g.setColor(Color.GRAY);
				//g.setFont(smallFont);
				g.drawString(target.getName(), 3+xoff, 9);
				g.drawString("life", 3+xoff, 21);
				g.drawString("exp", 3+xoff, 29);

				// bars
				drawBar(40+xoff, 13, target.percentHealth(), Color.GRAY, g);
				drawBar(40+xoff, 23, 1, Color.LIGHT_GRAY, g);

				g.setColor(Color.DARK_GRAY);
				g.drawLine(124+xoff, 12, 124+xoff, getHeight()-4);

				// cooldowns
				g.setColor(Color.GRAY);
				drawCooldown(135+xoff,          11, target.getWeapon(0), g);
				drawCooldown(135+xoff+(20+3)*1, 11, target.getWeapon(1), g);
				drawCooldown(135+xoff+(20+3)*2, 11, target.getWeapon(2), g);
				drawCooldown(135+xoff+(20+3)*3, 11, target.getWeapon(3), g);
				drawCooldown(135+xoff+(20+3)*4, 11, target.getWeapon(4), g);
			}
		}



		public Dimension getMinumumSize(){
			return getPreferredSize();
		}
		public Dimension getPreferredSize(){
			return new Dimension(OuterSpacePanel.WIDTH,33);
		}
	}