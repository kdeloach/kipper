package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;

public class Blaster extends Shooter
{
	Image icon;

	public Blaster(int x, int y, int rx, int ry, Ship c)
    {
		super(x,y,rx,ry,c);

		addUpgrade(new RecoilAbility());
		addUpgrade(new SpreadAbility());
		addUpgrade(new SpreadAbility());

		icon = Util.instance.loadImage("/assets/blaster_icon.gif");
	}

    @Override
	public Image getIcon()
    {
		return icon;
	}
}
