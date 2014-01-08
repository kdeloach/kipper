import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ConcurrentModificationException;
import java.util.ArrayList;

// This has to be a general panel for the entire game
// Change BottomPanel, make it apart of OuterSpacePanel
// Should I have frame update it's components or have have one component
// Level1Screen'
// Screen object is added to the frame on loadup
// When all enemies are dead, show the
//

// this is our outerspacepanel, static
// this won't change...it'll load different scenes

public class OuterSpacePanel extends JPanel implements KeyListener, Runnable {

	// window dimensions
	final static int WIDTH  = 800;
	final static int HEIGHT = 400;

	// amount of stars to have in the foreground and background
	Point[] starsbg = new Point[500];
	Point[] starsfg = new Point[25];

	Ship player1;
	ArrayList<Ship> players;

	// current scene, 1 at a time baby
	Scene scene;

	// keep track of all bullets fired so they still exist
	// after enemy kicks the bucket
	ArrayList<Projectile> bulletList;
	ArrayList<Explosion> explosionList;
	//ArrayList<Ship> playerList;

	// current bullet id,used to ID bullets
	int bulletId=0, explosionId=0, playersId=0;

	public OuterSpacePanel(){
		setSize(getMinumumSize());

		// make some bg stars-static
		for(int i=0;i<starsbg.length;i++){
			starsbg[i]=new Point(Random.range(0,getWidth()),Random.range(0,getHeight()));
		}

		// make some fg stars
		// intially the same position as the bg stars
		for(int i=0;i<starsfg.length;i++){
			starsfg[i]=new Point(starsbg[i].x,starsbg[i].y);
		}

		bulletList = new ArrayList<Projectile>();
		explosionList = new ArrayList<Explosion>();
		players = new ArrayList<Ship>();

		/////////

		player1 = new Enterprise(100,100,this);

		////////

		// make those stars scroll
		new MarqueeStars(starsfg,this).start();

		// initial Scene -------
		// ---------------------
		changeScene(new DemoLevel(this));
		// ---------------------

		// paint thread
		new Thread(this).start();
	}
	public void run(){
		while(true){
			repaint();

			try { Thread.sleep(5); } catch(Exception ie){}
		}
	}
	public void paint(Graphics g){
		// bg
		g.setColor(Color.BLACK);
		g.fillRect(0,0,getWidth(),getHeight());

		// stars (bg-static)
		g.setColor(Color.GRAY);
		for(int i=0;i<starsbg.length;i++)
			g.drawOval(starsbg[i].x,starsbg[i].y,0,0);

		// stars fg
		g.setColor(Color.WHITE);
		for(int i=0;i<starsfg.length;i++)
			g.drawOval(starsfg[i].x,starsfg[i].y,2,2);

		// paint players
		try {
			for(Ship p : players){
				if(p.isAlive())
					p.draw(g);
			}
		} catch(ConcurrentModificationException ie){}

		// paint bullets
		try {
			for(Projectile b : bulletList){
					b.draw(g);
			}
		} catch(ConcurrentModificationException ie){}
		  catch(NullPointerException ie){ System.out.println("whered that bullet go"); }

		// paint explosion
		try {
			for(Explosion e : explosionList){
					e.draw(g);
			}
		} catch(ConcurrentModificationException ie){}

		// draw Scene elements if any need be
		scene.paint(g);
	}

    void changeScene(Scene s) {
        if (scene != null) {
            scene.destroyScene();
        }
        scene = s;
        scene.createScene();
    }

	/////////////////////////
	// Keyboard Controls

	public void keyTyped(KeyEvent evt){
		getPlayer().keyTyped(evt);
	}
	public void keyPressed(KeyEvent evt){
		// if P is pressed, pause game, etc.

		if(evt.getKeyCode()==KeyEvent.VK_P){
            if (scene.name() == "upgrade") {
                changeScene(new DemoLevel(this));
            } else {
                changeScene(new ShipUpgradeScreen(this));
            }
		}

		getPlayer().keyPressed(evt);
	}
	public void keyReleased(KeyEvent evt){
		getPlayer().keyReleased(evt);
	}

	////////////////////////

	public synchronized void registerProjectile(Projectile b){
		b.setId(bulletId++);
		bulletList.add(b);
	}
	public synchronized void unregisterProjectile(Projectile b){
		for(int i=0;i<bulletList.size();i++){
			if(bulletList.get(i).getId()==b.getId()){
				bulletList.get(i).setId(Const.UNREGISTERED);
				bulletList.remove(i);
				break;
			}
		}
	}

	public synchronized void registerExplosion(Explosion e){
		e.setId(explosionId++);
		explosionList.add(e);
	}
	public synchronized void unregisterExplosion(Explosion e){
		for(int i=0;i<explosionList.size();i++){
			if(explosionList.get(i).getId()==e.getId()){
				explosionList.get(i).setId(Const.UNREGISTERED);
				explosionList.remove(i);
				break;
			}
		}
	}

	public synchronized void registerShip(Ship e){
		e.setId(playersId++);
		players.add(e);
	}
	public synchronized void unregisterShip(Ship e){
		for(int i=0;i<players.size();i++){
			if(players.get(i).getId()==e.getId()){
				players.get(i).setId(Const.UNREGISTERED);
				players.remove(i);
				break;
			}
		}
	}
    public synchronized void killNPCs() {
        ArrayList<Ship> npcs = new ArrayList<Ship>();
        for (Ship player : players) {
            if (player != getPlayer()) {
                npcs.add(player);
            }
        }
        for (Ship npc : npcs) {
            npc.explode();
        }
    }

	// @Deprecated
	public synchronized Ship intersects(Projectile b){
		try {
			for(Ship s : players){
				if(b.intersects(s))
					return s;
			}
		} catch(ConcurrentModificationException ie){}
		return null;
	}
	public synchronized Ship intersects(Ship r){
		try {
			for(Ship p : players){
				// NOTE: a ship might want to know if an ally hits it, maybe to push him away?
				// add it to the listener sometime
				if(p.getId()!=r.getId() && p.getTeam()!=r.getTeam() && p.intersects((Destructable)r))
					return p;
			}
		} catch(ConcurrentModificationException ie){}
		return null;
	}

	///

	public Dimension getMinumumSize(){
		return getPreferredSize();
	}
	public Dimension getPreferredSize(){
		return new Dimension(OuterSpacePanel.WIDTH,OuterSpacePanel.HEIGHT);
	}
	public BottomPanel getBottomPanel(){
		return new BottomPanel(this);
	}
	public Ship getPlayer(){
		return player1;
	}

}