import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

public class BasicLaserBeam extends LaserBeam {
	public BasicLaserBeam(int x, int y, double t, double dmg, Weapon w){
		super(x, y, t, dmg, w);
	}
	public void draw(Graphics g){
		g.setColor(Color.WHITE);
		g.drawLine((int)start.x, (int)start.y,
		           (int)stop.x,  (int)stop.y
		);
	}
	public void die(){
		new BasicExplosion((int)contact.x, (int)contact.y, panel());
	}
	public int getDefaultSpeed(){
		return 15;
	}
	public int getLength(){
		return 10;
	}
	public int getDefaultTeam(){
		return Const.PLAYER;
	}

    public int getTeam() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}