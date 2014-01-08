package kipper;

import java.awt.Graphics;
import java.awt.Color;

public class ShipExplosion2 extends Explosion {
	public ShipExplosion2(int x,int y,OuterSpacePanel c){
		super(x,y,c);
	}
	public Color getColor(){
		return Color.GREEN;	
	}
	public int getTicks(){
		return 14;	
	}
	public int getAmount(){
		return 30;
	}
}