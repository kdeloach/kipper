package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;

public class Blaster extends Weapon {

	// icon for weapon
	Image icon;

	public Blaster(int x, int y, int rx, int ry, Ship c){
		super(x,y,rx,ry,c);

		// natural abilities
		addUpgrade(new RecoilAbility(this));
		addUpgrade(new SpreadAbility(this));
		addUpgrade(new SpreadAbility(this));

		icon = Util.instance.loadImage("/assets/blaster_icon.gif");
	}
	public Image getIcon(){
		return icon;
	}
	public int getDefaultDamage(){
		return 2;
	}
	public void draw(Graphics g){
	}
	public void fireProjectile(double heading){
		new Bullet(x, y, heading, getDamage(), this);
	}
	public int getDefaultCooldown(){
		return 400;
	}
}