package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Point;
import java.awt.Color;
import kipper.*;
import kipper.effects.*;

public class SpaceMine extends Bullet
{
	// amount of steps to take before resting
	private int steps;

	public SpaceMine(double x, double y, double t, double dmg, Weapon w)
    {
		super(x, y, t, dmg, w);
		steps = getDefaultSteps();
	}

    @Override
	public void move()
    {
		// we want to stop moving at some point
		if (steps <= 0) {
            for (Projectile p : weapon.ship().panel().bulletList) {
                if (p != this && p.intersects(this)) {
                    p.hit(p.getLife());
                    hit(getLife());
                    break;
                }
            }
			return;
		}

		setLocation(
			getX() + steps / Const.BULLET_SPEED * Math.cos(getTheta()),
			getY() + steps / Const.BULLET_SPEED * Math.sin(getTheta())
		);

		steps--;
	}

    @Override
	public void draw(Graphics g)
    {
        int px = (int)getX();
        int py = (int)getY();

		g.setColor(Color.GREEN);

		int off = getWidth() / 6;

		g.fillPolygon(
			new int[]{px, px + getWidth() / 2, px + getWidth(), px + getWidth() / 2},
			new int[]{py + getHeight() / 2, py, py + getHeight() / 2, py + getHeight()},
			4
		);
		g.fillRect(px + off,
		           py + off,
		           getWidth() - off * 2,
		           getHeight() - off * 2);
	}

	public int getDefaultSteps() { return 40; }

	@Override public int getWidth() { return 25; }
	@Override public int getHeight() { return 25; }
    @Override public boolean collidesWithOwner() { return true; }
}
