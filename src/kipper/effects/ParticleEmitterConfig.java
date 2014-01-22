package kipper.effects;

public class ParticleEmitterConfig
{
    public int maxParticles;
    public int durationTicks;
    public double spawnRate;
    public boolean continuous;

    // TODO: Min theta, max theta, offset theta
    // TODO: Particle shape?
    // TODO: maxParticles and spawn rate should be particle funcs

    public ParticleValueFunc theta;
    public ParticleValueFunc speed;
    public ParticleValueFunc size;
    public ParticleValueFunc hue;
    public ParticleValueFunc saturation;
    public ParticleValueFunc brightness;

    public ParticleEmitterConfig()
    {
        maxParticles = 12;
        durationTicks = 180;
        spawnRate = 1;
        continuous = false;
    }

    public int getSize(Particle p)
    {
        return (int)size.call(p, this);
    }

    public double getTheta(Particle p)
    {
        return theta.call(p, this);
    }

    public double getSpeed(Particle p)
    {
        return speed.call(p, this);
    }

    public float getHue(Particle p)
    {
        return (float)hue.call(p, this);
    }

    public float getSaturation(Particle p)
    {
        return (float)saturation.call(p, this);
    }

    public float getBrightness(Particle p)
    {
        return (float)brightness.call(p, this);
    }
}
