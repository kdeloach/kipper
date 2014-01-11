package kipper.effects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import kipper.*;

public class Explosion
{
    double x, y;
	int ticks = 0;
	Particle[] shrap;
	OuterSpacePanel osp;

	public Explosion(double x, double y, OuterSpacePanel c)
    {
		this.x = x;
		this.y = y;
		this.osp = c;

		initParticles();

		osp.addExplosion(this);
	}

	public void initParticles()
    {
		shrap = new Particle[getAmount()];
		for (int i = 0; i < shrap.length; i++) {
			shrap[i] = createParticle(x, y, particleAngle(), particleDistance());
		}
	}

    public Particle createParticle(double x, double y, double theta, double distance)
    {
        return new Particle(x, y, theta, distance);
    }

	public void update()
    {
        if (ticks <= getDurationTicks()) {
            tick(ticks);
            ticks++;
            return;
        }
        osp.removeExplosion(this);
	}

	public void tick(double t)
    {
        double d = getDurationTicks();
		for(int i = 0; i < shrap.length; i++) {
            Particle p = shrap[i];
            double bx = p.startX;
            double cx = p.endX - p.startX;
            double by = p.startY;
            double cy = p.endY - p.startY;
			p.x = easingFn(t, bx, cx, d);
			p.y = easingFn(t, by, cy, d);
		}
	}

	public void draw(Graphics g)
    {
		g.setColor(getColor());
		for (Particle d : shrap) {
			g.drawOval((int)d.x, (int)d.y, getWidth(), getHeight());
		}
	}

    // Represents number of times update() must be called until animation ends
    private int getDurationTicks()
    {
        return (int)((long)getDurationMs() / OuterSpacePanel.FPS);
    }

    public int getDurationMs() { return 500; }
	public Color getColor() { return Color.YELLOW; }
	public int getAmount() { return 10; }
	public int getWidth() { return 0; }
	public int getHeight() { return 0; }
    public double particleAngle() { return Math.toRadians(Util.randRange(0, 360)); }
    public double particleDistance() { return Math.random() * 20 + 10; }
    public double easingFn(double t, double b, double c, double d) { return Easing.easeOutQuad(t, b, c, d); }
}
