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

public class LaserBeam implements Projectile
{
	protected int length;
	private int speed;
	private double damage;
	protected double theta;

	// start and end of beam
	protected Point2D.Double start, stop, contact;

	// master panel
	protected Weapon weapon;

	public LaserBeam(double x, double y, double t, double dmg, Weapon w)
    {
		this.weapon = w;
		this.theta = t;

		speed = getDefaultSpeed();
		length = getDefaultLength();
		damage = dmg;

		start = new Point2D.Double();
		stop = new Point2D.Double();

		setLocation(x, y);

		weapon.ship.panel().addProjectile(this);
	}

    @Override
	public void explode()
    {
		weapon.ship.panel().removeProjectile(this);
		new Explosion(contact.x, contact.y, weapon.ship.panel());
	}

    @Override
	public void move()
    {
		start.x += Const.BULLET_SPEED * Math.cos(theta);
		start.y += Const.BULLET_SPEED * Math.sin(theta);
		stop.x += Const.BULLET_SPEED * Math.cos(theta);
		stop.y += Const.BULLET_SPEED * Math.sin(theta);
	}

    @Override
	public void update()
    {
		if (weapon.ship().panel().contains(getX(), getY())) {
			Ship ship = weapon.ship.panel().intersects(this);
            boolean collision = ship != null;
            boolean validTarget = collision && (ship != weapon.ship() || collidesWithOwner());
			if (collision && validTarget) {
                ship.hit(damage);
                weapon.ship.target = ship;
                explode();
                return;
			}
			move();
            return;
		}
        weapon.ship().panel().removeProjectile(this);
	}

    @Override
	public void draw(Graphics g)
    {
		g.setColor(Color.WHITE);
		g.drawLine((int)start.x, (int)start.y, (int)stop.x, (int)stop.y);
	}

    public int getDefaultSpeed() { return 15; }
    public int getDefaultLength() { return 10; }

	@Override public double getX() { return start.x; }
	@Override public double getY() { return start.y; }
    @Override public boolean collidesWithOwner() { return false; }

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

    @Override
    public boolean intersects(Projectile p)
    {
        System.out.println("LaserBeam.intersects not implemented yet");
        return false;
    }

    @Override
    public boolean contains(int x, int y)
    {
        System.out.println("LaserBeam.contains not implemented yet");
        return false;
    }

    @Override public boolean contains(double x, double y)
    {
        System.out.println("LaserBeam.contains not implemented yet");
        return false;
    }

	private void setLocation(double x, double y)
    {
		start.setLocation(x, y);
		stop.setLocation(x + length * Math.cos(theta), y + length * Math.sin(theta));
	}
}
