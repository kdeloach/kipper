package kipper;

import java.awt.*;

public class Shooter extends Weapon {
	
	// icon for weapon
	Image icon;
	
	public Shooter(int x, int y, int rx, int ry, Ship c){
		super(x,y,rx,ry,c);
		
		// natural abilities

		icon = Util.instance.loadImage("assets/shooter_icon.gif");
	}
	public Image getIcon(){
		return icon;
	}
	public int getDefaultDamage(){
		return 1;	
	}
	public void draw(Graphics g){
	}
	public void fireProjectile(double heading){
		new Bullet(x, y, heading, getDamage(), this);	
	}
	public int getDefaultCooldown(){
		return 250;	
	}
}