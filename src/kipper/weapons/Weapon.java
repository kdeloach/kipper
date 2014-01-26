package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Point;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;

public abstract class Weapon implements Upgradable
{
    private double x, y;
    private int damage;
    private int spread = 1;
    public int percCooled = 0;
    private int cooldown;
    private boolean fire = false;
    private Point rel;
    private Point mouse;
    private ArrayList<Upgrade> upgrades;
    private Ship ship;

    public Weapon(double x, double y, int rx, int ry, Ship s)
    {
        this.x = x + rx;
        this.y = y + ry;
        this.ship = s;
        this.rel = new Point(rx, ry);

        setLocation(x, y);

        upgrades = new ArrayList<Upgrade>();
        mouse = new Point();
        cooldown = getDefaultCooldown();
        damage = getDefaultDamage();
    }

    abstract public int getWidth();
    abstract public int getHeight();
    abstract public void fireProjectile(double heading);
    abstract public void draw(Graphics g);
    abstract public Image getIcon();
    abstract public int getDefaultCooldown();
    abstract public int getDefaultDamage();

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
    public double heading() { return getValue(Upgrade.HEADING, ship.heading()); }
    public double getSizeBonus() { return getValue(Upgrade.SIZE, 1); }
    public boolean collidesWithProjectiles() { return getValue(Upgrade.COLLIDE, 0) == 1; }

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

            int spread = getSpread();
            double heading = heading();

            if (spread <= 1) {
                fireProjectile(heading);
            } else {
                for (int i = 0; i < spread; i++) {
                    fireProjectile(Math.toRadians(180 * ship.getOrientation() - (60 / (spread - 1) * i - 30)) + heading);
                }
            }

            percCooled = getCooldown();
        }
    }

    public void setLocation(double x, double y)
    {
        this.x = x + rel.x;
        this.y = y + rel.y;
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

    protected double getValue(String attr, double n)
    {
        for (Upgrade a : upgrades) {
            n = a.getValue(this, attr, n);
        }
        return n;
    }

    public Ship ship() { return ship; }
    public OuterSpacePanel panel() { return ship().panel(); }
}
