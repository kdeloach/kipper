package kipper.ships;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Graphics;
import kipper.*;

public class NullShip extends Ship
{
    public NullShip()
    {
        super();
    }

	@Override public int getWidth() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int getHeight() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int getDefaultOrientation() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int defaultMaxHp() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int defaultTeam() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int getDefaultSpeed() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public Polygon getDefaultMask() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public String getName() { throw new UnsupportedOperationException("Not implemented"); }

    @Override public void die() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public Polygon getMask() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public boolean intersects(Entity e) { throw new UnsupportedOperationException("Not implemented"); }
}
