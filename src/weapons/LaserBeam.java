package kipper.weapons;

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

public class LaserBeam extends Bullet
{
	private int length = 15;
	protected Point2D.Double stop;

	public LaserBeam(double x, double y, double theta, double dmg, Weapon w)
    {
        super(x, y, theta, dmg, w);
	}

    @Override
	public void die()
    {
		new Explosion(stop.x + getWidth() / 2,
                      stop.y + getHeight() / 2,
                      weapon.ship().panel());
	}

    @Override
	public void setLocation(double x, double y)
    {
		super.setLocation(x, y);
        if (stop == null) {
            stop = new Point2D.Double();
        }
		stop.setLocation(
            x + length * weapon.getSizeBonus() * Math.cos(getTheta()),
            y + length * weapon.getSizeBonus() * Math.sin(getTheta()));
	}

    @Override
	public void draw(Graphics g)
    {
        g.setColor(Color.WHITE);
        Util.drawThickLine(g, getX(), getY(), stop.x, stop.y, getWidth());
	}

    // Width and height of each individual point on the beam, NOT the total width and height of the beam.
	@Override public int getWidth() { return (int)(3 * weapon.getSizeBonus()); }
	@Override public int getHeight() { return getWidth(); }
    @Override public boolean collidesWithOwner() { return false; }

    @Override
	public boolean intersects(Entity e)
    {
        return super.intersects(e)
            || intersectsPoint(e, stop.x, stop.y, getWidth(), getHeight());
	}
}
