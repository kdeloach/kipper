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
		super(x, y, 2, c);

        setDestination(getX() + getWidth() / 2, getY() + getHeight() / 2);

        Weapon w1 = new Trigun(getX(), getY(), 0, getHeight() / 2 - 5, this);
		w1.addUpgrade(new RotateAbility());
        // w1.addUpgrade(new CooldownUpgrade());
        // w1.addUpgrade(new CooldownUpgrade());
        // w1.addUpgrade(new CooldownUpgrade());
        // w1.addUpgrade(new CooldownUpgrade());
        // w1.addUpgrade(new CooldownUpgrade());
        // w1.addUpgrade(new CooldownUpgrade());
        // w1.addUpgrade(new CooldownUpgrade());

        Weapon w2 = new LightningGun(getX(), getY(), 0, getHeight() / 2 - 5, this);
        w2.addUpgrade(new CooldownUpgrade());
        w2.addUpgrade(new CooldownUpgrade());
        w2.addUpgrade(new CooldownUpgrade());
        w2.addUpgrade(new CooldownUpgrade());
        w2.addUpgrade(new CooldownUpgrade());

		equipWeapon(w1);
		equipWeapon(w2);
		//selectWeapon((int)(Math.random() * 2.0));
		selectWeapon(0);
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
        Ship player = osp.getPlayer();
		if (player == null) {
			getWeapon().stopFiring();
		} else {
			// shoot weapon
			if (getWeapon() != null && !getWeapon().isFiring()) {
				getWeapon().startFiring();
            }
			// get ready to fire here
            int px = (int)(player.getX() + player.getWidth() / 2);
            int py = (int)(player.getY() + player.getHeight() / 2);
			targetLocation(px, py);
		}
    }

	///

	@Override public int getWidth() { return 75; }
	@Override public int getHeight() { return 75; }
	@Override public String getName() { return "Triangle Man"; }
	@Override public int getOrientation() { return Const.FACE_LEFT; }
	@Override public int getMaxHp() { return 30; }
	@Override public int getSpeed() { return 5; }

	// Generated by mask tool
    @Override
	public Polygon getPolygonMask()
    {
		return new Polygon(
			new int[]{74,0,74},
			new int[]{0,36,73},
			3
		);
	}
}
