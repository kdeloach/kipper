import java.awt.Color;
import java.awt.Font;

public class SpreadAbility extends Ability implements WeaponListener {
	
	// amount by which to increase spread
	double amount;
	
	public SpreadAbility(Upgradeable v){
		this(2,v);
	}
	public SpreadAbility(int amt, Upgradeable v){
		amount=amt;
		
		// what shall we listen to
		v.addWeaponListener(this);
	}
	public double attributeCalled(String name, double value){
		if(name==Ability.SPREAD)
			return value+amount;
		// balancing act - you can fire more bullets at the price of slower firerate
		// its not much, but without this cooldown increase, spread effect can be DEVASTATING
		// can easily wipe out everyone on-screen with this baby
		else if(name==Ability.COOLDOWN)
			return value+10;
		return value;
	}
	
	public void weaponFired(Weapon w){}
	
	public void bulletHit(Bullet b){}
	public void bulletFired(Bullet b){}

	public static void drawIcon(java.awt.Graphics g, int x, int y, int width, int height){
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(getColor());
		g.fillRoundRect(x,y,width,height,15,15);
		g.setColor(Color.BLACK);
		g.drawString(getTitle(), x+width/2-26, y+height/2+5);
	}
	public static String getTitle(){
		return "Spread";	
	}
	public static Color getColor(){
		return Color.RED;
	}
}