package kipper;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

// Implementing objects CAN BE destroyed
public interface Destructable
{
	// Called when object is destroyed
	// Use for death animation, etc.
	public void die();

	// Use for collision detection
	public Polygon getMask();

    public boolean intersects(Rectangle r);
    public boolean intersects(Destructable s);
	public boolean contains(int x, int y);
	public boolean contains(Point p);
	public boolean contains(Rectangle r);
}
