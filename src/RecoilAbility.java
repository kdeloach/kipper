import java.awt.Color;
import java.awt.Font;

// Send a ship flying back
public class RecoilAbility extends Ability implements WeaponListener/*, ShipListener*/ {
	
	// distance to send ship backwards
	int distance;
	
	public RecoilAbility(Upgradeable v){
		this(100,v);
	}
	public RecoilAbility(int n, Upgradeable v){
		distance=n;
		
		// listeners
		//v.addShipListener(this);
		v.addWeaponListener(this);
	}
	public double attributeCalled(String name, double value){
		return value;	
	}
	public void weaponFired(Weapon w){
		Ship s = w.ship;
		
		// prevents player from moving ship while shooting and in-recoil
		// HAD to remove this because: theres a bug where you can Fire a recoil-capable weapon,
		//    then let go of the leftmousebutton and still be able to move&shoot with just the rightmousebutton
		//    PLUS you could switch weapons and keep the effect; I don't want inconsistent controls
		//    The DOWNSIDE is that players can "break" out of recoil by moving as soon as weapon is fired
		//s.leftmousedown=false;
		
		// if its a bot, make sure it isn't recoiling offscreen
		if(!s.underControl()&&s.getX()+s.getWidth()>=OuterSpacePanel.WIDTH)
			return;	
		
		// wooosh
		//if(s.getOrientation()==Const.LEFT_TO_RIGHT){
			s.move(
				(int)( s.getX()-distance*Math.cos(w.heading())),
				(int)( s.getY()-distance*Math.sin(w.heading()))
				);
		/*} else {
			s.move(
				(int)( s.getX()-distance*Math.cos(w.heading())),
				(int)( s.getY()-distance*Math.sin(w.heading()))
				);
		}*/
	}
	
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
		return "Recoil";	
	}
	public static Color getColor(){
		return Color.MAGENTA;	
	}
}