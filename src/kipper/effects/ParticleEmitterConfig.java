package kipper.effects;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;
import kipper.*;
import kipper.effects.transitions.*;
import particledsl.*;
import particledsl.tokens.*;

public class ParticleEmitterConfig
{
    public int maxParticles;
    public int durationTicks;
    public double spawnRate;
    public boolean continuous;

    // TODO: Min theta, max theta, offset theta
    // TODO: Particle shape?

    // These get called upon creation:

    // f(particleID) => ticks
    // What is the initial tick for this particle?
    // Return 0 to durationTicks
    //public IFunc2<Integer, Integer> startTick;

    public ParticleValueFunc theta;
    public ParticleValueFunc speed;
    public ParticleValueFunc hue;
    public ParticleValueFunc saturation;
    public ParticleValueFunc brightness;
    public ParticleValueFunc size;

    public ParticleEmitterConfig()
    {
        maxParticles = 12;
        durationTicks = 180;
        spawnRate = 1;
        continuous = false;

        hue = new ParticleLang("0xFF+linear(0, 180)/180").getValue();
        saturation = new ParticleLang("1").getValue();
        brightness = new ParticleLang("linear(1, 0)").getValue();

        //size = new EasedValue(new Linear(), new ConstantValue(10), new ConstantValue(0));
        //size = new ParticleLang("linear(10, 1)").getValue()
        size = new ParticleLang("linear(10, 1)").getValue();

        //theta = new RandomValue(0, Math.PI * 2);
        //theta = new EasedValue(new Linear(), new ConstantValue(0), new ConstantValue(2*Math.PI));
        //theta = new RadianValue(new ParticleIdValue());
        // theta = new CondValue(
            // new RangeCond(new ParticleTicksValue(), 0, 1),
            // new RandomValue(0, Math.PI * 2),
            // new CondValue(
                // new RangeCond(new ParticleTicksValue(), 90, 91),
                // new AddValue(new ParticleThetaValue(), new ConstantValue(Math.toRadians(45))),
                // new ParticleThetaValue()));
        theta = new CondValue(
            new RangeCond(new ParticleTicksValue(), 0, 1),
            new MultValue(new RandomValue(), new ConstantValue(Math.PI * 2)),
            new ParticleThetaValue());

        //speed = new RandomValue(0, 1);
        //speed = new EasedValue(new Linear(), new RandomValue(0, 1), new ConstantValue(0));
        //speed = new EasedValue(new EaseInQuad(), new RandomValue(0, 1), new ConstantValue(0));
        speed = new ConstantValue(1);
        // speed = new CondValue(
            // new RangeCond(new ParticleTicksValue(), 0, 1),
            // new MultValue(new RandomValue(), new ConstantValue(3)),
            // new ParticleSpeedValue());
    }
}

class ParticleLang
{
    String program;

    public ParticleLang(String program)
    {
        this.program = program;
    }

    public ParticleValueFunc getValue()
    {
        Parser parser = new Parser(new Tokenizer(program));
        Token ast = parser.expression(0);
        return walk(ast);
    }

    private ParticleValueFunc walk(Token node)
    {
        if (node instanceof NumberToken) {
            return new ConstantValue(Double.parseDouble(node.tokenValue()));
        } else if (node instanceof IdentToken) {
            switch (node.tokenValue().toLowerCase()) {
                case "x": return new ParticleXValue();
                case "y": return new ParticleYValue();
                case "id": return new ParticleIdValue();
                case "theta": return new ParticleThetaValue();
                case "speed": return new ParticleSpeedValue();
                case "ticks": return new ParticleTicksValue();
            }
            throw new UnsupportedOperationException("Identifier not supported (" + node.tokenValue() + ")");
        } else if (node instanceof PlusToken) {
            PlusToken token = (PlusToken)node;
            return new AddValue(walk(token.left), walk(token.right));
        } else if (node instanceof MinusToken) {
            MinusToken token = (MinusToken)node;
            return new MinusValue(walk(token.left), walk(token.right));
        } else if (node instanceof StarToken) {
            StarToken token = (StarToken)node;
            return new MultValue(walk(token.left), walk(token.right));
        } else if (node instanceof SlashToken) {
            SlashToken token = (SlashToken)node;
            return new DivValue(walk(token.left), walk(token.right));
        } else if (node instanceof PercentToken) {
            PercentToken token = (PercentToken)node;
            return new ModValue(walk(token.left), walk(token.right));
        } else if (node instanceof CallFuncToken) {
            CallFuncToken token = (CallFuncToken)node;
            List<Token> args = token.args.args;
            switch (token.ident.tokenValue().toLowerCase()) {
                case "random":
                    validateArgs(token, 0);
                    return new RandomValue();
                case "min":
                    validateArgs(token, 2);
                    return new MinValue(walk(args.get(0)), walk(args.get(1)));
                case "max":
                    validateArgs(token, 2);
                    return new MaxValue(walk(args.get(0)), walk(args.get(1)));
                case "abs":
                    validateArgs(token, 1);
                    return new AbsValue(walk(args.get(0)));
                case "linear":
                    validateArgs(token, 2);
                    return new EasedValue(new Linear(), walk(args.get(0)), walk(args.get(1)));
                case "easeinquad":
                    validateArgs(token, 2);
                    return new EasedValue(new EaseInQuad(), walk(args.get(0)), walk(args.get(1)));
                case "easeoutcubic":
                    validateArgs(token, 2);
                    return new EasedValue(new EaseOutCubic(), walk(args.get(0)), walk(args.get(1)));
                case "easeoutquad":
                    validateArgs(token, 2);
                    return new EasedValue(new EaseOutQuad(), walk(args.get(0)), walk(args.get(1)));
            }
            throw new UnsupportedOperationException("Function not supported (" + token.ident.tokenValue() + ")");
        }
        throw new UnsupportedOperationException("Not implemented (" + node.tokenValue() + ")");
    }

