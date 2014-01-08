import java.awt.Color;
import java.awt.Font;

// SpeedAbility is unique in that, it SHOULD work for both Ship and Weapon
public class SpeedAbility extends Ability implements WeaponListener, ShipListener {
	
	// percent to speed it up by
	double percent;
	
	public SpeedAbility(Upgradeable v){
		this(.20,v);
	}
	public SpeedAbility(double perc, Upgradeable v){
		percent=perc;
		
		// listeners
		// this makes it so it only affects the Ship OR the Weapon, not both
		if(v instanceof Weapon)
			v.addWeaponListener(this);
		else
			v.addShipListener(this);
	}
	public double attributeCalled(String name, double value){
		// decrease cooldown time for a Weapon
		if(name==Ability.COOLDOWN)
			return value-value*percent;
		// increase speed for a Ship
		else if(name==Ability.SPEED)
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
		g.drawString(getTitle(), x+width/2-26, y+height/2+5);
	}
	public static String getTitle(){
		return "Speed";	
	}
	public static Color getColor(){
		return Color.YELLOW;	
	}
}