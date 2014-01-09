package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;

public class Shooter extends Weapon
{
	Image icon;

	public Shooter(int x, int y, int rx, int ry, Ship c)
    {
		super(x,y,rx,ry,c);
		icon = Util.instance.loadImage("/assets/shooter_icon.gif");
	}

    @Override
	public Image getIcon()
    {
		return icon;
	}

    @Override
	public void draw(Graphics g)
    {
	}

    @Override
	public void fireProjectile(double heading)
    {
		new Bullet(x, y, heading, getDamage(), this);
	}

    @Override
	public int getDefaultDamage()
    {
		return 1;
	}

    @Override
	public int getDefaultCooldown()
    {
		return 250;
	}
}
