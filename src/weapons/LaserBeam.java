package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.geom.Point2D;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;

// LaserBeams are basically 2 points, whereas the first point is x,y
// and the second point is x+length*cos(theta),y+length*sin(theta)

public class LaserBeam implements Projectile, Runnable
{
	protected int length;
	private int speed;
	private double damage;
	protected double theta;

	// id of bullet, is set by master panel
	protected int id;

	// start and end of beam
	protected Point2D.Double start, stop, contact;

	// master panel
	protected Weapon weapon;

	public LaserBeam(int x, int y, double t, double dmg, Weapon w)
    {
		this.weapon = w;
		this.theta = t;

		speed = getDefaultSpeed();
		this.length = getDefaultLength();
		damage = dmg;

		start = new Point2D.Double(x, y);
		stop = new Point2D.Double(x + length * Math.cos(theta), y + length * Math.sin(theta));

		setLocation(x, y);
		weapon.ship.panel().registerProjectile(this);

		new Thread(this).start();
	}

    @Override
	public void explode()
    {
		weapon.ship.panel().unregisterProjectile(this);
		new Explosion((int)contact.x, (int)contact.y, weapon.ship.panel());
	}

    @Override
	public void move()
    {
		start.x += Const.BULLET_SPEED * Math.cos(theta);
		start.y += Const.BULLET_SPEED * Math.sin(theta);
		stop.x  += Const.BULLET_SPEED * Math.cos(theta);
		stop.y  += Const.BULLET_SPEED * Math.sin(theta);
	}

    @Override
	public void run()
    {
		while (weapon.ship().panel().contains(getX(), getY())) {
			Ship o = weapon.ship.panel().intersects(this);
			if(o != null && o.getId() != weapon.ship().getId()) {
				o.hit(damage,(int)contact.x, (int)contact.y);
				weapon.ship.target=o;
				explode();
				break;
			}
			move();
			try{ Thread.sleep(speed); } catch (Exception ie) {}
		}
        weapon.ship().panel().unregisterProjectile(this);
	}

    @Override
	public void draw(Graphics g)
    {
		g.setColor(Color.WHITE);
		g.drawLine((int)start.x, (int)start.y, (int)stop.x, (int)stop.y);
	}

    public int getDefaultSpeed() { return 15; }
    public int getDefaultLength() { return 10; }

	@Override public int getX(){ return (int)start.x; }
	@Override public int getY(){ return (int)start.y; }
	@Override public int getId(){ return id; }

    @Override
	public boolean intersects(Ship s)
    {
		if (s.contains((int)start.x, (int)start.y)) {
			contact = start;
			return true;
		} else if (s.contains((int)stop.x, (int)stop.y)) {
			contact = stop;
			return true;
		}
		return false;
	}

    @Override public boolean intersects(Projectile p) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public boolean contains(int x, int y) { throw new UnsupportedOperationException("Not implemented yet"); }
    @Override public boolean contains(double x, double y) { throw new UnsupportedOperationException("Not implemented yet"); }

	private void setLocation(int x, int y)
    {
        setLocation((double)x, (double)y);
    }

	private void setLocation(double x, double y)
    {
		start.setLocation(x,y);
		stop.setLocation(x + length * Math.cos(theta), y + length * Math.sin(theta));
	}

	@Override public void setId(int k){ id = k; }
}
