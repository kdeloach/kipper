import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class BasicBullet extends Bullet {
	public BasicBullet(int x, int y, double t, double dmg, Weapon c){
		super(x,y,t,dmg,c);
	}
	public void draw(Graphics g){
		g.setColor(Color.YELLOW);
		g.drawOval(getX(), getY(), width, height);
	}
	public void die(){
		new BasicExplosion(getX(), getY(), panel());
	}
	public int getDefaultSpeed(){
		return 15;
	}
	public Dimension getSize(){
		return new Dimension(1,1);
	}
	public int getDefaultTeam(){
		return Const.PLAYER;
	}
}