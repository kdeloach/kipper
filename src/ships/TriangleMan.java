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

        setDestination(getX() + getWidth() / 2, getY() + getHeight() / 2);

        Weapon w1 = new Trigun(getX(), getY(), 0, getHeight() / 2 - 5, this);
		w1.addUpgrade(new RotateAbility());

        Weapon w2 = new LightningGun(getX(), getY(), 0, getHeight() / 2 - 5, this);
        w2.addUpgrade(new CooldownUpgrade());
        w2.addUpgrade(new CooldownUpgrade());
        w2.addUpgrade(new CooldownUpgrade());
        w2.addUpgrade(new CooldownUpgrade());
        w2.addUpgrade(new CooldownUpgrade());

		equipWeapon(w1);
		equipWeapon(w2);
		selectWeapon((int)(Math.random() * 2.0));
	}

    @Override
	public void die()
    {
        super.die();
		new ShipExplosion2(getX() + getWidth() / 2, getY() + getHeight() / 2, osp);
	}

    @Override
	public void draw(Graphics g)
    {
        super.draw(g);
        int px = (int)getX();
        int py = (int)getY();
		g.setColor(Color.GREEN);
		g.fillPolygon(
			new int[] {px, px + getWidth(), px + getWidth()},
			new int[] {py + getHeight() / 2, py, py + getHeight()},
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

	@Override public int getWidth() { return 75; }
	@Override public int getHeight() { return 75; }
	@Override public String getName() { return "Triangle Man"; }
    // TODO: Do we need this?
	@Override public int getDefaultOrientation() { return Const.RIGHT_TO_LEFT; }
    // TODO: Remove
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
