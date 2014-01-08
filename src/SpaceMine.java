import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Point;
import java.awt.Color;

public class SpaceMine extends Bullet
{
	// amount of steps to take before resting
	private int steps;

	public SpaceMine(int x, int y, double t, double dmg, Weapon w)
    {
		super(x, y, t, dmg, w);
		steps = getDefaultSteps();
	}

	public void move()
    {
		// we want to stop moving at some point
		if (steps <= 0) {
			try {
				for (Projectile p : weapon.ship.panel().bulletList) {
					if (p instanceof SpaceMine && p.getId() != getId() && p.intersects(this)) {
						p.explode();
						explode();
					}
                }
			} catch (java.util.ConcurrentModificationException ie) {}
			return;
		}

		setLocation(
			dx + steps / Const.BULLET_SPEED * Math.cos(theta),
			dy + steps / Const.BULLET_SPEED * Math.sin(theta)
		);

		steps--;
	}

	public void draw(Graphics g)
    {
		g.setColor(Color.GREEN);

		int off = width / 6;
		//g.drawOval(getX()-width/2,getY()-height/2,width,height);

		g.drawRect(getX() - width / 2 + off,
		           getY() - height / 2 + off,
		           width - off * 2,
		           height - off * 2);

		g.drawPolygon(
			new int[]{getX() - width / 2, getX(), getX() + width / 2, getX()},
			new int[]{getY(), getY() - height / 2, getY(), getY() + height / 2},
			4
		);
	}

	public void die()
    {
		new Explosion(getX(), getY(), panel());
	}

	public int getDefaultSpeed()
    {
		return 15;
	}

	public int getDefaultSteps()
    {
		return 40;
	}

	public Dimension getSize()
    {
		return new Dimension(25,25);
	}

	public int getDefaultTeam()
    {
		return Const.PLAYER;
	}
}