package kipper;

import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import kipper.ships.*;
import kipper.effects.*;
import kipper.weapons.*;

public class OuterSpacePanel extends JPanel implements KeyListener, Runnable
{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 400;
    // Frame per second (1000 MS / 60 frames)
    public static long FPS = 1000 / 60;

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

    private Queue<Ship> deleteShips;
    private Queue<Projectile> deleteProjectiles;
    private Queue<Explosion> deleteExplosions;

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

		deleteShips = new LinkedList<Ship>();
		deleteProjectiles = new LinkedList<Projectile>();
		deleteExplosions = new LinkedList<Explosion>();

        starsBg = new MarqueeStars(500, Math.toRadians(180), 0.05, 0, Color.GRAY);
        starsFg = new MarqueeStars(25, Math.toRadians(180), 0.099, 2, Color.WHITE);

		player1 = new Enterprise(100, 100, this);
		changeScene(new DemoLevel(this));

		new Thread(this).start();
	}

    // Author: Bob Nystrom
    // Source: http://gameprogrammingpatterns.com/game-loop.html
	public void run()
    {
        long previous = System.currentTimeMillis();
        long lag = 0;

		while (true) {
            long current = System.currentTimeMillis();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            while (lag >= FPS) {
                update();
                lag -= FPS;
            }

			repaint();
		}
	}

    public void update()
    {
        statusBar.update(player1);
        starsBg.update();
        starsFg.update();
        for (int i = 0; i < players.size(); i++) {
            Ship p = players.get(i);
            if (p.isAlive()) {
                p.update();
            } else {
                // TODO: Do we still need this?
                removeShip(p);
            }
        }
        for (int i = 0; i < bulletList.size(); i++) {
            bulletList.get(i).update();
        }
        for (int i = 0; i < explosionList.size(); i++) {
            explosionList.get(i).update();
        }
        cleanup();
    }

    protected void cleanup()
    {
        Object head = deleteShips.poll();
        while (head != null) {
            players.remove(head);
            head = deleteShips.poll();
        }
        head = deleteProjectiles.poll();
        while (head != null) {
            bulletList.remove(head);
            head = deleteProjectiles.poll();
        }
        head = deleteExplosions.poll();
        while (head != null) {
            explosionList.remove(head);
            head = deleteExplosions.poll();
        }
    }

	public void paint(Graphics g)
    {
		// bg
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

        statusBar.repaint();
        starsBg.paint(g);
        starsFg.paint(g);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).draw(g);
        }
        for (int i = 0; i < bulletList.size(); i++) {
            bulletList.get(i).draw(g);
        }
        for (int i = 0; i < explosionList.size(); i++) {
            explosionList.get(i).draw(g);
        }
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

	public void keyTyped(KeyEvent evt)
    {
		getPlayer().keyTyped(evt);
	}

	public void keyPressed(KeyEvent evt)
    {
		if (evt.getKeyCode() == KeyEvent.VK_Q) {
            if (scene.name() == "upgrade") {
                changeScene(new DemoLevel(this));
            } else {
                changeScene(new ShipUpgradeScreen(this));
            }
		}
		getPlayer().keyPressed(evt);
	}

	public void keyReleased(KeyEvent evt)
    {
		getPlayer().keyReleased(evt);
	}

	////////////////////////

	public void registerProjectile(Projectile b)
    {
		b.setId(bulletId++);
		bulletList.add(b);
	}

    // TODO: Remove
	public void unregisterProjectile(Projectile b)
    {
		removeProjectile(b);
	}

    public void removeProjectile(Projectile p)
    {
        deleteProjectiles.add(p);
    }

    public void clearProjectiles()
    {
        for (int i = 0; i < bulletList.size(); i++) {
            bulletList.get(i).setId(Const.UNREGISTERED);
        }
        bulletList.clear();
    }

	public void registerExplosion(Explosion e)
    {
		e.setId(explosionId++);
		explosionList.add(e);
	}

    // TODO: Remove
	public void unregisterExplosion(Explosion e)
    {
		removeExplosion(e);
	}

    public void removeExplosion(Explosion e)
    {
        deleteExplosions.add(e);
    }

	public void registerShip(Ship e)
    {
		e.setId(playersId++);
		players.add(e);
	}

    // TODO: Remove
	public void unregisterShip(Ship e)
    {
		removeShip(e);
	}

    public void removeShip(Ship s)
    {
        deleteShips.add(s);
    }

    public void killNPCs()
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
	public Ship intersects(Projectile b)
    {
        for (Ship s : players) {
            if (b.intersects(s)) {
                return s;
            }
        }
		return null;
	}

	public Ship intersects(Ship r)
    {
        for(Ship p : players) {
            // NOTE: a ship might want to know if an ally hits it, maybe to push him away?
            if (p.getId() != r.getId() && p.getTeam() != r.getTeam() && p.intersects((Destructable)r)) {
                return p;
            }
        }
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

    public boolean contains(double x, double y)
    {
        return contains((int)x, (int)y);
    }
}
