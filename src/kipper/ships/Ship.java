package kipper.ships;

import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.util.ArrayList;
import kipper.*;
import kipper.weapons.*;
import kipper.upgrades.*;
import kipper.effects.*;

public abstract class Ship implements
    ImageEntity, PolygonMaskedEntity, Controllable, Upgradable,
    MouseListener, MouseMotionListener, KeyListener
{
    public int team;
    public double x, y;

    // amount of damage taken
    protected double dmg;

    protected boolean underControl = false;
    protected int disabledTicks = 0;

    public Point destination = new Point();
    public Point mouse = new Point();
    public Point mousePressed = new Point();

    public Weapon wpn;
    public Weapon[] wpnList = new Weapon[10];

    protected OuterSpacePanel osp;

    // last ship hit
    public Ship target = null;

    private EntityWobble wobble;
    private boolean isShiftDown = false;

    public Ship()
    {
    }

    public Ship(double x, double y, int team, OuterSpacePanel c)
    {
        this.osp = c;
        this.team = team;
        wobble = new EntityWobble();
        setLocation(x, y);
        setDestination(getX(), getY());
    }

    @Override
    public void update()
    {
        if (underControl() && isShiftDown) {
            setDestination(getX() + getWidth() / 2, getY() + getWidth() / 2);
        }
        if (disabledTicks > 0) {
            disabledTicks--;
        }
        updateWeapons();
        if (!underControl()) {
            think();
        }
        move();
    }

    private void updateWeapons()
    {
        for (Weapon w : wpnList) {
            if (w != null) {
                w.update();
            }
        }
    }

    public void move()
    {
        double mx = x + (destination.x - x) / getSpeed();
        double my = y + (destination.y - y) / getSpeed();

        setLocation(mx, my);

        wobble.move(this);

        if (getWeapon() != null) {
            getWeapon().setLocation(x, y);
        }
    }

    @Override
    public void collide(Entity e)
    {
        double leastHp = Math.min(e.getLife(), getLife());
        e.hit(leastHp);
        hit(leastHp);
    }

    public void freezeMovement(int durationMs)
    {
        disabledTicks = Util.msToTicks(durationMs);
    }

    public void setDestination(double mx, double my)
    {
        if (isDisabled()) {
            return;
        }
        // don't let the player go off-screen
        if (underControl()) {
            if (mx < getWidth() / 2) {
                mx = getWidth() / 2;
            } else if (mx > OuterSpacePanel.WIDTH - getWidth() / 2) {
                mx = OuterSpacePanel.WIDTH - getWidth() / 2;
            }
            if (my < getHeight() / 2) {
                my = getHeight() / 2;
            } else if (my > OuterSpacePanel.HEIGHT - getHeight() / 2) {
                my = OuterSpacePanel.HEIGHT - getHeight() / 2;
            }
        }
        destination = new Point((int)mx, (int)my);
    }

    // NPC logic goes here
    public void think() {}

    // Used by NPC's to simulate mouse click
    public void targetLocation(int x, int y)
    {
        setMousePressedLocation(x, y);
    }

    public void equipWeapon(Weapon w)
    {
        for (int i = 0; i < wpnList.length; i++) {
            if (wpnList[i] == null) {
                wpnList[i] = w;
                return;
            }
        }
    }

    @Override
    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void selectWeapon(int n)
    {
        if (n >= 0 && n < wpnList.length) {
            if (wpnList[n] == null) {
                return;
            }
            wpn = wpnList[n];
            wpn.setLocation(x, y);
        }
    }

    public void setMouseLocation(int x, int y)
    {
        mouse.x = x;
        mouse.y = y;
    }

    public void setMousePressedLocation(int x, int y)
    {
        mousePressed.x = x;
        mousePressed.y = y;
    }

    // Returns 0 or 1
    public double heading()
    {
        return Math.toRadians(getOrientation() * 180);
    }

    public OuterSpacePanel panel() { return osp; }

    /////////////

    @Override abstract public int getWidth();
    @Override abstract public int getHeight();

    @Override
    public void draw(Graphics g)
    {
        //g.setColor(Color.GREEN);
        //g.drawRect((int)getX(), (int)getY(), getWidth(), getHeight());
        g.drawImage(getImage(), (int)getX(), (int)getY(), osp);
        if (getWeapon() != null) {
            getWeapon().draw(g);
        }
    }

    abstract public int getOrientation();
    abstract public int getMaxHp();
    abstract public int getSpeed();
    abstract public Polygon getPolygonMask();
    abstract public String getName();

    /////////////

    public Weapon getWeapon(int n)
    {
        if(n < 0 || n >= wpnList.length) {
            throw new IllegalArgumentException("Weapon [" + n + "] does not exist");
        }
        return wpnList[n];
    }

    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public boolean isAlive() { return dmg < getMaxHp(); }
    @Override public int getLife() { return getMaxHp() - (int)dmg; }
    @Override public int getTeam() { return team; }

    public int getSlotsAmt() { return 6; }
    public Ship getTarget() { return target; }
    public Point getDesination() { return destination; }
    public Weapon getWeapon() { return wpn; }
    public double percentHealth() { return (double)getLife() / ( double)getMaxHp(); }
    public boolean isDisabled() { return disabledTicks > 0; }
    public Image getImage() { throw new UnsupportedOperationException("Not implemented"); }

    /////////////

    @Override
    public void hit(double damage)
    {
        if (isAlive()) {
            this.dmg += damage;
            if (getLife() <= 0) {
                die();
            }
        }
    }

    @Override
    public void die()
    {
        dmg += getMaxHp();
        deathExplosion();
    }

    public void deathExplosion()
    {
        double px = getX() + getWidth() / 2;
        double py = getY() + getHeight() / 2;
        ParticleEmitter pe = new ParticleEmitter(px, py, new SampleConfigImpl());
        osp.addEmitter(pe);
    }

    /////////////

    @Override public boolean underControl() { return underControl; }

    @Override
    public void gainControl()
    {
        osp.addMouseListener(this);
        osp.addMouseMotionListener(this);
        underControl = true;
    }

    @Override
    public void releaseControl()
    {
        osp.removeMouseListener(this);
        osp.removeMouseMotionListener(this);
        underControl = false;
    }

    /////////////

    @Override public void mouseEntered(MouseEvent evt) {}
    @Override public void mouseExited(MouseEvent evt) {}
    @Override public void mouseClicked(MouseEvent evt) {}

    @Override
    public void mousePressed(MouseEvent e)
    {
        Point p = scalePoint(e.getPoint());
        setMousePressedLocation(p.x, p.y);
        if (e.getButton() == MouseEvent.BUTTON1) {
            getWeapon().startFiring();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1) {
            getWeapon().stopFiring();
        }
    }

    /////////////

    @Override public void mouseDragged(MouseEvent e)
    {
        mousePressed(e);
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        Point p = scalePoint(e.getPoint());
        setMouseLocation(p.x, p.y);
        getWeapon().setMouseLocation(p.x, p.y);
        setDestination(p.x - getWidth() / 2, p.y-getHeight() / 2);
    }

    public Point scalePoint(Point p)
    {
        double ratio = Util.getAspectRatio(osp);
        Point offset = Util.boxOffset(osp, ratio);
        return new Point(
            (int)((p.x - offset.x) * 1 / ratio),
            (int)((p.y - offset.y) * 1 / ratio));
    }

    /////////////

    @Override public void keyTyped(KeyEvent evt) {}

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (underControl) {
            isShiftDown = e.isShiftDown();
            setDestination(mouse.x, mouse.y);
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (underControl) {
            int code = e.getKeyCode() - KeyEvent.VK_1;
            isShiftDown = e.isShiftDown();

            // only need keys 1-5    (0-4)
            // and abort if weapon request is already selected
            if (code < 0 || code >= 5 || wpnList[code] == getWeapon()) {
                return;
            }

            // resume firing if weapons switches while the fire button is held down
            if (getWeapon().isFiring()) {
                getWeapon().stopFiring();
                selectWeapon(code);
                getWeapon().startFiring();
            } else {
                selectWeapon(code);
            }
        }
    }

    /////////////

    @Override public Ability upgradeAt(int index) { throw new UnsupportedOperationException("Not implemented"); }
    @Override public void addUpgrade(Ability a) { throw new UnsupportedOperationException("Not implemented"); }
    @Override public void removeUpgrade(int index) { throw new UnsupportedOperationException("Not implemented"); }
}
