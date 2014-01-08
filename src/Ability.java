import java.awt.Color;

public abstract class Ability {
	
	final static String SPEED = "SPEED";
	final static String COOLDOWN = "COOLDOWN";
	final static String DAMAGE = "DAMAGE";
	final static String SPREAD = "SPREAD";
	final static String GUARD = "GUARD";
	final static String HEADING = "HEADING";
	
	// Reverse any changes made to the ship
	// Used when detaching ability from ship
	//abstract public void revert();
	
	public static void drawIcon(java.awt.Graphics g, int x, int y, int width, int height){}
	
	//tracking
	private int id;
	
	public void setId(int n){
		id=n;	
	}
	public int getId(){
		return id;	
	}
}