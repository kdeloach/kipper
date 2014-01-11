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
                    p.explode();
                    explode();
                }
            }
			return;
		}

		setLocation(
			getX() + steps / Const.BULLET_SPEED * Math.cos(theta),
			getY() + steps / Const.BULLET_SPEED * Math.sin(theta)
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

		g.drawRect(px - getWidth() / 2 + off,
		           py - getHeight() / 2 + off,
		           getWidth() - off * 2,
		           getHeight() - off * 2);

		g.drawPolygon(
			new int[]{px - getWidth() / 2, px, px + getWidth() / 2, px},
			new int[]{py, py - getHeight() / 2, py, py + getHeight() / 2},
			4
		);
	}

	public int getDefaultSteps() { return 40; }

	@Override public int getDefaultSpeed() { return 15; }
	@Override public int getWidth() { return 25; }
	@Override public int getHeight() { return 25; }
    @Override public boolean collidesWithOwner() { return true; }
}
