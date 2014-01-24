package kipper.effects;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Arrays;
import kipper.*;

public class ParticleEmitter implements Entity
{
    private double x, y;
    private int particleID = 0, ticks = 0;

    private int numAlive = 0, maxNumAlive = 0;
    private int population = 0;
    // Indexes < numAlive are considered alive, >= numAlive are considered dead
    private Particle[] particles;
    private ParticleEmitterConfig config, nextConfig;

    public ParticleEmitter(double x, double y, ParticleEmitterConfig config)
    {
        this.x = x;
        this.y = y;
        this.config = config;
        this.particles = new Particle[config.getMaxParticles()];
        initParticles();
    }

    private void initParticles()
    {
        for (int i = population; i < particles.length; i++) {
            particles[i] = new Particle();
        }
        maxNumAlive = 0;
        numAlive = Math.min(numAlive, particles.length);
        population = particles.length;
    }

    public void setConfig(ParticleEmitterConfig config)
    {
        nextConfig = config;
    }

    private void updateConfig()
    {
        if (nextConfig == null) {
            return;
        }
        if (nextConfig.getMaxParticles() != config.getMaxParticles()) {
            particles = Arrays.copyOf(particles, nextConfig.getMaxParticles());
            initParticles();
        }
        config = nextConfig;
        nextConfig = null;
    }

    public boolean canSpawnParticle()
    {
        return config.getSpawnRate() > 0 && numAlive < config.getMaxParticles();
    }

    private boolean chanceToSpawn()
    {
        return config.getSpawnRate() == 1 || Math.random() < config.getSpawnRate();
    }

    private void spawnParticle()
    {
        Particle p = particles[numAlive++];
        p.id = particleID++;
        // TODO: Call config.startX(p), config.startY(p)
        p.x = this.x;
        p.y = this.y;
        p.ticks = 0;
        p.speed = config.getSpeed(p);
        p.theta = config.getTheta(p);
        p.size = config.getSize(p);
        p.hue = config.getHue(p);
        p.saturation = config.getSaturation(p);
        p.brightness = config.getBrightness(p);
    }

    @Override
    public void update()
    {
        while (canSpawnParticle() && chanceToSpawn()) {
            spawnParticle();
        }

        for (int i = 0; i < numAlive; i++) {
            Particle p = particles[i];
            if (config.isAlive(p)) {
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
                Particle tmp = particles[numAlive - 1];
                particles[numAlive - 1] = p;
                particles[i] = tmp;
                numAlive--;
                i--;
            }
        }
        maxNumAlive = Math.max(maxNumAlive, numAlive);

        updateConfig();
        ticks++;
    }

    @Override
    public void draw(Graphics g)
    {
        for (int i = 0; i < numAlive; i++) {
            Particle p = particles[i];
            g.setColor(Color.getHSBColor(p.hue, p.saturation, p.brightness));
            if (config.isRectShape(p)) {
                g.fillRect((int)(p.x - p.size / 2),
                           (int)(p.y - p.size / 2), p.size, p.size);
            } else {
                g.fillOval((int)(p.x - p.size / 2),
                           (int)(p.y - p.size / 2), p.size, p.size);

            }
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
        return config.isContinuous() || ticks <= config.getDurationTicks();
    }

    @Override
    public void die()
    {
        ticks = config.getDurationTicks() + 1;
    }

    @Override
    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public int getTicks() { return ticks; }
    public int getNumAlive() { return numAlive; }
    public int getMaxNumAlive() { return maxNumAlive; }
    public int getPopulation() { return population; }
}
