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
	private int speed;
	private double damage;
	private double x, y;

	// bullet trajectory
	protected double theta;

	// master panel
	protected Weapon weapon;

	public Bullet(double x, double y, double t, double dmg, Weapon w)
    {
		this.weapon = w;
		this.theta = t;
		this.x = x;
		this.y = y;

		setLocation(x, y);

		speed = getDefaultSpeed();
		damage = dmg;

		weapon.ship().panel().addProjectile(this);
	}

    @Override
	public void explode()
    {
		weapon.ship().panel().removeProjectile(this);
		new Explosion(getX(), getY(), weapon.ship().panel());
	}

    @Override
	public void move()
    {
		setLocation(
			x + Const.BULLET_SPEED * Math.cos(theta),
			y + Const.BULLET_SPEED * Math.sin(theta)
		);
	}

    @Override
	public void update()
    {
		if (weapon.ship().panel().contains(getX(), getY())) {
			Ship ship = weapon.ship().panel().intersects(this);
            boolean collision = ship != null;
            boolean validTarget = collision && (ship != weapon.ship() || collidesWithOwner());
			if (collision && validTarget) {
				ship.hit(damage);
				weapon.ship().target = ship;
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
		g.setColor(getColor());
		g.drawOval((int)getX(), (int)getY(), getWidth(), getHeight());
	}

	protected int getDefaultSpeed() { return 15; }
    public Color getColor() { return Color.YELLOW; }

	@Override public double getX() { return x; }
	@Override public double getY() { return y; }
	@Override public int getWidth() { return 1; }
	@Override public int getHeight() { return 1; }
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

	protected void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    protected Rectangle getRectangle()
    {
        return new Rectangle((int)getX() - getWidth() / 2,
                             (int)getY() - getHeight() / 2,
                             getWidth(),
                             getHeight());
    }
}
