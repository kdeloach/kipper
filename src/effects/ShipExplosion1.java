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

    @Override
	public void initParticles()
    {
		shrap = new Debris[getAmount()];
		for (int i = 0; i < shrap.length; i++) {
			shrap[i] = new Debris(x, y, Math.toRadians(Util.randRange(0, 360)), 0.65);
		}
	}

	@Override public Color getColor() { return Color.RED; }
	@Override public int getTicks() { return 14; }
	@Override public int getAmount() { return 25; }
}
