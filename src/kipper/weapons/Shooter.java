package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.projectiles.*;

public class Shooter extends Weapon
{
    Image icon;

    public Shooter(double x, double y, int rx, int ry, Ship c)
    {
        super(x, y, rx, ry, c);
        icon = Util.instance.loadImage("/assets/images/shooter_icon.gif");
    }

    @Override public int getWidth() { return 20; }
    @Override public int getHeight() { return 10; }
    @Override public Image getIcon() { return icon; }
    @Override public int getDefaultDamage() { return 8; }
    @Override public int getDefaultCooldown() { return 50; }

    @Override
    public void draw(Graphics g)
    {
    }

    @Override
    public void fireProjectile(double heading)
    {
        new Bullet(getX(), getY(), heading, getDamage(), this);
    }

    @Override
    public String getSoundFile()
    {
        return "/assets/sounds/Shooter.wav";
    }
}
