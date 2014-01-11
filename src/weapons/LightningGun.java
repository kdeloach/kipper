package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;

// WHEN this gun upgrades you get POINTS
// You can use these points to select how you want to upgrade the weapon
//   WITH the LightningGun Calibrator
// Basically, the calibrator will be several Sliders, ex:
//
// Length:   short <----0---------> long
// Branches: few   <-------0------> many
// Children: few   <----0---------> many
// Span:     short <--0-----------> long
//
// The max value of any individual sliders is the amount of points you have
// Edit these properties of the gun BY removing all natural upgrades onSave and then re-applying them
//

public class LightningGun extends Weapon
{
	Image icon;

	public LightningGun(double x, double y, int rx, int ry, Ship c)
    {
		super(x, y, rx, ry, c);
		icon = Util.instance.loadImage("/assets/lightning_icon.gif");
	}

	@Override public Image getIcon() { return icon; }
	@Override public int getDefaultDamage() { return 1; }
	@Override public int getDefaultCooldown() { return 200; }

    @Override
	public void draw(Graphics g)
    {
	}

    @Override
	public void fireProjectile(double heading)
    {
		new Bolt(x, y, heading, getDamage(), this);
	}
}
