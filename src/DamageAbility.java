import java.awt.Color;
import java.awt.Font;

// DamageAbility should be able to be used interchangeably with Ship and Weapon
// Should increase damage a weapon yields
// If applied to Ship, should increase all weapons' damage
public class DamageAbility extends Ability implements WeaponListener, ShipListener {
	
	// percent to increase damage
	double percent;
	
	public DamageAbility(Upgradeable v){
		this(.30,v);
	}
	public DamageAbility(double perc, Upgradeable v){
		percent=perc;
		
		// what listeners to register?
		v.addWeaponListener(this);
		v.addShipListener(this);
	}
	public double attributeCalled(String name, double value){
		if(name==Ability.DAMAGE)
			return value+value*percent;
		return value;
	}
	public void weaponFired(Weapon w){}
	
	public void bulletHit(Bullet b){}
	public void bulletFired(Bullet b){}
	
	public void shipMoved(Ship s){}
	public void shipStopped(Ship s){}
	
	public static void drawIcon(java.awt.Graphics g, int x, int y, int width, int height){
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(getColor());
		g.fillRoundRect(x,y,width,height,15,15);
		g.setColor(Color.BLACK);
		g.drawString(getTitle(), x+width/2-30, y+height/2+5);
	}
	public static String getTitle(){
		return "Damage";	
	}
	public static Color getColor(){
		return Color.GREEN;	
	}
}