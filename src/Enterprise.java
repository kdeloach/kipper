package kipper;

import java.awt.Image;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import kipper.weapons.*;

public class Enterprise extends Ship {

	// ship sprite
	Image img;

	public Enterprise(int x,int y,OuterSpacePanel c){
		super(x,y,c);

		setSize(141,52);

		equipWeapon(new Shooter(x,y,width-3,height/2+4,this));
		equipWeapon(new Blaster(x,y,width-3,height/2+4,this));
		equipWeapon(new LaserGun(x,y,width-3,height/2+4,this));
		equipWeapon(new MineLauncher(x,y,width-3,height/2+4,this));
		equipWeapon(new LightningGun(x,y,width-3,height/2+4,this));
		selectWeapon(0);

		// abilities
		//addUpgrade(new RotateAbility(this));

		img = Util.instance.loadImage("assets/enterprise2.gif");
	}
	public int getDefaultOrientation(){
		return Const.LEFT_TO_RIGHT;
	}
	// Dramatic death explosion
	public void die(){
		new ShipExplosion1(x+width/2,y+height/2,osp){
			public Color getColor(){
				return Color.YELLOW;
			}
			public int getTicks(){
				return 10;
			}
			public int getAmount(){
				return 25;
			}
		};
		new ShipExplosion1(x+width/2,y+height/2,osp){
			public Color getColor(){
				return Color.RED;
			}
		};
	}
	public void draw(Graphics g){
		g.drawImage(img, x, y, osp);

		// paint current weapon
		getWeapon().draw(g);
	}
	// ** generated with MaskMachine ** //
	public Polygon getDefaultMask(){
		return new Polygon(
			new int[]{0,73,94,141,133,102,87,70,26,49,37,24},
			new int[]{3,0,4,32,44,51,51,47,48,38,19,9},
			12
		);
	}
	public int defaultTeam(){
		return Const.PLAYER;
	}
	public int getDefaultSpeed(){
		return 10;
	}
	public int defaultMaxHp(){
		return 50;
	}
	public String getName(){
		return "ENTERPRISE";
	}
}