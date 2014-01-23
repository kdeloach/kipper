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
    private int particleID = 0, ticks = 0;
    private boolean alive = true;

    private LinkedList<Particle> pz;
    private LinkedList<Particle> graveyard;
    private ParticleEmitterConfig config;

    public ParticleEmitter(double x, double y, ParticleEmitterConfig config)
    {
        this.x = x;
        this.y = y;
        this.config = config;
        this.pz = new LinkedList<Particle>();
        this.graveyard = new LinkedList<Particle>();
    }

    public boolean canSpawnParticle()
    {
        return config.spawnRate > 0 && pz.size() < config.maxParticles;
    }

    private boolean chanceToSpawn()
    {
        return config.spawnRate == 1 || Math.random() < config.spawnRate;
    }

    private void spawnParticle()
    {
        Particle p;
        try {
            p = graveyard.pop();
        } catch (NoSuchElementException ex) {
            p = new Particle();
        }
        p.id = particleID++;
        p.x = 0;
        p.y = 0;
        p.ticks = 0;
        p.speed = config.getSpeed(p);
        p.theta = config.getTheta(p);
        p.size = config.getSize(p);
        p.hue = config.getHue(p);
        p.saturation = config.getSaturation(p);
        p.brightness = config.getBrightness(p);
        pz.add(p);
    }

    @Override
    public void update()
    {
        while (canSpawnParticle() && chanceToSpawn()) {
            spawnParticle();
        }

        Iterator<Particle> iter = pz.iterator();
        while (iter.hasNext()) {
            Particle p = iter.next();
            if (isParticleAlive(p)) {
                p.speed = config.getSpeed(p);
                p.theta = config.getTheta(p);
                p.size = config.getSize(p);
                p.hue = config.getHue(p);
                p.saturation = config.getSaturation(p);
                p.brightness = config.getBrightness(p);
                p.x += Math.cos(p.theta) * p.speed;
                p.y += Math.sin(p.theta) * p.speed;
                p.ticks++;
            } else {
                iter.remove();
                graveyard.push(p);
            }
        }

        ticks++;
    }

    @Override
    public void draw(Graphics g)
    {
        for (Particle p : pz) {
            g.setColor(Color.getHSBColor(p.hue, p.saturation, p.brightness));
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
    @Override public void hit(double damage) { alive = false; }
    @Override public void collide(Entity e) { }

    @Override
    public boolean isAlive()
    {
        return alive && (config.continuous || ticks <= config.durationTicks);
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
}
