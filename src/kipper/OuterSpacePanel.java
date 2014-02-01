package kipper;

import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.Queue;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import kipper.ships.*;
import kipper.effects.*;
import kipper.weapons.*;
import kipper.projectiles.*;

public class OuterSpacePanel extends JComponent implements Runnable
{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 300;
    // Frame per second (1000 MS / 60 frames)
    public static long FPS = 1000 / 80;

    private LightNoiseBg noiseBg;
    private MarqueeStars starsBg;
    private MarqueeStars starsFg;
    private Image bgCache;

    private Ship player1;
    private Scene scene;

    private LinkedList<Ship> players;
    private LinkedList<Projectile> projectiles;
    private LinkedList<ParticleEmitter> emitters;

    private boolean paused = false;

    // Debug: remove these
    private boolean _drawing = false;
    private boolean _updating = false;

    private int numUpdates = 0;
    private int numRepaints = 0;
    private int duration = 0;

    public OuterSpacePanel()
    {
        super();
        setFocusable(true);
        setIgnoreRepaint(true);

        Global.key = new KeyHandler();
        Global.mouse = new MouseHandler();
        addKeyListener(Global.key);
        addMouseListener(Global.mouse);
        addMouseMotionListener(Global.mouse);
        addComponentListener(new RepaintAfterResize(this));

        players = new LinkedList<Ship>();
        projectiles = new LinkedList<Projectile>();
        emitters = new LinkedList<ParticleEmitter>();

        noiseBg = new LightNoiseBg(this);
        starsBg = new MarqueeStars(500, Math.toRadians(180), 0, 1, 3, 0x33, 0xFF);
        starsFg = new MarqueeStars(25, Math.toRadians(180), 0.5, 3, 6, 0xFF, 0xFF);

        player1 = new Darkwing(100, 100, this);
        addShip(player1);
        changeScene(new DemoLevel(this));
    }

    // Author: Bob Nystrom
    // Source: http://gameprogrammingpatterns.com/game-loop.html
    @Override
    public void run()
    {
        long previous = System.currentTimeMillis();
        long lag = 0;

        Image buffer = createImage(WIDTH, HEIGHT);

        while (true) {
            long current = System.currentTimeMillis();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;
            duration += elapsed;

            if (duration >= 3000) {
                duration = 0;
                numRepaints = 0;
                numUpdates = 0;
            }

            while (lag >= FPS) {
                handleInput();
                if (!paused) {
                    update();
                    numUpdates++;
                }
                Global.key.update();
                Global.mouse.update();
                lag -= FPS;
            }

            int w = getWidth();
            int h = getHeight();
            if (w > 0 && h > 0) {
                draw(buffer.getGraphics());
                double ratio = Util.getAspectRatio(this);
                Point offset = Util.boxOffset(this, ratio);
                w = (int)(WIDTH * ratio);
                h = (int)(HEIGHT * ratio);
                getGraphics().drawImage(buffer, offset.x, offset.y, w, h, this);
            }
        }
    }

    public void handleInput()
    {
        if (Global.key.justPressed(KeyEvent.VK_SPACE)) {
            paused = !paused;
            Util.instance.playSound("/assets/sounds/Pause.wav");
        }

        if (paused) {
            return;
        }

        if (Global.key.justPressed(KeyEvent.VK_Q)) {
            if (scene.getName() == "upgrade") {
                changeScene(new DemoLevel(this));
            } else {
                changeScene(new ShipUpgradeScreen(this));
            }
        }
        if (scene != null) {
            scene.handleInput();
        }
    }

    public void update()
    {
        if (_updating) {
            throw new UnsupportedOperationException("Tried to update while already updating");
        }
        if (_drawing) {
            throw new UnsupportedOperationException("Tried to update while drawing");
        }
        _updating = true;

        noiseBg.update();
        starsBg.update();
        starsFg.update();

        updateEntities();
        performCollisions();

        _updating = false;
    }

    private void updateEntities()
    {
        Iterator<Ship> shipsIter = players.iterator();
        while (shipsIter.hasNext()) {
            Ship s = shipsIter.next();
            if (s.isAlive()) {
                s.handleInput();
                s.update();
            } else {
                shipsIter.remove();
            }
        }

        Iterator<Projectile> projectilesIter = projectiles.iterator();
        while (projectilesIter.hasNext()) {
            Projectile p = projectilesIter.next();
            if (p.isAlive()) {
                p.update();
            } else {
                projectilesIter.remove();
            }
        }

        Iterator<ParticleEmitter> emittersIter = emitters.iterator();
        while (emittersIter.hasNext()) {
            ParticleEmitter pe = emittersIter.next();
            if (pe.isAlive()) {
                pe.update();
            } else {
                emittersIter.remove();
            }
        }
    }

