package kipper.effects;

import java.awt.geom.Point2D;

public class Particle extends Point2D.Double
{
    double startX, startY, endX, endY;

    public Particle(double x, double y, double theta, double distance)
    {
        super(x, y);
        this.startX = x;
        this.startY = y;
        this.endX = x + Math.cos(theta) * distance;
        this.endY = y + Math.sin(theta) * distance;
    }
}
