import java.awt.*;

public class Blaster extends Weapon {
	
	// icon for weapon
	Image icon;
	
	public Blaster(int x, int y, int rx, int ry, Ship c){
		super(x,y,rx,ry,c);
		
		// natural abilities
		addNaturalAbility(new RecoilAbility(this));
		addNaturalAbility(new SpreadAbility(this));
		addNaturalAbility(new SpreadAbility(this));
		
		icon = Util.instance.loadImage("assets/blaster_icon.gif");
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
		new BasicBullet(x, y, heading, getDamage(), this );	
	}
	public int getDefaultCooldown(){
		return 400;	
	}
}