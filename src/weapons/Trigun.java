package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;
import kipper.upgrades.*;

// Weapon used by TriangleMan
public class Trigun extends Weapon
{
	Image img;
	public Trigun(int x, int y, int rx, int ry, Ship s){
		super(x,y,rx,ry,s);
		setSize(10,10);

		img = Util.instance.loadImage("/assets/trigun_ico.gif");
	}

	public void fireProjectile(double heading)
    {
		new Bullet(x-1, y+height/2, heading, getDamage(), this)
        {
			public void draw(Graphics g)
            {
				g.setColor(Color.GREEN);
				g.drawOval(getX(), getY(), 1, 1);
			}
			public void die()
            {
				new Explosion(getX(), getY(), panel())
                {
					public Color getColor()
                    {
						return Color.GREEN;
					}
				};
			}
		};
	}

	public Image getIcon(){
		return img;
	}
	public int getDefaultDamage(){
		return 1;
	}
	public int getDefaultCooldown(){
		return 500;
	}
	public void draw(Graphics g){

		g.setColor(Color.RED);
		g.fillPolygon(
			new int[]{ x, x+width, x+width },
			new int[]{ y+height/2, y, y+height },
			3
		);
	}
}
