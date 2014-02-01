package kipper.projectiles;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;
import kipper.weapons.*;

public class LaserBeam extends Bullet implements MaskedEntity
{
    public LaserBeam(double dmg, Weapon w)
    {
        super(dmg, w);
    }

    @Override
    public void deathExplosion()
    {
        double px = stopX() + getWidth() / 2;
        double py = stopY() + getHeight() / 2;
        ParticleEmitter pe = new ParticleEmitter(px, py, new SampleConfigImpl());
        weapon.ship().panel().addEmitter(pe);
    }

    @Override
    public void setLocation(double x, double y)
    {
        super.setLocation(x, y);
    }

    @Override
    public void draw(Graphics g)
    {
        g.setColor(Color.WHITE);
        Util.drawThickLine(g, getX(), getY(), stopX(), stopY(), getWidth());
    }

    // Width and height of each individual point on the beam
    // NOT the width and height of the bounding box
    @Override public int getWidth() { return (int)(3 * weapon.getSizeBonus()); }
    @Override public int getHeight() { return getWidth(); }

    public int getLength() { return (int)(15 * weapon.getSizeBonus()); }

    @Override
    public Rectangle2D.Double[] getRectMask()
    {
        double r = getWidth() / 2;
        return new Rectangle2D.Double[] {
            new Rectangle2D.Double(getX() - r, getY() - r, getWidth(), getHeight()),
            new Rectangle2D.Double(stopX() - r, stopY() - r, getWidth(), getHeight())
        };
    }

    public double stopX() { return getX() + getLength() * Math.cos(getTheta()); }
    public double stopY() { return getY() + getLength() * Math.sin(getTheta()); }
}
