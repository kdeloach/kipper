import java.awt.*;

// WHEN this gun upgrades you get POINTS
// You can use these points to select how you want to upgrade the weapon
//   WITH the LightningGun Calibrator
// Basically, the calibrator will be several Sliders, ex:
//  
// Length:   short <----0---------> long
// Branches: few   <-------0------> many
// Children: few   <----0---------> many
// Span:     short <--0-----------> long
// 
// The max value of any individual sliders is the amount of points you have
// Edit these properties of the gun BY removing all natural upgrades onSave and then re-applying them
// 

public class LightningGun extends Weapon {
	
	// icon for weapon
	Image icon;
	
	public LightningGun(int x, int y, int rx, int ry, Ship c){
		super(x,y,rx,ry,c);
		
		// natural abilities
		addNaturalAbility(new SpreadAbility(2,this));
		
		icon = Util.instance.loadImage("assets/lightning_icon.gif");
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
		new LightningBolt(x, y, heading, getDamage(), this );	
	}
	public int getDefaultCooldown(){
		return 200;	
	}
}