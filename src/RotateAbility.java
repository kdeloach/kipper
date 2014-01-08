import java.awt.Color;
import java.awt.Font;

public class RotateAbility extends Ability implements WeaponListener, ShipListener {
	
	Ship s;
	
	public RotateAbility(Upgradeable v){
		
		// listeners
		v.addWeaponListener(this);
		
		if(v instanceof Weapon)
			s = ((Weapon)v).ship;
		else
			s = (Ship)v;
	}
	
	public double attributeCalled(String name, double value){
		if(name==Ability.HEADING){
			return heading();
		}
		return value;
	}
	private double heading(){
		double a = s.mousePressed.x-(s.x+s.width);
		double b = s.mousePressed.y-(s.y+s.height/2);
		double c = Math.sqrt(a*a+b*b);

		return Math.PI*s.getOrientation()+Math.atan(b/a);
		//return Math.PI*s.getOrientation()+Math.asin(b/c);
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
		g.drawString(getTitle(), x+width/2-23, y+height/2+5);
	}
	public static String getTitle(){
		return "Rotate";	
	}
	public static Color getColor(){
		return Color.CYAN;	
	}
}