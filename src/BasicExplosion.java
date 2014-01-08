import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class BasicExplosion extends Explosion {

	public BasicExplosion(int x,int y,OuterSpacePanel c){
		super(x,y,c);
	}
	public Color getColor(){
		return Color.YELLOW;	
	}
	public int getTicks(){
		return 10;	
	}
	public int getAmount(){
		return 10;
	}
	public Dimension getSize(){
		return new Dimension(0,0);	
	}
}