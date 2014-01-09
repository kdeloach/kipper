package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;

public class MineLauncher extends Weapon {

	// icon for weapon
	Image icon;

	public MineLauncher(int x, int y, int rx, int ry, Ship c){
		super(x,y,rx,ry,c);

		// natural abilities
		addUpgrade(new RecoilAbility(this));

		icon = Util.instance.loadImage("assets/mines_icon.gif");
	}
	public Image getIcon(){
		return icon;
	}
	public int getDefaultDamage(){
		return 5;
	}
	public void draw(Graphics g){
	}
	public void fireProjectile(double heading){
		new SpaceMine(x, y, heading, getDamage(), this );
	}
	public int getDefaultCooldown(){
		return 300;
	}
}