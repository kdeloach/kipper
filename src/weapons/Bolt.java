package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;

// Bolts are basically 2 points, whereas the first point is x,y
//   and the second point is x+length*cos(theta),y+length*sin(theta)

// TODO: Extend from bullet...
public class Bolt implements Entity, Projectile
{
	// length is the size of 1 segment of the largest tier branches
	private int branches, length, life, lifespan, thickness;

	private double damage;

	// bullet trajectory
	private double theta, offset;

	// start and end of beam
	private Point2D.Double start, stop;

	// master panel
	private Weapon weapon;

    private boolean alive = true;

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
        life--;
	}

    @Override
	public void die()
    {
		new Explosion(getX(), getY(), weapon.ship().panel())
        {
			@Override public Color getColor() { return Color.WHITE; }
		};
	}

    @Override
	public void draw(Graphics g)
    {
        int a = (int)Easing.easeInQuad(life, 0, 0xFF, lifespan);
		g.setColor(new Color(0xFF, 0xFF, 0xFF, a));
        if (thickness == 1) {
            g.drawLine((int)startX(), (int)startY(), (int)stopX(), (int)stopY());
        } else {
            drawThickLine(g, startX(), startY(), stopX(), stopY(), thickness);
        }
	}


    // Note: Using Graphics2D to draw shape objects is SLOOOW
    // Source: http://www.rgagnon.com/javadetails/java-0260.html
    public void drawThickLine(Graphics g, double x1, double y1, double x2, double y2, int thickness)
    {
        double dX = x2 - x1;
        double dY = y2 - y1;
        // line length
        double lineLength = Math.sqrt(dX * dX + dY * dY);

        double scale = ((double)thickness) / (2.0 * lineLength);

        // The x,y increments from an endpoint needed to create a rectangle...
        double ddx = -scale * dY;
        double ddy = scale * dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        int dx = (int)ddx;
        int dy = (int)ddy;

        // Now we can compute the corner points...
        int xPoints[] = new int[4];
        int yPoints[] = new int[4];

        xPoints[0] = (int)x1 + dx; yPoints[0] = (int)y1 + dy;
        xPoints[1] = (int)x1 - dx; yPoints[1] = (int)y1 - dy;
        xPoints[2] = (int)x2 - dx; yPoints[2] = (int)y2 - dy;
        xPoints[3] = (int)x2 + dx; yPoints[3] = (int)y2 + dy;

        g.fillPolygon(xPoints, yPoints, 4);
    }

    @Override
    public void hit(double damage)
    {
        if (isAlive()) {
            // Life ticks and damage may not exactly be compatible types but we'll ignore this for now
            life -= damage;
            if (life <= 0) {
                die();
            }
        }
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
    @Override public int getLife() { return life; }
	@Override public boolean isAlive() { return life > 0; }
	@Override public double getDamage() { return damage; }
    @Override public boolean collidesWithOwner() { return false; }

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
            return mask.intersects((int)startX(), (int)startY(), getWidth(), getHeight())
                || mask.intersects((int)stopX(), (int)stopY(), getWidth(), getHeight());
        }
        Rectangle2D.Double boundingBox = new Rectangle2D.Double(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        return boundingBox.intersects(startX(), startY(), getWidth(), getHeight())
            || boundingBox.intersects(stopX(), stopY(), getWidth(), getHeight());
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
