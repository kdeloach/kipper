package kipper.effects;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Point2D;
import kipper.*;

public class MarqueeStars
{
	Point2D.Double[] stars;
    int size;
    double angle, speed;
    Color color;

	public MarqueeStars(int numStars, double angle, double speed, int size, Color color)
    {
        this.angle = angle;
        this.speed = speed;
        this.size = size;
        this.color = color;
        stars = new Point2D.Double[numStars];
		for (int i = 0; i < numStars; i++) {
			stars[i] = new Point2D.Double(Util.randRange(0, OuterSpacePanel.WIDTH), Util.randRange(0, OuterSpacePanel.HEIGHT));
		}
	}

	public void update()
    {
        for (int i = 0; i < stars.length; i++) {
            stars[i].x += Math.cos(angle) * speed;
            stars[i].y += Math.sin(angle) * speed;
            // if a star goes out of bounds reset
            if (stars[i].x <= 0)
                stars[i] = new Point2D.Double(OuterSpacePanel.WIDTH, Math.random() * OuterSpacePanel.HEIGHT);
            if (stars[i].x > OuterSpacePanel.WIDTH)
                stars[i] = new Point2D.Double(0, Math.random() * OuterSpacePanel.HEIGHT);
            if (stars[i].y > OuterSpacePanel.HEIGHT)
                stars[i] = new Point2D.Double(Math.random() * OuterSpacePanel.WIDTH, 0);
            if (stars[i].y <= 0)
                stars[i] = new Point2D.Double(Math.random() * OuterSpacePanel.WIDTH, OuterSpacePanel.HEIGHT);
        }
	}

    public void paint(Graphics g)
    {
		g.setColor(color);
		for (int i = 0; i < stars.length; i++) {
			g.drawOval((int)stars[i].x, (int)stars[i].y, size, size);
        }
    }
}
