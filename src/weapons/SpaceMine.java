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
            for (Projectile p : weapon.ship.panel().bulletList) {
                if (p != this && p.intersects(this)) {
                    p.explode();
                    explode();
                }
            }
			return;
		}

		setLocation(
			dx + steps / Const.BULLET_SPEED * Math.cos(theta),
			dy + steps / Const.BULLET_SPEED * Math.sin(theta)
		);

		steps--;
	}

    @Override
	public void draw(Graphics g)
    {
        int px = (int)getX();
        int py = (int)getY();

		g.setColor(Color.GREEN);

		int off = width / 6;

		g.drawRect(px - width / 2 + off,
		           py - height / 2 + off,
		           width - off * 2,
		           height - off * 2);

		g.drawPolygon(
			new int[]{px - width / 2, px, px + width / 2, px},
			new int[]{py, py - height / 2, py, py + height / 2},
			4
		);
	}

	public int getDefaultSteps() { return 40; }

	@Override public int getDefaultSpeed() { return 15; }
	@Override public Dimension getSize() { return new Dimension(25, 25); }
    @Override public boolean collidesWithOwner() { return true; }
}
