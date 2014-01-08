package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.Color;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;

// Bolts are basically 2 points, whereas the first point is x,y
//   and the second point is x+length*cos(theta),y+length*sin(theta)

public class Bolt implements Projectile, Runnable
{
	// length is the size of 1 segment of the largest tier branches
	protected int branches, length, span;

	// id of bullet,is set by master panel
	protected int id;

	protected double damage;

	// bullet trajectory
	protected double theta, offset;

	// start and end of beam
	protected Point2D.Double start, stop, contact;

	// master panel
	protected Weapon weapon;

	public Bolt(int x, int y, double t, double dmg, Weapon w)
    {
		new Bolt(x, y, t, getDefaultBranches(), getDefaultLength(), getDefaultSpan(), dmg, w);
	}

	public Bolt(double x, double y, double offset, int branches, int length, int span, double dmg, Weapon w)
    {
		this.weapon = w;
		this.length = length;
		this.branches = branches;
		this.damage = dmg;
		this.span = span;
		this.offset = offset;

		start = new Point2D.Double();
		stop = new Point2D.Double();

		// add offset here
		this.theta =  Math.toRadians(Math.random() * 90 - 45);
		this.theta += offset;

		setLocation(x, y);
		weapon.ship.panel().registerProjectile(this);

		if (length > 10) {
			splinter();
        }

		new Thread(this).start();
	}

	final public void splinter()
    {
		double x = stop.x;
        double y = stop.y;
		for (int i = 0; i < branches; i++) {
            int branches = (int)(Math.random() * getAmtChildrenBranches());
			Bolt b = new Bolt(x, y, offset, branches, length - 10, span, damage, weapon);
			x = b.stop.x;
			y = b.stop.y;
		}
	}

	final public void run()
    {
		while (span >= 0) {
			Ship o = weapon.ship.panel().intersects(this);
			if (o != null && o.getId() != weapon.ship().getId()) {
				o.hit(getDamage(), (int)contact.x, (int)contact.y);
				weapon.ship.target = o;
				explode();
				break;
			}
			try { Thread.sleep(50); } catch (Exception ie) {}
			span -= 10;
		}
		weapon.ship().panel().unregisterProjectile(this);
	}

	public void explode()
    {
		weapon.ship().panel().unregisterProjectile(this);
		die();
	}

	public void die()
    {
		new Explosion(getX(), getY(), panel())
        {
			public Color getColor()
            {
				return Color.BLUE;
			}
		};
	}

	public int getDefaultSpan() { return 10; }
	public int getDefaultLength() { return 40; }
	public int getDefaultBranches() { return 2; }
	public int getAmtChildrenBranches(){ return 5; }
	public int getDefaultTeam() { return Const.PLAYER; }

	public void draw(Graphics g)
    {
		g.setColor(Color.WHITE);
		g.drawLine((int)start.x, (int)start.y, (int)stop.x, (int)stop.y);
	}

	final public boolean registered() { return id != Const.UNREGISTERED; }
	final public int getSpan() { return span; }
	final public double getDamage() { return damage; }
	final public int getX() { return (int)start.x; }
	final public int getY() { return (int)start.y; }
	final public int getId() {return id; }

	final public boolean intersects(Ship s)
    {
		if(s.contains((int)start.x, (int)start.y)) {
			contact = start;
			return true;
		} else if (s.contains((int)stop.x, (int)stop.y)) {
			contact = stop;
			return true;
		}
		return false;
	}

	final public boolean intersects(Projectile p)
    {
		return p.contains(start.x, start.y) || p.contains(stop.x, stop.y);
	}

	final public boolean contains(double x, double y)
    {
		return contains((int)x, (int)y);
	}

	final public boolean contains(int x, int y)
    {
		return new Rectangle.Double(start.x, start.y, 1, 1).contains(x, y) || new Rectangle.Double(stop.x, stop.y, 1, 1).contains(x, y);
	}

	private void setLocation(int x, int y)
    {
		setLocation((double)x, (double)y);
	}

	final void setLocation(double x,double y)
    {
		start.setLocation(x, y);
		stop.setLocation(x + length * Math.cos(theta), y + length * Math.sin(theta));
	}

	final public void setId(int k) { id = k; }
	final public OuterSpacePanel panel() { return weapon().ship().panel(); }
	final public Weapon weapon() { return weapon; }
	final public Ship ship() { return weapon().ship(); }
}
