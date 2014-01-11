package kipper;

import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ConcurrentModificationException;
import java.util.ArrayList;
import kipper.ships.*;
import kipper.effects.*;
import kipper.weapons.*;

public class OuterSpacePanel extends JPanel implements KeyListener, Runnable
{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 400;

    BottomPanel statusBar;
    MarqueeStars starsBg, starsFg;

	public Ship player1;

	// current scene
	Scene scene;

    // Never call clear() on these! They need to be unregistered first.
	private ArrayList<Ship> players;
    // XXX: Expose this to allow collisions with other bullets for spacemine
	public ArrayList<Projectile> bulletList;
	private ArrayList<Explosion> explosionList;

    // TODO: Get rid of this stuff
	int bulletId = 0;
    int explosionId = 0;
    int playersId = 0;

	public OuterSpacePanel(BottomPanel statusBar)
    {
        this.statusBar = statusBar;

		setSize(getMinumumSize());

		players = new ArrayList<Ship>();
		bulletList = new ArrayList<Projectile>();
		explosionList = new ArrayList<Explosion>();

        starsBg = new MarqueeStars(500, Math.toRadians(180), 0.05, 0, Color.GRAY);
        starsFg = new MarqueeStars(25, Math.toRadians(180), 0.099, 2, Color.WHITE);

		player1 = new Enterprise(100, 100, this);
		changeScene(new DemoLevel(this));

		new Thread(this).start();
	}

	public void run()
    {
		while (true) {
            update();
			repaint();
			try {
                Thread.sleep(5);
            } catch (Exception ie) {
            }
		}
	}

    public void update()
    {
        statusBar.update(player1);
        starsBg.update();
        starsFg.update();
    }

	public void paint(Graphics g)
    {
		// bg
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

        statusBar.repaint();
        starsBg.paint(g);
        starsFg.paint(g);

		// paint players
		try {
			for (Ship p : players) {
				if (p.isAlive()) {
					p.draw(g);
                } else {
                    // XXX: This really shouldn't be in the render phase...
                    players.remove(p);
                }
			}
		} catch (ConcurrentModificationException ie) {
        }

		// paint bullets
		try {
			for(Projectile b : bulletList) {
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

		if(evt.getKeyCode()==KeyEvent.VK_Q){
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

	public synchronized void registerProjectile(Projectile b)
    {
		b.setId(bulletId++);
		bulletList.add(b);
	}

	public synchronized void unregisterProjectile(Projectile b)
    {
		for (int i = 0; i < bulletList.size(); i++) {
			if (bulletList.get(i).getId() == b.getId()) {
				bulletList.get(i).setId(Const.UNREGISTERED);
				bulletList.remove(i);
				return;
			}
		}
	}

    public synchronized void clearProjectiles()
    {
        for (int i = 0; i < bulletList.size(); i++) {
            bulletList.get(i).setId(Const.UNREGISTERED);
        }
        bulletList.clear();
    }

	public synchronized void registerExplosion(Explosion e)
    {
		e.setId(explosionId++);
		explosionList.add(e);
	}

	public synchronized void unregisterExplosion(Explosion e)
    {
		for (int i = 0; i < explosionList.size(); i++) {
			if (explosionList.get(i).getId() == e.getId()) {
				explosionList.get(i).setId(Const.UNREGISTERED);
				explosionList.remove(i);
				return;
			}
		}
	}

	public synchronized void registerShip(Ship e)
    {
		e.setId(playersId++);
		players.add(e);
	}

	public synchronized void unregisterShip(Ship e)
    {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) == e) {
				players.get(i).setId(Const.UNREGISTERED);
				players.remove(i);
				return;
			}
		}
	}
    public synchronized void killNPCs()
    {
        ArrayList<Ship> npcs = new ArrayList<Ship>();
        for (Ship player : players) {
            if (player instanceof Enterprise == false) {
                npcs.add(player);
            }
        }
        for (Ship npc : npcs) {
            npc.explode();
        }
    }

	// @Deprecated
	public synchronized Ship intersects(Projectile b)
    {
		try {
			for (Ship s : players) {
				if (b.intersects(s)) {
					return s;
                }
			}
		} catch (ConcurrentModificationException ie) {}
		return null;
	}

	public synchronized Ship intersects(Ship r)
    {
		try {
			for(Ship p : players) {
				// NOTE: a ship might want to know if an ally hits it, maybe to push him away?
				if (p.getId() != r.getId() && p.getTeam() != r.getTeam() && p.intersects((Destructable)r)) {
					return p;
                }
			}
		} catch (ConcurrentModificationException ie) {}
		return null;
	}

	///

	public Dimension getMinumumSize() { return getPreferredSize(); }
	public Dimension getPreferredSize() { return new Dimension(OuterSpacePanel.WIDTH, OuterSpacePanel.HEIGHT); }
	public Ship getPlayer() { return player1; }

    public void respawnPlayer()
    {
        player1 = new Enterprise(100, 100, this);
        changeScene(new DemoLevel(this));
    }
}
