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

    @Override
	public void explode()
    {
		weapon.ship().panel().unregisterProjectile(this);
		new Explosion(getX(), getY(), weapon.ship.panel());
	}

    @Override
	public void move()
    {
		setLocation(
			dx + Const.BULLET_SPEED * Math.cos(theta),
			dy + Const.BULLET_SPEED * Math.sin(theta)
		);
	}

    @Override
	public void run()
    {
		while (weapon.ship.panel().contains(getX(), getY()) && registered()) {
			Ship o = weapon.ship().panel().intersects(this);
			if (o != null && o.getId() != weapon.ship().getId()) {
				o.hit(damage, getX(), getY());
				weapon.ship.target = o;
				explode();
				break;
			}
			move();
	 		try { Thread.sleep(speed); } catch (Exception ie) {}
		}
        weapon.ship().panel().unregisterProjectile(this);
	}

    @Override
	public void draw(Graphics g)
    {
		g.setColor(Color.YELLOW);
		g.drawOval(getX(), getY(), width, height);
	}

	protected int getDefaultSpeed() { return 15; }
	protected Dimension getSize() { return new Dimension(1, 1); }

	@Override public int getX(){ return (int)dx; }
	@Override public int getY(){ return (int)dy; }
	@Override public int getId(){ return id; }
    @Override public void setId(int k){ id = k; }

    @Override
	public boolean intersects(Ship s)
    {
		return s.intersects(getRectangle());
	}

    @Override
	public boolean intersects(Projectile p)
    {
		return p.contains(getX(), getY());
	}

    @Override
	public boolean contains(double x, double y)
    {
		return contains((int)x, (int)y);
	}

    @Override
	public boolean contains(int x, int y)
    {
		return getRectangle().contains(x,y);
	}

	protected void setSize(int w, int h)
    {
        this.width = w;
        this.height = h;
    }

	protected void setLocation(int x, int y)
    {
        setLocation((double)x, (double)y);
    }

	protected void setLocation(double x, double y)
    {
        dx = x;
        dy = y;
    }

    protected Rectangle getRectangle()
    {
        return new Rectangle(getX() - width / 2, getY() - height / 2, width, height);
    }
    
    // TODO: Remove
	private int getDefaultTeam() { return Const.PLAYER; }
	private boolean registered(){ return id!=Const.UNREGISTERED; }
}