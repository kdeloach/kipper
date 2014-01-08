package kipper.weapons;

import kipper.*;
import kipper.ships.*;

public interface Projectile
{
	public int getX();
	public int getY();
	public int getId();

	public boolean intersects(Ship s);
	public boolean intersects(Projectile s);
	public boolean contains(double x, double y);
	public boolean contains(int x, int y);

	public void setId(int n);

	public void draw(java.awt.Graphics g);
	public void explode();
}