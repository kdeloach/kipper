package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;

public class LaserGun extends Weapon {

	// icon for weapon
	Image icon;

	public LaserGun(int x, int y, int rx, int ry, Ship c){
		super(x, y, rx, ry, c);

		// natural abilities

		icon = Util.instance.loadImage("assets/laser_icon.gif");
	}
	public Image getIcon(){
		return icon;
	}
	public int getDefaultDamage(){
		return 3;
	}
	public void draw(Graphics g){
	}
	public void fireProjectile(double heading){
		new LaserBeam(x, y, heading, getDamage(), this );
	}
	public int getDefaultCooldown(){
		return 200;
	}
}