    private void validateArgs(CallFuncToken token, int expectedSize)
    {
        int actualSize = token.args.args.size();
        if (actualSize != expectedSize) {
            throw new UnsupportedOperationException("Expected " + expectedSize + " arguments but received " + actualSize + " arguments (" + token.ident.tokenValue() + ")");
        }
    }
}

abstract class ParticleValueFunc
{
    abstract double call(Particle p, int maxTicks);
}

abstract class ParticleCondFunc
{
    abstract boolean isTrue(Particle p, int maxTicks);
}

class RangeCond extends ParticleCondFunc
{
    ParticleValueFunc val;
    int minTick, maxTick;

    public RangeCond(ParticleValueFunc val, int minTick, int maxTick)
    {
        this.val = val;
        this.minTick = minTick;
        this.maxTick = maxTick;
    }

    @Override
    public boolean isTrue(Particle p, int maxTicks)
    {
        return val.call(p, maxTicks) >= minTick && val.call(p, maxTicks) < maxTick;
    }
}

class CondValue extends ParticleValueFunc
{
    ParticleCondFunc cond;
    ParticleValueFunc aValue, bValue;

    public CondValue(ParticleCondFunc cond, ParticleValueFunc aValue, ParticleValueFunc bValue)
    {
        this.cond = cond;
        this.aValue = aValue;
        this.bValue = bValue;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        if (cond.isTrue(p, maxTicks)) {
            return aValue.call(p, maxTicks);
        }
        return bValue.call(p, maxTicks);
    }
}

class AddValue extends ParticleValueFunc
{
    ParticleValueFunc a, b;

    public AddValue(ParticleValueFunc a, ParticleValueFunc b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return a.call(p, maxTicks) + b.call(p, maxTicks);
    }
}

class MinusValue extends ParticleValueFunc
{
    ParticleValueFunc a, b;

    public MinusValue(ParticleValueFunc a, ParticleValueFunc b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return a.call(p, maxTicks) - b.call(p, maxTicks);
    }
}

class MultValue extends ParticleValueFunc
{
    ParticleValueFunc a, b;

    public MultValue(ParticleValueFunc a, ParticleValueFunc b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return a.call(p, maxTicks) * b.call(p, maxTicks);
    }
}

class DivValue extends ParticleValueFunc
{
    ParticleValueFunc a, b;

    public DivValue(ParticleValueFunc a, ParticleValueFunc b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return a.call(p, maxTicks) / b.call(p, maxTicks);
    }
}

class ModValue extends ParticleValueFunc
{
    ParticleValueFunc a, b;

    public ModValue(ParticleValueFunc a, ParticleValueFunc b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return a.call(p, maxTicks) % b.call(p, maxTicks);
    }
}

// Convert another value to radian
class RadianValue extends ParticleValueFunc
{
    ParticleValueFunc val;

    public RadianValue(ParticleValueFunc val)
    {
        this.val = val;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return Math.toRadians(val.call(p, maxTicks));
    }
}

class ParticleXValue extends ParticleValueFunc
{
    @Override
    public double call(Particle p, int maxTicks)
    {
        return p.x;
    }
}

class ParticleYValue extends ParticleValueFunc
{
    @Override
    public double call(Particle p, int maxTicks)
    {
        return p.y;
    }
}

class ParticleIdValue extends ParticleValueFunc
{
    @Override
    public double call(Particle p, int maxTicks)
    {
        return p.id;
    }
}

class ParticleThetaValue extends ParticleValueFunc
{
    @Override
    public double call(Particle p, int maxTicks)
    {
        return p.theta;
    }
}

class ParticleSpeedValue extends ParticleValueFunc
{
    @Override
    public double call(Particle p, int maxTicks)
    {
        return p.speed;
    }
}

class ParticleTicksValue extends ParticleValueFunc
{
    @Override
    public double call(Particle p, int maxTicks)
    {
        return p.ticks;
    }
}

class RandomValue extends ParticleValueFunc
{
    public RandomValue()
    {
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return Math.random();
    }
}

class MinValue extends ParticleValueFunc
{
    ParticleValueFunc a, b;

    public MinValue(ParticleValueFunc a, ParticleValueFunc b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return Math.min(a.call(p, maxTicks), b.call(p, maxTicks));
    }
}

class MaxValue extends ParticleValueFunc
{
    ParticleValueFunc a, b;

    public MaxValue(ParticleValueFunc a, ParticleValueFunc b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return Math.max(a.call(p, maxTicks), b.call(p, maxTicks));
    }
}

class AbsValue extends ParticleValueFunc
{
    ParticleValueFunc a;

    public AbsValue(ParticleValueFunc a)
    {
        this.a = a;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return Math.abs(a.call(p, maxTicks));
    }
}

class EasedValue extends ParticleValueFunc
{
    EasingFunc easer;
    ParticleValueFunc startValue, endValue;

    public EasedValue(EasingFunc easer, ParticleValueFunc startValue, ParticleValueFunc endValue)
    {
        this.easer = easer;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        double b = startValue.call(p, maxTicks);
        double c = endValue.call(p, maxTicks) - b;
        return easer.call(p.ticks, b, c, maxTicks);
    }
}

class ConstantValue extends ParticleValueFunc
{
    double n;

    public ConstantValue(double n)
    {
        this.n = n;
    }

    @Override
    public double call(Particle p, int maxTicks)
    {
        return this.n;
    }
}
