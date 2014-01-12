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

// LaserBeams are basically 2 points, whereas the first point is x,y
// and the second point is x+length*cos(theta),y+length*sin(theta)
public class LaserBeam extends Bullet
{
	private int length = 10;
	protected Point2D.Double stop;

	public LaserBeam(double x, double y, double theta, double dmg, Weapon w)
    {
        super(x, y, theta, dmg, w);
	}

    @Override
	public void die()
    {
		new Explosion(stop.x, stop.y, weapon.ship().panel());
	}

    @Override
	public void setLocation(double x, double y)
    {
		super.setLocation(x, y);
        if (stop == null) {
            stop = new Point2D.Double();
        }
		stop.setLocation(
            x + length * Math.cos(getTheta()),
            y + length * Math.sin(getTheta()));
	}

    @Override
	public void draw(Graphics g)
    {
         g.setColor(Color.WHITE);
         g.drawLine((int)getX(), (int)getY(), (int)stop.x, (int)stop.y);
	}

	@Override public int getWidth() { return 1; }
	@Override public int getHeight() { return 1; }
    @Override public boolean collidesWithOwner() { return false; }

    @Override
	public boolean intersects(Entity e)
    {
        return super.intersects(e)
            || intersectsPoint(e, stop.x, stop.y, getWidth(), getHeight());
	}
}
