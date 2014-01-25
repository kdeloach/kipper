package kipper.effects;

import kipper.effects.transitions.*;

public class SampleConfigImpl extends ParticleEmitterConfig
{
    @Override public int getMaxParticles() { return 10; }
    @Override public int getDurationTicks() { return 50; }
    @Override public double getSpawnRate() { return 1; }
    @Override public boolean isContinuous() { return false; }
    @Override public boolean isRectShape(Particle p) { return false; }
    @Override public int getSize(Particle p) { return (int)(new EaseOutCubic().call(p.ticks, 15, -15, getDurationTicks())); }
    @Override public double getTheta(Particle p) { return p.ticks < 1 ? Math.random() * 2 * Math.PI : p.theta; }
    @Override public double getSpeed(Particle p) { return p.ticks < 1 ? Math.random() * 2 : p.speed; }
    @Override public float getHue(Particle p) { return 60f / 360f; }
    @Override public float getSaturation(Particle p) { return 1; }
    @Override public float getBrightness(Particle p) { return 1; }
}
