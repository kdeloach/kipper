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

		equipWeapon(new Shooter(getX(), getY(), getWidth(), getHeight() / 2 + 4, this));
        // The shooter can become a Blaster easily so there's no point to this gun
		//equipWeapon(new Blaster(x, y, width, height / 2 + 4, this));
		equipWeapon(new LaserGun(getX(), getY(), getWidth(), getHeight() / 2 + 4, this));
		equipWeapon(new MineLauncher(getX(), getY(), getWidth() + 15, getHeight() / 2 + 4, this));
		equipWeapon(new LightningGun(getX(), getY(), getWidth(), getHeight() / 2 + 6, this));
		selectWeapon(0);

		img = Util.instance.loadImage("/assets/enterprise2.gif");
	}

    @Override
	public void die()
    {
        super.die();
		new ShipExplosion1(getX() + getWidth() / 2, getY() + getHeight() / 2, osp)
        {
			@Override public Color getColor() { return Color.YELLOW; }
			@Override public int getAmount() { return 50; }
		};
		new ShipExplosion1(getX() + getWidth() / 2, getY() + getHeight() / 2, osp)
        {
			@Override public Color getColor() { return Color.RED; }
		};
        panel.respawnPlayer();
	}

    @Override
	public void draw(Graphics g)
    {
        super.draw(g);
		g.drawImage(img, (int)getX(), (int)getY(), osp);
        if (getWeapon() != null) {
            getWeapon().draw(g);
        }
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

	@Override public int getWidth() { return 141; }
	@Override public int getHeight() { return 52; }
	@Override public String getName() { return "ENTERPRISE";}
	@Override public int defaultTeam() { return Const.PLAYER; }
    @Override public int getDefaultOrientation() { return Const.LEFT_TO_RIGHT; }
	@Override public int getDefaultSpeed() { return 30; }
	@Override public int defaultMaxHp() { return 50; }
}