    private void performCollisions()
    {
        // Collide ships with projectiles
        for (int i = 0; i < players.size(); i++) {
            Ship player = players.get(i);
            for (int k = 0; k < projectiles.size(); k++) {
                Projectile bullet = projectiles.get(k);
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
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p1 = projectiles.get(i);
            for (int k = 0; k < i && p1.isAlive(); k++) {
                Projectile p2 = projectiles.get(k);
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

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
    }

    public void draw(Graphics g)
    {
        if (_updating) {
            throw new UnsupportedOperationException("Tried to draw while updating");
        }
        if (_drawing) {
            throw new UnsupportedOperationException("Tried to draw while already drawing");
        }
        _drawing = true;

        // SLOW
        //Graphics2D g2 = (Graphics2D)g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (bgCache == null) {
            bgCache = createImage(WIDTH, HEIGHT);
            noiseBg.draw(bgCache.getGraphics());
            starsBg.draw(bgCache.getGraphics());
        }
        g.drawImage(bgCache, 0, 0, WIDTH, HEIGHT, this);

        starsFg.draw(g);

        Iterator<Ship> shipsIter = players.iterator();
        while (shipsIter.hasNext()) {
            Ship s = shipsIter.next();
            s.draw(g);
        }

        Iterator<Projectile> projectilesIter = projectiles.iterator();
        while (projectilesIter.hasNext()) {
            Projectile p = projectilesIter.next();
            p.draw(g);
        }

        Iterator<ParticleEmitter> emittersIter = emitters.iterator();
        while (emittersIter.hasNext()) {
            ParticleEmitter pe = emittersIter.next();
            pe.draw(g);
        }

        if (scene != null) {
            scene.draw(g);
        }

        if (paused) {
            drawPauseScreen(g);
        }

        if (false) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 150, 100);
            g.setColor(Color.WHITE);
            g.drawString("Updates: " + numUpdates, 5, 15);
            g.drawString("Repaints: " + numRepaints, 5, 30);
            g.drawString("Duration: " + duration, 5, 45);
            if (duration > 0) {
                g.drawString(String.format("Repaints per sec: %.2f", (double)numRepaints / duration * 100), 5, 60);
                g.drawString(String.format("Updates per sec: %.2f", (double)numUpdates / duration * 1000), 5, 75);
            }
        }

        numRepaints++;
        _drawing = false;
    }

    private void drawPauseScreen(Graphics g)
    {
        Font f = new Font("Arial", Font.BOLD, 18);
        String text = "PAUSED";
        int textWidth = g.getFontMetrics(f).stringWidth(text);
        g.setFont(f);
        g.setColor(Color.WHITE);
        int x = WIDTH / 2 - textWidth / 2;
        int y = HEIGHT / 2;
        g.drawString(text, x, y);
    }

    private void changeScene(Scene nextScene)
    {
        if (scene != null) {
            scene.destroyScene();
        }
        scene = nextScene;
        scene.createScene();
    }

    ////////////////////////

    public void addProjectile(Projectile p)
    {
        projectiles.add(p);
    }

    public void removeAllProjectiles()
    {
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.die();
        }
    }

    public void addEmitter(ParticleEmitter pe)
    {
        emitters.add(pe);
    }

    public void addShip(Ship s)
    {
        players.add(s);
    }

    public void removeManyShips(List<Ship> ships)
    {
        Iterator<Ship> iter = ships.iterator();
        while (iter.hasNext()) {
            Ship s = iter.next();
            s.die();
        }
    }

    public List<Ship> getNPCs()
    {
        LinkedList<Ship> result = new LinkedList<Ship>();
        Iterator<Ship> iter = players.iterator();
        while (iter.hasNext()) {
            Ship s = iter.next();
            if (s instanceof Enterprise == false) {
                result.add(s);
            }
        }
        return result;
    }

    ///

    @Override
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize()
    {
        int zoom = 2;
        return new Dimension(OuterSpacePanel.WIDTH * zoom, OuterSpacePanel.HEIGHT * zoom);
    }

    public Ship getPlayer() { return player1; }

    public void respawnPlayer()
    {
        player1 = new Darkwing(100, 100, this);
        addShip(player1);
        player1.gainControl();
    }

    public boolean contains(double x, double y)
    {
        return contains((int)x, (int)y);
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
