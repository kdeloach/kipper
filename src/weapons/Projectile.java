package kipper.weapons;

import kipper.*;
import kipper.ships.*;

public interface Projectile
{
	public double getX();
	public double getY();

	public boolean intersects(Ship s);
	public boolean intersects(Projectile s);
	public boolean contains(int x, int y);
	public boolean contains(double x, double y);

	public void draw(java.awt.Graphics g);
    public void move();
	public void explode();
    public void update();

    public boolean collidesWithOwner();
}