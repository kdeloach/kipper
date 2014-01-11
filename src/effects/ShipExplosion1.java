package kipper.effects;

import java.awt.Graphics;
import java.awt.Color;
import kipper.*;

public class ShipExplosion1 extends Explosion
{
	public ShipExplosion1(double x, double y, OuterSpacePanel c)
    {
		super(x, y, c);
	}

	@Override public Color getColor() { return Color.RED; }
	@Override public int getDurationMs() { return 2000; }
	@Override public double particleDistance() { return Math.random() * 25 + 25; }
}
