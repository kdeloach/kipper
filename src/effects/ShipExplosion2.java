package kipper.effects;

import java.awt.Graphics;
import java.awt.Color;
import kipper.*;

public class ShipExplosion2 extends Explosion
{
	public ShipExplosion2(double x, double y, OuterSpacePanel c)
    {
		super(x, y, c);
	}

	@Override public Color getColor() { return Color.GREEN; }
	@Override public int getTicks() { return 14; }
	@Override public int getAmount() { return 30; }
}
