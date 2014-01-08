package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;

public class Bullet implements Projectile, Runnable
{
	protected int width, height;
	private int speed;
	private double damage;
	protected double dx, dy;

	// id of bullet,is set by master panel
	protected int id;

	// bullet trajectory
	protected double theta;

	// master panel
	protected Weapon weapon;

	public Bullet(int x, int y, double t, double dmg, Weapon w)
    {
		this.weapon=w;
		this.theta=t;
		dx=x;
		dy=y;

		setLocation(x, y);

		if (getSize() != null)
			setSize(getSize().width, getSize().height);

		speed = getDefaultSpeed();
		damage = dmg;

		weapon.ship().panel().registerProjectile(this);

		new Thread(this).start();
	}

	public void explode()
    {
		weapon.ship().panel().unregisterProjectile(this);
		die();
	}

	public void move()
    {
		setLocation(
			dx + Const.BULLET_SPEED * Math.cos(theta),
			dy + Const.BULLET_SPEED * Math.sin(theta)
		);
	}

	public void run()
    {
		while (weapon.ship.panel().contains(getX(), getY()) && registered()) {
			Ship o = weapon.ship().panel().intersects(this);
			if (o != null && o.getId() != weapon.ship().getId()) {
				o.hit(getDamage(), getX(), getY());
				weapon.ship.target = o;
				explode();
				break;
			}
			move();
	 		try { Thread.sleep(getSpeed()); } catch (Exception ie) {}
		}
        weapon.ship().panel().unregisterProjectile(this);
	}

	public void draw(Graphics g)
    {
		g.setColor(Color.YELLOW);
		g.drawOval(getX(), getY(), width, height);
	}

	public void die()
    {
		new Explosion(getX(), getY(), panel());
	}

	public int getDefaultSpeed() { return 15; }
	public Dimension getSize() { return new Dimension(1, 1); }
	public int getDefaultTeam() { return Const.PLAYER; }

	public boolean registered(){ return id!=Const.UNREGISTERED; }
	public double getDamage(){ return damage; }
	public int getSpeed(){ return speed; }
	public int getX(){ return (int)dx; }
	public int getY(){ return (int)dy; }
	public int getId(){ return id; }
	Rectangle getRectangle(){ return new Rectangle(getX() - width / 2, getY() - height / 2, width, height); }

	public boolean intersects(Ship s)
    {
		return s.intersects(getRectangle());
	}

	public boolean intersects(Projectile p)
    {
		return p.contains(getX(), getY());
	}

	public boolean contains(double x, double y)
    {
		return contains((int)x, (int)y);
	}

	public boolean contains(int x, int y)
    {
		return getRectangle().contains(x,y);
	}

	public void setSize(int w,int h) { this.width = w; this.height = h; }
	public void setLocation(int x,int y){ setLocation((double)x, (double)y); }
	public void setLocation(double x,double y){ dx = x; dy = y; }
	public void setId(int k){ id = k; }

	public OuterSpacePanel panel() { return weapon().ship().panel(); }
	public Weapon weapon(){ return weapon; }
	public Ship ship(){ return weapon().ship(); }
}