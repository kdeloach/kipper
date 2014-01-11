package kipper.effects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import kipper.*;

public class Explosion
{
    double x, y;
    int id;
	int ticks = 0;
	Debris[] shrap;
	OuterSpacePanel osp;

	public Explosion(double x, double y, OuterSpacePanel c)
    {
		this.x = x;
		this.y = y;
		this.osp = c;

		initParticles();

		osp.registerExplosion(this);
	}

	public void initParticles()
    {
		shrap = new Debris[getAmount()];
		for (int i = 0; i < shrap.length; i++) {
			shrap[i] = new Debris(x, y, particleAngle(), particleDistance());
		}
	}

	public void update()
    {
        if (ticks <= getTicks()) {
            tick(ticks);
            ticks++;
            return;
        }
        osp.unregisterExplosion(this);
	}

	public void tick(double t)
    {
        double d = getTicks();
		for(int i = 0; i < shrap.length; i++) {
            Debris p = shrap[i];
            double bx = p.startX;
            double cx = p.endX - p.startX;
            double by = p.startY;
            double cy = p.endY - p.startY;
			p.x = Easing.easeOutQuad(t, bx, cx, d);
			p.y = Easing.easeOutQuad(t, by, cy, d);
		}
	}

	public void draw(Graphics g)
    {
		g.setColor(getColor());
		for (Debris d : shrap) {
			g.drawOval((int)d.x, (int)d.y, getSize().width, getSize().height);
		}
	}

    // TODO: Remove
	public void setId(int id) { this.id = id; }
	public int getId() { return id; }

    public int getTicks() { return 50; }
	public Color getColor() { return Color.YELLOW; }
	public int getAmount() { return 10; }
	public Dimension getSize() { return new Dimension(0, 0); }
    public double particleAngle() { return Math.toRadians(Util.randRange(0, 360)); }
    public double particleDistance() { return Math.random() * 20 + 10; }
}
