package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;

public class LightningGun extends Weapon
{
    Image icon;

    public LightningGun(double x, double y, int rx, int ry, Ship c)
    {
        super(x, y, rx, ry, c);
        icon = Util.instance.loadImage("/assets/lightning_icon.gif");
    }

    @Override public int getWidth() { return 2; }
    @Override public int getHeight() { return 2; }
    @Override public Image getIcon() { return icon; }
    @Override public int getDefaultDamage() { return 1; }
    @Override public int getDefaultCooldown() { return 200; }

    @Override
    public void draw(Graphics g)
    {
    }

    @Override
    public void fireProjectile(double heading)
    {
        new Bolt(getX(), getY(), heading, getDamage(), this);
    }
}
