package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;

public class LaserGun extends Weapon
{
	Image icon;

	public LaserGun(double x, double y, int rx, int ry, Ship c)
    {
		super(x, y, rx, ry, c);
		icon = Util.instance.loadImage("/assets/laser_icon.gif");
	}

	@Override public Image getIcon() { return icon; }
	@Override public int getDefaultDamage() { return 3; }
	@Override public int getDefaultCooldown() { return 200; }

    @Override
	public void draw(Graphics g)
    {
    }

    @Override
	public void fireProjectile(double heading)
    {
		new LaserBeam(x, y, heading, getDamage(), this);
	}
}
