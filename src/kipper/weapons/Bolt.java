package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;
import kipper.effects.transitions.*;

// Bolts are basically 2 points, whereas the first point is x,y
//   and the second point is x+length*cos(theta),y+length*sin(theta)

// TODO: Extend from bullet...
public class Bolt implements MaskedEntity, Projectile
{
    // length is the size of 1 segment of the largest tier branches
    private int branches, length, life, lifespan, thickness;

    private double damage;

    // bullet trajectory
    private double theta, offset;

    // start and end of beam
    private Point2D.Double start, stop;

    // master panel
    private Weapon weapon;

    private boolean alive = true;

    private EasingFunc easer = new EaseInQuad();

    public Bolt(double x, double y, double t, double dmg, Weapon w)
    {
        x = x - w.ship().x;
        y = y - w.ship().y;
        new Bolt(x, y, t, getDefaultBranches(), getDefaultLength(), getDefaultLifespanTicks(), getDefaultThickness(), dmg, w);
    }

    public Bolt(double x, double y, double offset, int branches, int length, int lifespan, int thickness, double dmg, Weapon w)
    {
        this.offset = offset;
        this.branches = branches;
        this.length = length;
        this.life = lifespan;
        this.lifespan = lifespan;
        this.thickness = thickness;
        this.damage = dmg;
        this.weapon = w;

        start = new Point2D.Double();
        stop = new Point2D.Double();

        this.theta =  Math.toRadians(Math.random() * 90 - 45);
        this.theta += offset;

        setLocation(x, y);
        weapon.ship().panel().addProjectile(this);

        if (length > 10) {
            splinter();
        }
    }

    private void splinter()
    {
        double x = stop.x;
        double y = stop.y;
        for (int i = 0; i < branches; i++) {
            int branches = (int)(Math.random() * getAmtChildrenBranches());
            Bolt b = new Bolt(x, y, offset, branches, length - 10, lifespan, thickness - 1, damage, weapon);
            x = b.stop.x;
            y = b.stop.y;
        }
    }

    @Override
    public void update()
    {
        life--;
    }

    @Override
    public void collide(Entity e)
    {
        e.hit(getDamage());
        hit(getLife());
    }

    @Override
    public void die()
    {
        life = 0;
        ParticleEmitter pe = new ParticleEmitter(getX(), getY(), new SampleConfigImpl());
        weapon.ship().panel().addEmitter(pe);
    }

    @Override
    public void draw(Graphics g)
    {
        int a = (int)easer.call(life, 0, 0xFF, lifespan);
        g.setColor(new Color(0xFF, 0xFF, 0xFF, a));
        if (getThickness() == 1) {
            g.drawLine((int)startX(), (int)startY(), (int)stopX(), (int)stopY());
        } else {
            Util.drawThickLine(g, startX(), startY(), stopX(), stopY(), getThickness());
        }
    }

    @Override
    public void hit(double damage)
    {
        if (isAlive()) {
            life -= damage;
            if (life <= 0) {
                die();
            }
        }
    }

    private int getDefaultLifespanTicks()
    {
        return Util.msToTicks(getDefaultLifespanMs());
    }

    protected int getDefaultLifespanMs() { return 1000; }
    protected int getDefaultLength() { return 40; }
    protected int getDefaultBranches() { return 2; }
    protected int getAmtChildrenBranches(){ return 5; }
    protected int getDefaultThickness(){ return 4; }

    public int getThickness() { return (int)(thickness * weapon.getSizeBonus()); }

    @Override public double getX() { return startX(); }
    @Override public double getY() { return startY(); }
    @Override public int getWidth() { return 1; }
    @Override public int getHeight() { return 1; }
    @Override public int getLife() { return life; }
    @Override public int getTeam() { return weapon.ship().getTeam(); }
    @Override public boolean isAlive() { return life > 0; }
    @Override public double getDamage() { return damage * life / lifespan; }
    @Override public boolean collidesWithOwner() { return false; }
    @Override public boolean collidesWithProjectiles() { return false; }
    @Override public Weapon getOwner() { return weapon; }

    @Override
    public void setLocation(double x, double y)
    {
        start.setLocation(x, y);
        stop.setLocation(x + length * Math.cos(theta), y + length * Math.sin(theta));
    }

    @Override
    public Rectangle2D.Double[] getRectMask()
    {
        return new Rectangle2D.Double[] {
            new Rectangle2D.Double(startX(), startY(), getWidth(), getHeight()),
            new Rectangle2D.Double(stopX(), stopY(), getWidth(), getHeight())
        };
    }

    public double startX() { return start.x + weapon.ship().x; }
    public double startY() { return start.y + weapon.ship().y; }
    public double stopX() { return stop.x + weapon.ship().x; }
    public double stopY() { return stop.y + weapon.ship().y; }
}
