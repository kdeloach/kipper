package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;
import kipper.projectiles.*;

public class LaserGun extends Weapon
{
    Image icon;

    public LaserGun(double x, double y, int rx, int ry, Ship c)
    {
        super(x, y, rx, ry, c);
        icon = Util.instance.loadImage("/assets/images/laser_icon.gif");
    }

    @Override public int getWidth() { return 10; }
    @Override public int getHeight() { return 10; }
    @Override public Image getIcon() { return icon; }
    @Override public int getDefaultDamage() { return 2; }
    @Override public int getDefaultCooldown() { return 50; }

    @Override
    public void draw(Graphics g)
    {
    }

    @Override
    public void fireProjectile(double heading)
    {
        new LaserBeam(getX(), getY(), heading, getDamage(), this);
    }

    @Override
    public String getSoundFile()
    {
        return "/assets/sounds/LaserGun.wav";
    }
}
