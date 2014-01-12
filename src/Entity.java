package kipper;

import java.awt.Graphics;

public interface Entity
{
	public double getX();
	public double getY();
    public void setLocation(double x, double y);
	public int getWidth();
	public int getHeight();
    public int getLife();
    public boolean isAlive();
    public void hit(double damage);
	public boolean intersects(Entity entity);
    public void update();
	public void draw(Graphics g);
    public void die();
}
