package kipper;

import java.awt.Rectangle;

// Implementing objects CAN BE destroyed
public interface Destructable {

	// Called when object is destroyed
	// Use for death animation, etc.
	public void die();

	// Use for collision detection
	public java.awt.Polygon getMask();

	public boolean contains(int x, int y);
	public boolean contains(java.awt.Point p);
	public boolean contains(java.awt.Rectangle r);
    public boolean intersects(Destructable s);
    public boolean intersects(Rectangle r);
}
