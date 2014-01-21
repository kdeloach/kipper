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
    LightNoiseBg noiseBg;
    MarqueeStars starsBg, starsFg;

    public Ship player1;

    // current scene
    Scene scene;

    private ArrayList<Ship> players;
    private ArrayList<Projectile> bulletList;
    private ArrayList<Explosion> explosionList;

    private Queue<Ship> deleteShips;
    private Queue<Projectile> deleteProjectiles;
    private Queue<Explosion> deleteExplosions;

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

        noiseBg = new LightNoiseBg(this);
        starsBg = new MarqueeStars(500, Math.toRadians(180), 0, 1, 3, 0x33, 0xFF);
        starsFg = new MarqueeStars(25, Math.toRadians(180), 0.5, 3, 6, 0xFF, 0xFF);

        player1 = new Darkwing(100, 100, this);
        addShip(player1);
        changeScene(new DemoLevel(this));

        new Thread(this).start();
    }

    // Author: Bob Nystrom
    // Source: http://gameprogrammingpatterns.com/game-loop.html
    @Override
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
        noiseBg.update();
        starsBg.update();
        starsFg.update();
        updateEntities();
        performCollisions();
        cleanup();
    }

    private void updateEntities()
    {
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
    }

    private void performCollisions()
    {
        // Collide ships with projectiles
        for (int i = 0; i < players.size(); i++) {
            Ship player = players.get(i);
            for (int k = 0; k < bulletList.size(); k++) {
                Projectile bullet = bulletList.get(k);
                if (Util.intersects(player, bullet)) {
                    boolean canCollide = player != bullet.getOwner().ship() || bullet.collidesWithOwner();
                    if (canCollide) {
                        bullet.getOwner().ship().target = player;
                        player.hit(bullet.getDamage());
                        bullet.hit(bullet.getLife());
                    }
                }
            }
        }
        // Collide ships
        for (int i = players.size() - 1; i >= 0; i--) {
            Ship p1 = players.get(i);
            for (int k = 0; k < i && p1.isAlive(); k++) {
                Ship p2 = players.get(k);
                if (Util.intersects(p1, p2)) {
                    p1.collide(p2);
                }
            }
        }
        // Collide projectiles
        for (int i = bulletList.size() - 1; i >= 0; i--) {
            Projectile p1 = bulletList.get(i);
            for (int k = 0; k < i && p1.isAlive(); k++) {
                Projectile p2 = bulletList.get(k);
                boolean canCollide = p1.getTeam() != p2.getTeam() && (p1.collidesWithProjectiles() || p2.collidesWithProjectiles());
                if (canCollide) {
                    if (Util.intersects(p1, p2)) {
                        p1.hit(p1.getLife());
                        p2.hit(p2.getLife());
                    }
                }
            }
        }
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
        // SLOW
        //Graphics2D g2 = (Graphics2D)g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // bg
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        statusBar.repaint();
        noiseBg.paint(g);
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

    void changeScene(Scene s)
    {
        if (scene != null) {
            scene.destroyScene();
        }
        scene = s;
        scene.createScene();
    }

    /////////////////////////
    // Keyboard Controls

    @Override public void keyTyped(KeyEvent e) { getPlayer().keyTyped(e); }
    @Override public void keyReleased(KeyEvent e) { getPlayer().keyReleased(e); }

    @Override
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

    ///

    public Dimension getMinumumSize() { return getPreferredSize(); }
    public Dimension getPreferredSize() { return new Dimension(OuterSpacePanel.WIDTH, OuterSpacePanel.HEIGHT); }
    public Ship getPlayer() { return player1; }

    public void respawnPlayer()
    {
        player1 = new Darkwing(100, 100, this);
        addShip(player1);
        changeScene(new DemoLevel(this));
    }

    public boolean contains(double x, double y)
    {
        return contains((int)x, (int)y);
    }
}
