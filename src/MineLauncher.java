import java.awt.*;

public class MineLauncher extends Weapon {

	// icon for weapon
	Image icon;

	public MineLauncher(int x, int y, int rx, int ry, Ship c){
		super(x,y,rx,ry,c);

		// natural abilities
		addNaturalAbility(new RecoilAbility(this));
		//addNaturalAbility(new SpreadAbility(4,this));

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
		new Mine(x, y, heading, getDamage(), this );
	}
	public int getDefaultCooldown(){
		return 300;
	}
}