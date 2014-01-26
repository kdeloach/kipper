package kipper.ships;

import java.awt.Image;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import kipper.*;
import kipper.effects.*;
import kipper.weapons.*;

public class Darkwing extends Enterprise
{
    Image img;
    OuterSpacePanel panel;
    ParticleEmitter trails;
    Point trailsOffset = new Point(5, 18);

    public Darkwing(int x, int y, OuterSpacePanel c)
    {
        super(x, y, c);
        img = Util.instance.loadImage("/assets/darkwing.png");
        trails = new ParticleEmitter(0, 0, new ShipTrails());
    }

    @Override
    public void update()
    {
        super.update();
        trails.update();
        trails.setLocation(getX() + trailsOffset.x,
                           getY() + trailsOffset.y);
    }

    @Override
    public void draw(Graphics g)
    {
        trails.draw(g);
        super.draw(g);
    }

    // Generated by mask tool
    @Override
    public Polygon getPolygonMask()
    {
        return new Polygon(new int[] {6, 28, 52, 49, 43, 35, 28, 13, 0}, new int[] {34, 32, 31, 18, 10, 6, 5, 0, 0}, 9);
    }

    @Override public int getWidth() { return 53; }
    @Override public int getHeight() { return 42; }
    @Override public String getName() { return "DARKWING";}
    @Override public int getSpeed() { return 20; }
    @Override public int getMaxLife() { return 5000; }
    @Override public Image getImage() { return img; }
}
