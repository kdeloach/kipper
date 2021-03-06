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
import kipper.ships.controllers.*;

public class Darkwing extends Enterprise
{
    Image img;
    ParticleEmitter trails;
    Point trailsOffset = new Point(5, 18);

    public Darkwing(int x, int y, OuterSpacePanel c)
    {
        super(x, y, c);
        img = Util.instance.loadImage("/assets/images/darkwing.png");
        trails = new ParticleEmitter(0, 0, new ShipTrails());
        selectWeapon(0);
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
        //drawRubberBand(g);
        //drawVector(g);
    }

    public void drawVector(Graphics g)
    {
        if (getController() instanceof ElasticShipController) {
            g.setColor(Color.WHITE);
            ElasticShipController c = (ElasticShipController)getController();
            double x1 = getX() + getWidth() / 2;
            double y1 = getY() + getHeight() / 2;
            double x2 = x1 + c.vx*10;
            double y2 = y1 + c.vy*10;
            Util.drawThickLine(g, (int)x1, (int)y1, (int)x2, (int)y2, 3);
        }
    }

    public void drawRubberBand(Graphics g)
    {
        Point mouse = Util.scalePoint(Global.mouse.getPoint());
        g.setColor(Color.GREEN);
        g.drawLine((int)(getX() + getWidth() / 2),
                   (int)(getY() + getHeight() / 2),
                   (int)mouse.x,
                   (int)mouse.y);
    }

    @Override
    protected void deathExplosion()
    {
        double px = getX() + getWidth() / 2;
        double py = getY() + getHeight() / 2;
        ParticleEmitter pe = new ParticleEmitter(px, py, new DarkwingExplosion());
        osp.addEmitter(pe);
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
    @Override public int getMaxLife() { return 500; }
    @Override public Image getImage() { return img; }
}
