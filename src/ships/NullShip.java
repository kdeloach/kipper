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
	@Override public int getOrientation() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int getMaxHp() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int getSpeed() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public Polygon getMask() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public String getName() { throw new UnsupportedOperationException("Not implemented"); }

    @Override public void die() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public boolean intersects(Entity e) { throw new UnsupportedOperationException("Not implemented"); }
}
