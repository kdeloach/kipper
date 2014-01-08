import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Point;

public abstract class Mine extends Bullet {
	
	// amount of steps to take before resting
	private int steps;
	
	public Mine(int x, int y, double t, double dmg, Weapon w){
		super(x, y, t, dmg, w);
		
		steps=getDefaultSteps();
	}
	
	public void move(){
		// we want to stop moving at some point
		if(steps<=0){
			
			try {
				for(Projectile p : weapon.ship.panel().bulletList)
					if(p instanceof Mine && p.getId()!=getId() && p.intersects(this))
					{
						p.explode();
						explode();	
					}
			} catch (java.util.ConcurrentModificationException ie) {}
				
			return;
		}

		setLocation(
			dx + steps/Const.BULLET_SPEED*Math.cos(theta),
			dy + steps/Const.BULLET_SPEED*Math.sin(theta)
		);
		
		steps--;
	}
		
	///////
	// Abstracts
	
	abstract public int getDefaultSpeed();
	abstract public int getDefaultSteps();
	//abstract public int getDefaultRange();
	abstract public void draw(Graphics g);
	abstract public void die();

}