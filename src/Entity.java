package kipper;

import java.awt.Image;
import java.awt.Graphics;

public interface Entity
{
    public double getX();
    public double getY();
    public void setLocation(double x, double y);
    public int getWidth();
    public int getHeight();
    public int getTeam();
    public int getLife();
    public boolean isAlive();
    // It is required that hit method calls die() when damage inflicted is greater than getLife()
    public void hit(double damage);
    public void update();
    public void draw(Graphics g);
    public void collide(Entity e);
    public void die();
}
