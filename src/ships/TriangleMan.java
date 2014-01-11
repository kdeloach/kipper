package kipper.ships;

import java.awt.Image;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import kipper.*;
import kipper.effects.*;
import kipper.upgrades.*;
import kipper.weapons.*;

public class TriangleMan extends Ship
{
	public TriangleMan(double x, double y, OuterSpacePanel c)
    {
		super(x, y, c);

		setSize(75, 75);
        setDestination(x + width / 2, y + height / 2);

        Weapon w1 = new Trigun(x, y, 0, height / 2 - 5, this);
		w1.addUpgrade(new RotateAbility());

		//equipWeapon(new LightningGun(x,y,0,height/2-5,this));
		equipWeapon(w1);
		selectWeapon(0);
	}

    @Override
	public void die()
    {
		new ShipExplosion2(x + width / 2, y + height / 2, osp);
	}

    @Override
	public void draw(Graphics g)
    {
        int px = (int)x;
        int py = (int)y;
		g.setColor(Color.GREEN);
		g.fillPolygon(
			new int[]{px, px + width, px + width},
			new int[]{py + height / 2, py, py + height},
			3
		);
        if (getWeapon() != null) {
            getWeapon().draw(g);
        }
	}

    @Override
	public void think()
    {
        //doSomethingSmart();
	}

    private void doSomethingSmart()
    {
		if (osp.getPlayer() == null) {
			getWeapon().stopFiring();
		} else {
			// shoot weapon
			if (getWeapon() != null && !getWeapon().isFiring()) {
				getWeapon().startFiring();
            }
			// get ready to fire here
            int px = (int)osp.getPlayer().getX();
            int py = (int)osp.getPlayer().getY();
			targetLocation(px, py);
		}
    }

	///

	@Override public int getDefaultOrientation() { return Const.RIGHT_TO_LEFT; }
	@Override public String getName() { return "Triangle Man"; }
	@Override public int defaultTeam() { return Const.NPC; }
	@Override public int defaultMaxHp() { return 30; }
	@Override public int getDefaultSpeed() { return 5; }

	// ** generated with MaskMachine ** //
    @Override
	public Polygon getDefaultMask()
    {
		return new Polygon(
			new int[]{74,0,74},
			new int[]{0,36,73},
			3
		);
	}
}
