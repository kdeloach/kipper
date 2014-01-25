package kipper.effects;

import kipper.effects.transitions.*;

public class ShipTrails extends ParticleEmitterConfig
{
    @Override public int getMaxParticles() { return 25; }
    @Override public int getDurationTicks() { return 20; }
    @Override public double getSpawnRate() { return 0.5; }
    @Override public boolean isContinuous() { return true; }
    @Override public boolean isRectShape(Particle p) { return false; }
    @Override public int getSize(Particle p) { int a = 12; int b = 1; return (int)(new Linear().call(p.ticks, a, b-a, getDurationTicks())); }
    @Override public double getTheta(Particle p) { return Math.PI; }
    @Override public double getSpeed(Particle p) { return 2; }
    @Override public float getHue(Particle p) { return 210f / 360f; }
    @Override public float getSaturation(Particle p) { int a = 10; int b = 100; return (float)(new EaseOutQuad().call(p.ticks, a, b-a, getDurationTicks())/100); }
    @Override public float getBrightness(Particle p) { return 1; }
}
