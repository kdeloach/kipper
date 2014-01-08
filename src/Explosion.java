package kipper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Explosion implements Runnable
{
	OuterSpacePanel osp;
	Debris[] shrap;
	int ticks, id, x, y;

	public Explosion(int x, int y, OuterSpacePanel c)
    {
		this.x = x;
		this.y = y;
		this.osp = c;

		ticks = getTicks();
		initParticles();

		osp.registerExplosion(this);

		new Thread(this).start();
	}

	public void initParticles()
    {
		shrap = new Debris[getAmount()];
		for(int i=0;i<shrap.length;i++) {
			shrap[i] = new Debris(x, y, Math.toRadians(Util.randRange(0,360)), Math.random());
		}
	}

	final public void run()
    {
        while (ticks > 0) {
            tick(ticks);
            try{ Thread.sleep(Const.EXPLOSION_TIME); }catch (Exception ie) {}
            ticks--;
        }
        osp.unregisterExplosion(this);
	}

	public void draw(Graphics g)
    {
		g.setColor(getColor());
		for (Debris d : shrap) {
			g.drawOval(d.x, d.y, getSize().width, getSize().height);
		}
	}

	// can overwrite this to change the path/movement of the shrapenel
	public void tick(int t)
    {
		for(int i = 0; i < shrap.length; i++) {
			shrap[i].x += t * Math.cos(shrap[i].theta) * shrap[i].dist;
			shrap[i].y += t * Math.sin(shrap[i].theta) * shrap[i].dist;
		}
	}

	final public void setId(int id) { this.id = id; }
	final public int getId(){ return id; }

	public Color getColor()
    {
		return Color.YELLOW;
	}

	public int getTicks()
    {
		return 10;
	}

	public int getAmount()
    {
		return 10;
	}

	public Dimension getSize()
    {
		return new Dimension(0, 0);
	}
}