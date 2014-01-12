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

// TODO: Extend from bullet...
public class LaserBeam implements Entity, Projectile
{
	private int length;
	private int speed;
	private double damage;
	private double theta;
    private int life;

	// start and end of beam
	protected Point2D.Double start, stop;

	// master panel
	protected Weapon weapon;

	public LaserBeam(double x, double y, double t, double dmg, Weapon w)
    {
		this.weapon = w;
		this.theta = t;

        life = 1;
		speed = getDefaultSpeed();
		length = getDefaultLength();
		damage = dmg;

		start = new Point2D.Double();
		stop = new Point2D.Double();

		setLocation(x, y);

		weapon.ship().panel().addProjectile(this);
	}

    @Override
	public void die()
    {
		new Explosion(stop.x, stop.y, weapon.ship().panel());
	}

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
        g.setColor(Color.WHITE);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Line2D.Double(start.x, start.y, stop.x, stop.y));
	}

    public int getDefaultSpeed() { return 15; }
    public int getDefaultLength() { return 10; }

	@Override public double getX() { return start.x; }
	@Override public double getY() { return start.y; }
	@Override public int getWidth() { return 1; }
	@Override public int getHeight() { return 1; }
    @Override public int getLife() { return life; }
	@Override public double getDamage() { return damage; }
    @Override public boolean collidesWithOwner() { return false; }

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
            return mask.intersects((int)start.x, (int)start.y, getWidth(), getHeight())
                || mask.intersects((int)stop.x, (int)stop.y, getWidth(), getHeight());
        }
        Rectangle2D.Double boundingBox = new Rectangle2D.Double(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        return boundingBox.intersects(start.x, start.y, getWidth(), getHeight())
            || boundingBox.intersects(stop.x, stop.y, getWidth(), getHeight());
	}

	private void setLocation(double x, double y)
    {
		start.setLocation(x, y);
		stop.setLocation(x + length * Math.cos(theta), y + length * Math.sin(theta));
	}
}
