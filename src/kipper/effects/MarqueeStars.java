package kipper.effects;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Point2D;
import kipper.*;

// TODO: Refactor into generic partical emitter
public class MarqueeStars
{
    private Star[] stars;
    private double angle, speed;
    private int minSize, maxSize;
    private int minAlpha, maxAlpha;

    public MarqueeStars(int numStars, double angle, double speed, int minSize, int maxSize, int minAlpha, int maxAlpha)
    {
        this.angle = angle;
        this.speed = speed;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.minAlpha = minAlpha;
        this.maxAlpha = maxAlpha;
        stars = new Star[numStars];
        for (int i = 0; i < numStars; i++) {
            stars[i] = new Star(Util.randRange(0, OuterSpacePanel.WIDTH),
                                Util.randRange(0, OuterSpacePanel.HEIGHT),
                                randSize(),
                                randColor());
        }
    }

    private int randSize()
    {
        return Util.randRange(minSize, maxSize);
    }

    private int randAlpha()
    {
        return Util.randRange(minAlpha, maxAlpha);
    }

    private Color randColor()
    {
        return new Color(0xFF, 0xFF, 0xFF, randAlpha());
    }

    public void update()
    {
        if (speed <= 0) {
            return;
        }
        for (int i = 0; i < stars.length; i++) {
            Star p = stars[i];
            p.x += Math.cos(angle) * speed;
            p.y += Math.sin(angle) * speed;
            // if a star goes out of bounds reset
            if (p.x < 0) {
                p.x = OuterSpacePanel.WIDTH;
                p.y = Math.random() * OuterSpacePanel.HEIGHT;
                p.size = randSize();
                p.color = randColor();
            }
            // Not thoroughly tested
            // if (stars[i].x > OuterSpacePanel.WIDTH)
                // stars[i] = new Point2D.Double(0, Math.random() * OuterSpacePanel.HEIGHT);
            // if (stars[i].y > OuterSpacePanel.HEIGHT)
                // stars[i] = new Point2D.Double(Math.random() * OuterSpacePanel.WIDTH, 0);
            // if (stars[i].y < 0)
                // stars[i] = new Point2D.Double(Math.random() * OuterSpacePanel.WIDTH, OuterSpacePanel.HEIGHT);
        }
    }

    public void draw(Graphics g)
    {
        for (int i = 0; i < stars.length; i++) {
            Star p = stars[i];
            g.setColor(p.color);
            g.fillRect((int)p.x, (int)p.y, p.size, p.size);
            //if (alpha >= 0xFF)
                //g.drawOval((int)p.x, (int)p.y, p.size, p.size);
        }
    }
}


class Star extends Point2D.Double
{
    public int size;
    public Color color;

    public Star(double x, double y, int size, Color color)
    {
        super(x, y);
        this.size = size;
        this.color = color;
    }
}
