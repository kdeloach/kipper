package kipper;

import java.awt.Graphics;
import java.awt.Color;

public class ShipExplosion1 extends Explosion {
	public ShipExplosion1(int x,int y,OuterSpacePanel c){
		super(x,y,c);
	}
	public void initParticles(){
		shrap = new Debris[getAmount()];
		
		for(int i=0;i<shrap.length;i++){
			shrap[i]=new Debris(x, y, Math.toRadians(Random.range(0,360)),.65);
		}
	}
	public Color getColor(){
		return Color.RED;	
	}
	public int getTicks(){
		return 14;	
	}
	public int getAmount(){
		return 25;
	}
}