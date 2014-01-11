package kipper.ships;

import java.awt.Image;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import kipper.*;
import kipper.effects.*;
import kipper.weapons.*;

public class Enterprise extends Ship
{
	Image img;
    OuterSpacePanel panel;

	public Enterprise(int x, int y, OuterSpacePanel c)
    {
		super(x, y, c);
        panel = c;

		setSize(141, 52);

		equipWeapon(new Shooter(x, y, width, height / 2 + 4, this));
		equipWeapon(new Blaster(x, y, width, height / 2 + 4, this));
		equipWeapon(new LaserGun(x, y, width, height / 2 + 4, this));
		equipWeapon(new MineLauncher(x, y, width + 15, height / 2 + 4, this));
		equipWeapon(new LightningGun(x, y, width, height / 2 + 6, this));
		selectWeapon(0);

		img = Util.instance.loadImage("/assets/enterprise2.gif");
	}

    @Override
	public void die()
    {
		new ShipExplosion1(x + width / 2, y + height / 2, osp)
        {
			@Override public Color getColor() { return Color.YELLOW; }
			@Override public int getTicks() { return 10; }
			@Override public int getAmount() { return 25; }
		};
		new ShipExplosion1(x + width / 2, y + height / 2, osp)
        {
			@Override public Color getColor() { return Color.RED; }
		};
	}

    @Override
    public void explode()
    {
        super.explode();
        panel.respawnPlayer();
    }

    @Override
	public void draw(Graphics g)
    {
		g.drawImage(img, (int)x, (int)y, osp);
		getWeapon().draw(g);
	}

	// ** generated with MaskMachine ** //
    @Override
	public Polygon getDefaultMask()
    {
		return new Polygon(
			new int[]{0,73,94,141,133,102,87,70,26,49,37,24},
			new int[]{3,0,4,32,44,51,51,47,48,38,19,9},
			12
		);
	}

	@Override public String getName() { return "ENTERPRISE";}
	@Override public int defaultTeam() { return Const.PLAYER; }
    @Override public int getDefaultOrientation() { return Const.LEFT_TO_RIGHT; }
	@Override public int getDefaultSpeed() { return 50; }
	@Override public int defaultMaxHp() { return 50; }
}
