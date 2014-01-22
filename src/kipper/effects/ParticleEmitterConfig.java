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
    public ParticleValueFunc H;
    public ParticleValueFunc S;
    public ParticleValueFunc B;

    public ParticleEmitterConfig()
    {
        maxParticles = 12;
        durationTicks = 180;
        spawnRate = 1;
        continuous = false;
    }
}
