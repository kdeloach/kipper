package kipper;

import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
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

    private boolean paused = false;

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

        starsBg = new MarqueeStars(500, Math.toRadians(180), 0, 2, Color.GRAY);
        starsFg = new MarqueeStars(25, Math.toRadians(180), 0.5, 4, Color.WHITE);

		player1 = new Enterprise(100, 100, this);
        addShip(player1);
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

            while (paused) {
                previous = System.currentTimeMillis();
            }

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
                removeShip(p);
            }
        }
        for (int i = 0; i < bulletList.size(); i++) {
            Projectile p = bulletList.get(i);
            if (p.isAlive()) {
                p.update();
            } else {
                removeProjectile(p);
            }
        }
        for (int i = 0; i < explosionList.size(); i++) {
            Explosion p = explosionList.get(i);
            if (p.isAlive()) {
                p.update();
            } else {
                removeExplosion(p);
            }
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

	public void keyTyped(KeyEvent e)
    {
		getPlayer().keyTyped(e);
	}

	public void keyPressed(KeyEvent e)
    {
		if (e.getKeyCode() == KeyEvent.VK_Q) {
            if (scene.name() == "upgrade") {
                changeScene(new DemoLevel(this));
            } else {
                changeScene(new ShipUpgradeScreen(this));
            }
		}
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = !paused;
        }
		getPlayer().keyPressed(e);
	}

	public void keyReleased(KeyEvent e)
    {
		getPlayer().keyReleased(e);
	}

	////////////////////////

	public void addProjectile(Projectile b)
    {
		bulletList.add(b);
	}

    public void removeProjectile(Projectile p)
    {
        deleteProjectiles.add(p);
    }

    public void removeAllProjectiles()
    {
        for (int i = 0; i < bulletList.size(); i++) {
            removeProjectile(bulletList.get(i));
        }
    }

	public void addExplosion(Explosion e)
    {
		explosionList.add(e);
	}

    public void removeExplosion(Explosion e)
    {
        deleteExplosions.add(e);
    }

	public void addShip(Ship e)
    {
		players.add(e);
	}

    public void removeShip(Ship s)
    {
        deleteShips.add(s);
    }

    public void removeManyShips(List<Ship> ships)
    {
        for (int i = 0; i < ships.size(); i++) {
            deleteShips.add(ships.get(i));
        }
    }

    public List<Ship> getNPCs()
    {
        ArrayList<Ship> result = new ArrayList<Ship>();
        for (int i = 0; i < players.size(); i++) {
            Ship player = players.get(i);
            if (player instanceof Enterprise == false) {
                result.add(player);
            }
        }
        return result;
    }

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
        for (Ship p : players) {
            // NOTE: a ship might want to know if an ally hits it, maybe to push him away?
            if (p != r && p.getTeam() != r.getTeam() && p.intersects(r)) {
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
        addShip(player1);
        changeScene(new DemoLevel(this));
    }

    public boolean contains(double x, double y)
    {
        return contains((int)x, (int)y);
    }
}
