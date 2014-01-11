package kipper.effects;

import java.awt.geom.Point2D;

// TODO: Rename to Particle
public class Debris extends Point2D.Double
{
	double theta;

	// distance from origin
	double dist;

	public Debris(double x, double y, double theta, double dist)
    {
		super(x, y);
		this.theta = theta;
		this.dist = dist;
	}
}
