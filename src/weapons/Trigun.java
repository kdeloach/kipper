package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;
import kipper.upgrades.*;

// Weapon used by TriangleMan
public class Trigun extends Weapon
{
	Image img;

	public Trigun(double x, double y, int rx, int ry, Ship s)
    {
		super(x, y, rx, ry, s);
		setSize(10, 10);
		img = Util.instance.loadImage("/assets/trigun_ico.gif");
	}

    @Override
	public void fireProjectile(double heading)
    {
		new Bullet(x - 1, y + height / 2, heading, getDamage(), this)
        {
            @Override
			public void draw(Graphics g)
            {
				g.setColor(Color.GREEN);
				g.drawOval((int)getX(), (int)getY(), 1, 1);
			}
		};
	}

	@Override public Image getIcon() { return img; }
	@Override public int getDefaultDamage() { return 1; }
	@Override public int getDefaultCooldown() { return 500; }

    @Override
	public void draw(Graphics g)
    {
        int px = (int)x;
        int py = (int)y;
		g.setColor(Color.RED);
		g.fillPolygon(
			new int[]{px, px + width, px + width},
			new int[]{py + height / 2, py, py + height},
			3
		);
	}
}
