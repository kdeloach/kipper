package kipper.ships;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.util.ArrayList;
import kipper.*;
import kipper.weapons.*;
import kipper.upgrades.*;
import kipper.effects.*;
import kipper.ships.controllers.*;

public abstract class Ship implements ImageEntity, PolygonMaskedEntity, Upgradable
{
    private static final Color healthBarRed = new Color(136, 0, 21);
    private static final Color healthBarGreen = new Color(34, 177, 76);

    private int team;
    private double x, y;
    private Point mouse = new Point();
    private Point mousePressed = new Point();
    private Point destination = new Point();
    private Weapon wpn;
    private Weapon[] wpnList = new Weapon[10];
    private EntityWobble wobble;
    private double dmg;
    private int disabledTicks = 0;
    private ShipController controller;

    // last ship hit
    public Ship target = null;
    protected OuterSpacePanel osp;

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

    public void setController(ShipController controller)
    {
        this.controller = controller;
    }

    public void handleInput()
    {
        if (controller != null) {
            controller.handleInput(this);
        }
    }

    @Override
    public void update()
    {
        if (disabledTicks > 0) {
            disabledTicks--;
        }
        updateWeapons();
        if (controller != null) {
            controller.update(this);
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
        if (controller != null) {
            controller.move(this);
        }
        wobble.move(this);
        moveWeapon();
    }

    public void moveWeapon()
    {
        Weapon weapon = getWeapon();
        if (weapon != null) {
            Point mount = getWeaponMountPoint();
            weapon.setLocation(x + mount.x, y + mount.y);
        }
    }

    @Override
    public void collide(Entity e)
    {
        double leastHp = Math.min(e.getLife(), getLife());
        e.hit(leastHp);
        hit(e.getLife());
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
        destination = new Point((int)mx, (int)my);
    }

    // Used by NPC's to simulate mouse click (movement and targeting are orthogonal)
    public void targetLocation(int x, int y)
    {
        setMousePressedLocation(x, y);
    }

    @Override
    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
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

    public Point getMousePressedLocation()
    {
        return mousePressed;
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

    public void selectWeapon(int n)
    {
        if (n >= 0 && n < wpnList.length) {
            if (wpnList[n] == null) {
                return;
            }
            wpn = wpnList[n];
            moveWeapon();
        }
    }

    public Weapon getWeapon(int n)
    {
        if(n < 0 || n >= wpnList.length) {
            throw new IllegalArgumentException("Weapon [" + n + "] does not exist");
        }
        return wpnList[n];
    }

    // Returns 0 if facing right, PI if facing left
    public double heading()
    {
        return Math.toRadians(getOrientation() * 180);
    }

    public OuterSpacePanel panel() { return osp; }

    /////////////

    @Override
    public void draw(Graphics g)
    {
        //drawBoundingBox(g);
        drawHealthBar(g);
        g.drawImage(getImage(), (int)getX(), (int)getY(), osp);
        if (getWeapon() != null) {
            getWeapon().draw(g);
        }
        //Util.drawMask(g, this);
    }

    public void drawBoundingBox(Graphics g)
    {
        g.setColor(Color.GREEN);
        g.drawRect((int)getX(), (int)getY(), getWidth(), getHeight());
    }

    public void drawHealthBar(Graphics g)
    {
        if (getLife() == getMaxLife()) {
            return;
        }
        int x = (int)getX();
        int y = (int)getY() + getHeight() + 1;
        int w = getWidth();
        int h = 2;
        g.setColor(percentLife() > 0.20 ? healthBarGreen : healthBarRed);
        g.fillRect(x, y, (int)(w * percentLife()), h);
    }

    @Override abstract public int getWidth();
    @Override abstract public int getHeight();

    // Returns Const.FACE_LEFT or Const.FACE_RIGHT
    abstract public int getOrientation();
    abstract public int getMaxLife();
    abstract public int getSpeed();
    abstract public Polygon getPolygonMask();
    abstract public String getName();
    // Point where we will attach weapon relative to ship X & Y
    abstract public Point getWeaponMountPoint();

    /////////////

    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public boolean isAlive() { return dmg < getMaxLife(); }
    @Override public int getLife() { return getMaxLife() - (int)dmg; }
    @Override public int getTeam() { return team; }

    public int getSlotsAmt() { return 6; }
    public Ship getTarget() { return target; }
    public Point getDestination() { return destination; }
    public Weapon getWeapon() { return wpn; }
    public Weapon getWeaponAt(int index) { return wpnList[index]; }
    public double percentLife() { return (double)getLife() / ( double)getMaxLife(); }
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
        setController(null);
        dmg += getMaxLife();
        deathExplosion();
        playSound();
    }

    protected void deathExplosion()
    {
        double px = getX() + getWidth() / 2;
        double py = getY() + getHeight() / 2;
        ParticleEmitter pe = new ParticleEmitter(px, py, new SampleConfigImpl());
        osp.addEmitter(pe);
    }

    public String getSoundFile()
    {
        switch (getTeam()) {
            case Const.TEAM_PLAYER:
                return "/assets/sounds/Explosion3.wav";
            case Const.TEAM_NPC:
                return "/assets/sounds/Explosion2.wav";
        }
        return "";
    }

    public void playSound()
    {
        if (getSoundFile().length() > 0) {
            Util.instance.playSound(getSoundFile());
        }
    }

    /////////////

    public Point scalePoint(Point p)
    {
        double ratio = Util.getAspectRatio(osp);
        Point offset = Util.boxOffset(osp, ratio);
        return new Point(
            (int)((p.x - offset.x) * 1 / ratio),
            (int)((p.y - offset.y) * 1 / ratio));
    }

    /////////////

    @Override public Upgrade upgradeAt(int index) { throw new UnsupportedOperationException("Not implemented"); }
    @Override public void addUpgrade(Upgrade a) { throw new UnsupportedOperationException("Not implemented"); }
    @Override public void removeUpgrade(int index) { throw new UnsupportedOperationException("Not implemented"); }
}
