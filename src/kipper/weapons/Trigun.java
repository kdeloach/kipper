package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;
import kipper.upgrades.*;

// Weapon used by TriangleMan
public class Trigun extends Weapon
{
    Image img;

    public Trigun(double x, double y, int rx, int ry, Ship s)
    {
        super(x, y, rx, ry, s);
        img = Util.instance.loadImage("/assets/trigun_ico.gif");
    }

    @Override
    public void fireProjectile(double heading)
    {
        double px = getX() - 1;
        double py = getY() + getHeight() / 2;
        new Bullet(px, py, heading, getDamage(), this)
        {
            public Color getColor(Graphics g) { return Color.GREEN; }
        };
    }

    @Override public int getWidth() { return 10; }
    @Override public int getHeight() { return 10; }
    @Override public Image getIcon() { return img; }
    @Override public int getDefaultDamage() { return 1; }
    @Override public int getDefaultCooldown() { return 250; }

    @Override
    public void draw(Graphics g)
    {
        int px = (int)getX();
        int py = (int)getY();
        g.setColor(Color.RED);
        g.fillPolygon(
            new int[]{px, px + getWidth(), px + getWidth()},
            new int[]{py + getHeight() / 2, py, py + getHeight()},
            3
        );
    }
}
