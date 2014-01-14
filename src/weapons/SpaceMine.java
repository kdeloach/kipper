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
	private int ticks = 0;

	public SpaceMine(double x, double y, double t, double dmg, Weapon w)
    {
		super(x, y, t, dmg, w);
	}

    @Override
	public void move()
    {
        super.move();

		// Enable bullet collision
        // TODO: Move bullet collision into main game loop
		if (ticks > 40) {
            for (Projectile p : weapon.ship().panel().bulletList) {
                if (p != this && p.intersects(this)) {
                    p.hit(p.getLife());
                    hit(getLife());
                    break;
                }
            }
		}

		ticks++;
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

    // Halves speed every 500 MS
    private int getDecelerator()
    {
        int n = (int)Math.floor(ticks / Util.msToTicks(100)) + 1;
        return Math.min(n, 10);
    }

	@Override public int getWidth() { return 25; }
	@Override public int getHeight() { return 25; }
    @Override public double getSpeed() { return super.getSpeed() / getDecelerator(); }
    @Override public boolean collidesWithOwner() { return true; }
}
