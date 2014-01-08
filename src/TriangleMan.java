package kipper;

import java.awt.Image;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import kipper.upgrades.*;
import kipper.weapons.*;

public class TriangleMan extends Ship {

	public TriangleMan(int x, int y, OuterSpacePanel c){
		super(x,y,c);

		setSize(75,75);
		move(getX(),getY());

		//equipWeapon(new LightningGun(x,y,0,height/2-5,this));
		equipWeapon(new Trigun(x,y,0,height/2-5,this));
		selectWeapon(0);

		addUpgrade(new RotateAbility(this));
	}

	public void die(){
		new ShipExplosion2(x+width/2,y+height/2,osp);
	}

	public void draw(Graphics g){
		g.setColor(Color.GREEN);
		g.fillPolygon(
			new int[]{ x, x+width, x+width },
			new int[]{ y+height/2, y, y+height },
			3
		);

		getWeapon().draw(g);
	}

	public void think(){
        /*

		if(osp.getPlayer()==null){
			getWeapon().stopFiring();
		} else {

			// move towards player, but don't get too close


			// shoot weapon
			if(getWeapon()!=null && !getWeapon().isFiring())
				getWeapon().startFiring();

			// get ready to fire here
			targetLocation(osp.getPlayer().getX(), osp.getPlayer().getY());

		}
*/
	}

	///

	public int getDefaultOrientation(){
		return Const.RIGHT_TO_LEFT;
	}
	public String getName(){
		return "Triangle Man";
	}

	public int defaultTeam(){
		return Const.NPC;
	}
	public int defaultMaxHp(){
		return 30;
	}
	public int getDefaultSpeed(){
		return 5;
	}
	// ** generated with MaskMachine ** //
	public Polygon getDefaultMask(){
		return new Polygon(
			new int[]{74,0,74},
			new int[]{0,36,73},
			3
		);
	}
}
