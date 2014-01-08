import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

public class BasicMine extends Mine {
	public BasicMine(int x, int y, double t, double dmg, Weapon w){
		super(x, y, t, dmg, w);
	}
	public void draw(Graphics g){
		g.setColor(Color.GREEN);

		int off = width/6;
		//g.drawOval(getX()-width/2,getY()-height/2,width,height);

		g.drawRect(getX()-width/2+off,
		           getY()-height/2+off,
		           width-off*2,
		           height-off*2);


		g.drawPolygon(
			new int[]{ getX()-width/2,     getX(),          getX()+width/2,     getX() },
			new int[]{ getY(),             getY()-height/2, getY(),             getY()+height/2 },
			4
		);
	}
	public void die(){
		new BasicExplosion(getX(), getY(), panel());
	}
	public int getDefaultSpeed(){
		return 15;
	}
	public int getDefaultSteps(){
		return 40;
	}
	public Dimension getSize(){
		return new Dimension(25,25);
	}
	public int getDefaultTeam(){
		return Const.PLAYER;
	}
}