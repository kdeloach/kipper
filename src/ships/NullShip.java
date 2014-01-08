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

    public NullShip(int x, int y, OuterSpacePanel c)
    {
        super();
    }

	@Override public int getDefaultOrientation() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int defaultMaxHp() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int defaultTeam() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public int getDefaultSpeed() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public Polygon getDefaultMask() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public String getName() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public void draw(Graphics g) { throw new UnsupportedOperationException("Not implemented"); }

    @Override public void die() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public Polygon getMask() { throw new UnsupportedOperationException("Not implemented"); }
	@Override public boolean contains(int x, int y) { throw new UnsupportedOperationException("Not implemented"); }
	@Override public boolean contains(Point p) { throw new UnsupportedOperationException("Not implemented"); }
	@Override public boolean contains(Rectangle r) { throw new UnsupportedOperationException("Not implemented"); }
}
