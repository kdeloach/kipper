import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.geom.Point2D;

// LaserBeams are basically 2 points, whereas the first point is x,y 
//   and the second point is x+length*cos(theta),y+length*sin(theta)

public class LightningBolt extends Bolt {
	public LightningBolt(int x, int y, double t, double dmg, Weapon w){
		super();
		
		new LightningBolt(x, y, t, getDefaultBranches(), getDefaultLength(), getDefaultSpan(), dmg, w);
	}
	private LightningBolt(double x, double y, double offset, int branches, int length, int span, double dmg, Weapon w){
		this.weapon=w;

		this.length=length;
		this.branches=branches;
		this.damage=dmg;
		this.span=span;
		this.offset=offset;
			
		start=new Point2D.Double();
		stop=new Point2D.Double();
		
		// add offset here
		this.theta= Math.toRadians(Math.random()*90-45);
		//this.theta=Math.toRadians(Math.random()*180-90);
		//this.theta=Math.toRadians(Math.random()*360);
		this.theta += offset;
		
		setLocation(x,y);
		weapon.ship.panel().registerProjectile(this);
		
		if(length>10)
			splinter();
		
		new Thread(this).start();
	}
	final public void splinter(){
		double x=stop.x, y=stop.y;
		for(int i=0;i<branches;i++){
			LightningBolt b = new LightningBolt(x, y, offset, (int)( Math.random()*getAmtChildrenBranches() ), length-10, span, damage, weapon);
			x=b.stop.x;
			y=b.stop.y;
		}
	}
	
	////////
	
	public void die(){
		new Explosion(getX(), getY(), panel())
        {
			public Color getColor()
            {
				return Color.BLUE;	
			}	
		};
	}
	
	///////
	
	public int getDefaultSpan(){
		return 10;	
	}
	public int getDefaultLength(){
		return 40;	
	}
	public int getDefaultBranches(){
		return 2;//2
	}
	public int getAmtChildrenBranches(){
		return 5;//5
	}
	public int getDefaultTeam(){
		return Const.PLAYER;
	}
	
}