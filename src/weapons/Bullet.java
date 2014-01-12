package kipper.weapons;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;

public class Bullet implements Entity, Projectile
{
	private int speed;
    private int life;
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

        life = 1;
		speed = getDefaultSpeed();
		damage = dmg;

		weapon.ship().panel().addProjectile(this);
	}

    @Override
	public void die()
    {
		new Explosion(getX(), getY(), weapon.ship().panel());
	}

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
        Ship ship = weapon.ship().panel().intersects(this);
        boolean collision = ship != null;
        boolean validTarget = collision && (ship != weapon.ship() || collidesWithOwner());
        if (collision && validTarget) {
            weapon.ship().target = ship;
            ship.hit(getDamage());
            hit(getLife());
            return;
        }
        move();
	}

    @Override
	public void draw(Graphics g)
    {
		g.setColor(getColor());
		g.fillOval((int)getX(), (int)getY(), getWidth(), getHeight());
	}

	protected int getDefaultSpeed() { return 15; }
    public Color getColor() { return Color.YELLOW; }

	@Override public double getX() { return x; }
	@Override public double getY() { return y; }
	@Override public int getWidth() { return 6; }
	@Override public int getHeight() { return 6; }
    @Override public int getLife() { return life; }
    @Override public boolean collidesWithOwner() { return false; }
    @Override public double getDamage() { return damage; }

	@Override
    public boolean isAlive()
    {
        return life > 0 && weapon.ship().panel().contains(getX(), getY());
    }

    @Override
    public void hit(double damage)
    {
        if (isAlive()) {
            life -= damage;
            if (life <= 0) {
                die();
            }
        }
    }

    @Override
	public boolean intersects(Entity e)
    {
        if (!isAlive()) {
            return false;
        }
        if (e instanceof MaskedEntity) {
            Polygon tmp = ((MaskedEntity)e).getMask();
            Polygon mask = new Polygon(tmp.xpoints, tmp.ypoints, tmp.npoints);
            mask.translate((int)e.getX(), (int)e.getY());
            return mask.intersects(getX(), getY(), getWidth(), getHeight());
        }
        Rectangle2D.Double boundingBox = new Rectangle2D.Double(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        return boundingBox.intersects(getX(), getY(), getWidth(), getHeight());
	}

	protected void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
}
