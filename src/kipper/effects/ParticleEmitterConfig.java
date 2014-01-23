package kipper.effects;

abstract public class ParticleEmitterConfig
{
    // TODO: Add properties to render translateX, translateY but do not modify original x y
    public int getMaxParticles()
    {
        return 12;
    }

    public int getDurationTicks()
    {
        return 180;
    }

    // TODO: Pass in ParticleEmitter.ticks as argument
    public double getSpawnRate()
    {
        return 1;
    }

    public boolean isContinuous()
    {
        return false;
    }

    public boolean isRectShape(Particle p)
    {
        return false;
    }

    public int getSize(Particle p)
    {
        return 1;
    }

    public double getTheta(Particle p)
    {
        return 0;
    }

    public double getSpeed(Particle p)
    {
        return 1;
    }

    public float getHue(Particle p)
    {
        return 60f / 360f;
    }

    public float getSaturation(Particle p)
    {
        return 1;
    }

    public float getBrightness(Particle p)
    {
        return 1;
    }

    public boolean isAlive(Particle p)
    {
        return p.ticks <= getDurationTicks();
    }
}
