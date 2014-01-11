package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.Color;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;

// Bolts are basically 2 points, whereas the first point is x,y
//   and the second point is x+length*cos(theta),y+length*sin(theta)

public class Bolt implements Projectile
{
	// length is the size of 1 segment of the largest tier branches
	protected int branches, length, life, lifespan, thickness;

	protected double damage;

	// bullet trajectory
	protected double theta, offset;

	// start and end of beam
	protected Point2D.Double start, stop, contact;

	// master panel
	protected Weapon weapon;

	public Bolt(double x, double y, double t, double dmg, Weapon w)
    {
        x = x - w.ship().x;
        y = y - w.ship().y;
		new Bolt(x, y, t, getDefaultBranches(), getDefaultLength(), getDefaultLifespanTicks(), getDefaultThickness(), dmg, w);
	}

	public Bolt(double x, double y, double offset, int branches, int length, int lifespan, int thickness, double dmg, Weapon w)
    {
		this.offset = offset;
		this.branches = branches;
		this.length = length;
		this.life = lifespan;
		this.lifespan = lifespan;
        this.thickness = thickness;
		this.damage = dmg;
		this.weapon = w;

		start = new Point2D.Double();
		stop = new Point2D.Double();

		this.theta =  Math.toRadians(Math.random() * 90 - 45);
		this.theta += offset;

		setLocation(x, y);
		weapon.ship().panel().addProjectile(this);

		if (length > 10) {
			splinter();
        }
	}

	private void splinter()
    {
		double x = stop.x;
        double y = stop.y;
		for (int i = 0; i < branches; i++) {
            int branches = (int)(Math.random() * getAmtChildrenBranches());
			Bolt b = new Bolt(x, y, offset, branches, length - 10, lifespan, thickness - 1, damage, weapon);
			x = b.stop.x;
			y = b.stop.y;
		}
	}

    @Override
    public void move()
    {
    }

    @Override
	public void update()
    {
		if (life > 0) {
			Ship ship = weapon.ship().panel().intersects(this);
            boolean collision = ship != null;
            boolean validTarget = collision && (ship != weapon.ship() || collidesWithOwner());
			if (collision && validTarget) {
				ship.hit(damage);
				weapon.ship().target = ship;
				explode();
			}
            move();
			life--;
            return;
		}
		weapon.ship().panel().removeProjectile(this);
	}

    @Override
	public void explode()
    {
        life = 0;
		new Explosion(getX(), getY(), weapon.ship().panel())
        {
			@Override public Color getColor() { return Color.BLUE; }
		};
	}

    @Override
	public void draw(Graphics g)
    {
        int a = (int)Easing.easeInQuad(life, 0, 0xFF, lifespan);
		g.setColor(new Color(0xFF, 0xFF, 0xFF, a));
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thickness));
        g2.draw(new Line2D.Double(startX(), startY(), stopX(), stopY()));
	}

	private int getDefaultLifespanTicks()
    {
        return (int)((long)getDefaultLifespanMs() / OuterSpacePanel.FPS);
    }

	protected int getDefaultLifespanMs() { return 1000; }
	protected int getDefaultLength() { return 40; }
	protected int getDefaultBranches() { return 2; }
	protected int getAmtChildrenBranches(){ return 5; }
	protected int getDefaultThickness(){ return 4; }

	@Override public double getX() { return startX(); }
	@Override public double getY() { return startY(); }
	@Override public int getWidth() { return 1; }
	@Override public int getHeight() { return 1; }
    @Override public boolean collidesWithOwner() { return false; }

    @Override
	public boolean intersects(Ship s)
    {
		if(s.contains((int)startX(), (int)startY())) {
			contact = start;
			return true;
		} else if (s.contains((int)stopX(), (int)stopY())) {
			contact = stop;
			return true;
		}
		return false;
	}

    @Override
	public boolean intersects(Projectile p)
    {
		return p.contains(startX(), startY()) || p.contains(stopX(), stopY());
	}

    @Override
	public boolean contains(double x, double y)
    {
		return contains((int)x, (int)y);
	}

    @Override
	public boolean contains(int x, int y)
    {
		return new Rectangle.Double(startX(), startY(), 1, 1).contains(x, y)
            || new Rectangle.Double(stopX(), stopY(), 1, 1).contains(x, y);
	}

	protected void setLocation(double x, double y)
    {
		start.setLocation(x, y);
		stop.setLocation(x + length * Math.cos(theta), y + length * Math.sin(theta));
	}

    protected double startX() { return start.x + weapon.ship().x; }
    protected double startY() { return start.y + weapon.ship().y; }
    protected double stopX() { return stop.x + weapon.ship().x; }
    protected double stopY() { return stop.y + weapon.ship().y; }
}
