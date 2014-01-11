package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;

public class Bullet implements Projectile
{
	protected int width, height;
	private int speed;
	private double damage;
	protected double dx, dy;

	// bullet trajectory
	protected double theta;

	// master panel
	protected Weapon weapon;

	public Bullet(double x, double y, double t, double dmg, Weapon w)
    {
		this.weapon = w;
		this.theta = t;
		dx = x;
		dy = y;

		setLocation(x, y);

		if (getSize() != null) {
			setSize(getSize().width, getSize().height);
        }

		speed = getDefaultSpeed();
		damage = dmg;

		weapon.ship().panel().addProjectile(this);
	}

    @Override
	public void explode()
    {
		weapon.ship().panel().removeProjectile(this);
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
	public void update()
    {
		if (weapon.ship.panel().contains(getX(), getY())) {
			Ship ship = weapon.ship().panel().intersects(this);
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
		g.setColor(Color.YELLOW);
		g.drawOval((int)getX(), (int)getY(), width, height);
	}

	protected int getDefaultSpeed() { return 15; }
	protected Dimension getSize() { return new Dimension(1, 1); }

	@Override public double getX(){ return dx; }
	@Override public double getY(){ return dy; }
    @Override public boolean collidesWithOwner() { return false; }

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

	protected void setLocation(double x, double y)
    {
        dx = x;
        dy = y;
    }

    protected Rectangle getRectangle()
    {
        return new Rectangle((int)getX() - width / 2,
                             (int)getY() - height / 2,
                             width,
                             height);
    }
}
