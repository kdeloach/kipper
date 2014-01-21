package kipper.effects;

import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import kipper.Util;
import kipper.Entity;
import kipper.OuterSpacePanel;
import kipper.effects.transitions.EaseOutQuad;

public class Explosion implements Entity
{
    private double x, y;
    private int ticks = 0;
    private Particle[] shrap;
    private OuterSpacePanel osp;

    public Explosion(double x, double y, OuterSpacePanel c)
    {
        this.x = x;
        this.y = y;
        this.osp = c;

        initParticles();

        osp.addExplosion(this);
    }

    protected void initParticles()
    {
        shrap = new Particle[getAmount()];
        for (int i = 0; i < shrap.length; i++) {
            shrap[i] = createParticle(x, y, particleAngle(), particleDistance());
        }
    }

    public Particle createParticle(double x, double y, double theta, double distance)
    {
        throw new UnsupportedOperationException();
    }

    public void tick(double t)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update()
    {
        tick(ticks);
        ticks++;
    }

    @Override
    public void draw(Graphics g)
    {
        g.setColor(getColor());
        for (Particle d : shrap) {
            g.fillRect((int)d.x, (int)d.y, getWidth(), getHeight());
        }
    }

    // Represents number of times update() must be called until animation ends
    private int getDurationTicks()
    {
        return Util.msToTicks(getDurationMs());
    }

    public int getDurationMs() { return 500; }
    public Color getColor() { return Color.YELLOW; }
    public int getAmount() { return 10; }
    public double particleAngle() { return Math.toRadians(Util.randRange(0, 360)); }
    public double particleDistance() { return Math.random() * 20 + 10; }

    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public int getWidth() { return 3; }
    @Override public int getHeight() { return 3; }
    @Override public int getLife() { return 0; }
    @Override public int getTeam() { return 0; }
    @Override public boolean isAlive() { return ticks <= getDurationTicks(); }
    @Override public void hit(double damage) { }
    @Override public void collide(Entity e) { }
    @Override public void die() { }

    @Override
    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
}
