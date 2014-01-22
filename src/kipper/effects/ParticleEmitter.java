package kipper.effects;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import kipper.*;

public class ParticleEmitter implements Entity
{
    private double x, y;
    private int particleID= 0, ticks = 0;

    private LinkedList<Particle> pz;
    private LinkedList<Particle> pool;
    private ParticleEmitterConfig config;

    public ParticleEmitter(double x, double y, ParticleEmitterConfig config)
    {
        this.x = x;
        this.y = y;
        this.config = config;
        this.pz = new LinkedList<Particle>();
        this.pool = new LinkedList<Particle>();
    }

    private boolean canSpawnParticle()
    {
        return config.spawnRate > 0 && pz.size() < config.maxParticles;
    }

    private void spawnParticle()
    {
        Particle p;
        try {
            p = pool.pop();
        } catch (NoSuchElementException ex) {
            p = new Particle();
        }
        p.id = particleID++;
        p.x = 0;
        p.y = 0;
        p.ticks = 0;
        p.speed = getParticleSpeed(p);
        p.theta = getParticleTheta(p);
        p.H = getParticleH(p);
        p.S = getParticleS(p);
        p.B = getParticleB(p);
        p.size = getParticleSize(p);
        pz.add(p);
    }

    @Override
    public void update()
    {
        while (canSpawnParticle() && Math.random() < config.spawnRate) {
            spawnParticle();
        }

        Iterator<Particle> iter = pz.iterator();
        while (iter.hasNext()) {
            Particle p = iter.next();
            if (isParticleAlive(p)) {
                p.speed = getParticleSpeed(p);
                p.theta = getParticleTheta(p);
                p.H = getParticleH(p);
                p.S = getParticleS(p);
                p.B = getParticleB(p);
                p.size = getParticleSize(p);
                p.x += Math.cos(p.theta) * p.speed;
                p.y += Math.sin(p.theta) * p.speed;
                p.ticks++;
            } else {
                iter.remove();
                pool.push(p);
            }
        }

        ticks++;
    }

    @Override
    public void draw(Graphics g)
    {
        for (Particle p : pz) {
            g.setColor(Color.getHSBColor(p.H, p.S, p.B));
            g.fillRect((int)(x + p.x - p.size / 2),
                       (int)(y + p.y - p.size / 2), p.size, p.size);
        }
    }

    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public int getWidth() { return 0; }
    @Override public int getHeight() { return 0; }
    @Override public int getLife() { return 0; }
    @Override public int getTeam() { return 0; }
    @Override public void hit(double damage) { }
    @Override public void collide(Entity e) { }

    @Override
    public boolean isAlive()
    {
        return config.continuous || ticks <= config.durationTicks;
    }

    @Override
    public void die()
    {
        ticks = config.durationTicks + 1;
    }

    @Override
    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    private boolean isParticleAlive(Particle p)
    {
        return p.ticks <= config.durationTicks;
    }

    private int getParticleSize(Particle p)
    {
        return (int)config.size.call(p, config.durationTicks);
    }

    private double getParticleTheta(Particle p)
    {
        return config.theta.call(p, config.durationTicks);
    }

    private double getParticleSpeed(Particle p)
    {
        return config.speed.call(p, config.durationTicks);
    }

    private float getParticleH(Particle p)
    {
        return (float)config.H.call(p, config.durationTicks);
    }

    private float getParticleS(Particle p)
    {
        return (float)config.S.call(p, config.durationTicks);
    }

    private float getParticleB(Particle p)
    {
        return (float)config.B.call(p, config.durationTicks);
    }
}
