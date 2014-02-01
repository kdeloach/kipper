package kipper.weapons;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.geom.*;
import java.util.ArrayList;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;
import kipper.projectiles.*;

public abstract class Weapon implements Upgradable
{
    private double x, y;
    private int damage;
    private int spread = 1;
    public int percCooled = 0;
    private int cooldown;
    private boolean fire = false;
    private Point mouse;
    private ArrayList<Upgrade> upgrades;
    private Ship ship;

    public Weapon(double x, double y, Ship s)
    {
        this.x = x;
        this.y = y;
        this.ship = s;

        setLocation(x, y);

        upgrades = new ArrayList<Upgrade>();
        mouse = new Point();
        cooldown = getDefaultCooldown();
        damage = getDefaultDamage();
    }

    // XXX
    protected boolean facesLeft()
    {
        return ship.getOrientation() == Const.FACE_LEFT;
    }

    public void draw(Graphics g)
    {
        //drawBounds(g);
    }

    public void drawBounds(Graphics g)
    {
        g.setColor(Color.GREEN);
        g.drawRect((int)getX() + (facesLeft() ? -getWidth() : 0), (int)getY(), getWidth(), getHeight());
    }

    abstract public int getWidth();
    abstract public int getHeight();
    abstract public Projectile createProjectile();
    abstract public Image getIcon();
    abstract public int getDefaultCooldown();
    abstract public int getDefaultDamage();

    public String getSoundFile() { return ""; }

    public void playSound()
    {
        if (getSoundFile().length() > 0) {
            Util.instance.playSound(getSoundFile());
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isFiring() { return fire; }
    public void startFiring() { fire = true; }
    public void stopFiring() { fire = false; }
    public double percentCooled() { return (double)percCooled / (double)getCooldown(); }
    public int amountSlots() { return 12; }

    public int getCooldown() { return (int)getValue(Upgrade.COOLDOWN, cooldown); }
    public double getDamage() { return getValue(Upgrade.DAMAGE, damage); }
    public int getSpread() { return (int)getValue(Upgrade.SPREAD, spread); }
    public double getTheta() { return getValue(Upgrade.HEADING, ship.heading()); }
    public double getSizeBonus() { return getValue(Upgrade.SIZE, 1); }
    public boolean collidesWithProjectiles() { return getValue(Upgrade.COLLIDE, 0) == 1; }

    // Point where projectiles will fire from
    public Point2D.Double projectileOrigin()
    {
        double x = getX() + getWidth() * (facesLeft() ? -1 : 1);
        double y = getY() + getHeight() / 2;
        return new Point2D.Double(x, y);
    }

    public void update()
    {
        if (percCooled > 0) {
            percCooled--;
            return;
        }

        if (fire) {
            for (Upgrade a : upgrades) {
                a.weaponFired(this);
            }

            if (!fire) {
                return;
            }

            fireWeapon();

            percCooled = getCooldown();
        }
    }

    protected void fireWeapon()
    {
        int spread = getSpread();
        double theta = getTheta();

        Point2D.Double origin = projectileOrigin();
        Projectile p;

        if (spread <= 1) {
            p = createProjectile();
            p.setLocation(origin.x, origin.y - p.getHeight() / 2);
            playSound();
            ship().panel().addProjectile(p);
        } else {
            for (int i = 0; i < spread; i++) {
                p = createProjectile();
                p.setLocation(origin.x, origin.y - p.getHeight() / 2);
                p.setTheta(p.getTheta() + Math.toRadians(180 * ship.getOrientation() - (60 / (spread - 1) * i - 30)));
                ship().panel().addProjectile(p);
            }
            playSound();
        }
    }

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

    @Override
    public Upgrade upgradeAt(int index)
    {
        if (index >= 0 && index < upgrades.size()) {
            return upgrades.get(index);
        }
        return null;
    }

    @Override
    public void addUpgrade(Upgrade a)
    {
        if (upgrades.size() < amountSlots()) {
            upgrades.add(a);
        }
    }

    @Override
    public void removeUpgrade(int index)
    {
        if (index >= 0 && index < upgrades.size()) {
            Upgrade a = upgrades.get(index);
            if (a != null) {
                upgrades.remove(index);
            }
        }
    }

    public double getValue(String attr, double n)
    {
        for (Upgrade a : upgrades) {
            n = a.getValue(this, attr, n);
        }
        return n;
    }

    public Ship ship() { return ship; }
    public OuterSpacePanel panel() { return ship().panel(); }
}
