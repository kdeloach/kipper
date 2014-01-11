package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;

public class MineLauncher extends Weapon
{
	Image icon;

	public MineLauncher(int x, int y, int rx, int ry, Ship c)
    {
		super(x, y, rx, ry, c);
		addUpgrade(new RecoilAbility());
		icon = Util.instance.loadImage("/assets/mines_icon.gif");
	}

	@Override public Image getIcon() { return icon; }
	@Override public int getDefaultDamage() { return 5; }
	@Override public int getDefaultCooldown() { return 300; }

    @Override
	public void draw(Graphics g)
    {
	}

    @Override
	public void fireProjectile(double heading)
    {
		new SpaceMine(x, y, heading, getDamage(), this);
	}
}
