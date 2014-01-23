package kipper.effects;

import java.util.*;
import kipper.effects.transitions.*;
import particledsl.*;
import particledsl.tokens.*;

public class ParticleLang
{
    String expression;

    public ParticleLang(String expression)
    {
        this.expression = expression;
    }

    public ParticleValueFunc getValue()
    {
        Parser parser = new Parser(new Tokenizer(expression));
        Token ast = parser.expression(0);
        return walk(ast);
    }

    private ParticleValueFunc walk(Token node)
    {
        if (node instanceof NumberToken) {
            return new ConstantValue(Double.parseDouble(node.tokenValue()));
        } else if (node instanceof IdentToken) {
            switch (node.tokenValue().toLowerCase()) {
                case "pi": return new ConstantValue(Math.PI);
            }
            return new ParticleValue(node.tokenValue());
        } else if (node instanceof BinaryMathToken) {
            BinaryMathToken token = (BinaryMathToken)node;
            return new BinaryMathValue(token.tokenValue(), walk(token.left), walk(token.right));
        } else if (node instanceof CondToken) {
            CondToken token = (CondToken)node;
            return new CondValue(walkCond(token.cond), walk(token.trueBody), walk(token.otherBody));
        } else if (node instanceof CallFuncToken) {
            CallFuncToken token = (CallFuncToken)node;
            String funcName = token.ident.tokenValue();
            LinkedList<Token> args = token.args.args;
            try {
                switch (funcName.toLowerCase()) {
                    case "random": return new RandomValue();
                    case "radians": return new RadianValue(walk(args.pop()));
                    case "min": return new MinValue(walk(args.pop()), walk(args.pop()));
                    case "max": return new MaxValue(walk(args.pop()), walk(args.pop()));
                    case "abs": return new AbsValue(walk(args.pop()));
                    case "round": return new RoundValue(walk(args.pop()));
                    case "floor": return new FloorValue(walk(args.pop()));
                    case "ceil": return new CeilValue(walk(args.pop()));
                    case "cos": return new CosValue(walk(args.pop()));
                    case "sin": return new SinValue(walk(args.pop()));
                    case "tan": return new TanValue(walk(args.pop()));
                    case "linear": return new EasedValue(new Linear(), walk(args.pop()), walk(args.pop()));
                    case "easeinquad": return new EasedValue(new EaseInQuad(), walk(args.pop()), walk(args.pop()));
                    case "easeoutcubic": return new EasedValue(new EaseOutCubic(), walk(args.pop()), walk(args.pop()));
                    case "easeoutquad": return new EasedValue(new EaseOutQuad(), walk(args.pop()), walk(args.pop()));
                }
                throw new UnsupportedOperationException("Function not supported (" + funcName + ")");
            } catch (NoSuchElementException ex) {
                throw new UnsupportedOperationException("Not enough arguments provided (" + funcName + ")");
            }
        }
        throw new NotImplementedException(node.tokenValue());
    }

    private ParticleCondFunc walkCond(Token node)
    {
        if (node instanceof BinaryCompareToken) {
            BinaryCompareToken token = (BinaryCompareToken)node;
            return new BinaryCompareCond(node.tokenValue(), walk(token.left), walk(token.right));
        } else if (node instanceof LogicalOpToken) {
            LogicalOpToken token = (LogicalOpToken)node;
            return new LogicalOpCond(node.tokenValue(), walkCond(token.left), walkCond(token.right));
        } else if (node instanceof TrueFalseToken) {
            return new BooleanConstantCond(node.tokenValue());
        }
        throw new NotImplementedException(node.tokenValue());
    }
}

abstract class ParticleValueFunc
{
    abstract double call(Particle p, ParticleEmitterConfig config);
}

abstract class ParticleCondFunc
{
    abstract boolean isTrue(Particle p, ParticleEmitterConfig config);
}

class BinaryCompareCond extends ParticleCondFunc
{
    String op;
    ParticleValueFunc left, right;

    public BinaryCompareCond(String op, ParticleValueFunc left, ParticleValueFunc right)
    {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public boolean isTrue(Particle p, ParticleEmitterConfig config)
    {
        double a = left.call(p, config);
        double b = right.call(p, config);
        switch (op) {
            case "==": return a == b;
            case "!=": return a != b;
            case "<=": return a <= b;
            case ">=": return a >= b;
            case "<": return a < b;
            case ">": return a > b;
        }
        throw new NotImplementedException(op);
    }
}

class LogicalOpCond extends ParticleCondFunc
{
    String op;
    ParticleCondFunc left, right;

    public LogicalOpCond(String op, ParticleCondFunc left, ParticleCondFunc right)
    {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public boolean isTrue(Particle p, ParticleEmitterConfig config)
    {
        boolean a = left.isTrue(p, config);
        boolean b = right.isTrue(p, config);
        switch (op) {
            case "and": return a && b;
            case "or": return a || b;
        }
        throw new NotImplementedException(op);
    }
}

class BooleanConstantCond extends ParticleCondFunc
{
    String name;

    public BooleanConstantCond(String name)
    {
        this.name = name;
    }

    public boolean isTrue(Particle p, ParticleEmitterConfig config)
    {
        return name == "true";
    }
}

class CondValue extends ParticleValueFunc
{
    ParticleCondFunc cond;
    ParticleValueFunc trueBody, otherBody;

    public CondValue(ParticleCondFunc cond, ParticleValueFunc trueBody, ParticleValueFunc otherBody)
    {
        this.cond = cond;
        this.trueBody = trueBody;
        this.otherBody = otherBody;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        if (cond.isTrue(p, config)) {
            return trueBody.call(p, config);
        }
        return otherBody.call(p, config);
    }
}

class BinaryMathValue extends ParticleValueFunc
{
    String op;
    ParticleValueFunc left, right;

    public BinaryMathValue(String op, ParticleValueFunc left, ParticleValueFunc right)
    {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        double a = left.call(p, config);
        double b = right.call(p, config);
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
            case "%": return a % b;
        }
        throw new NotImplementedException(op);
    }
}

class RadianValue extends ParticleValueFunc
{
    ParticleValueFunc val;

    public RadianValue(ParticleValueFunc val)
    {
        this.val = val;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.toRadians(val.call(p, config));
    }
}

class ParticleValue extends ParticleValueFunc
{
    String name;

    public ParticleValue(String name)
    {
        this.name = name;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        switch (name) {
            case "id": return p.id;
            case "ticks": return p.ticks;
            case "size": return p.size;
            case "x": return p.x;
            case "y": return p.y;
            case "theta": return p.theta;
            case "speed": return p.speed;
            case "hue": return p.hue;
            case "saturation": return p.saturation;
            case "brightness": return p.brightness;
        }
        throw new UnsupportedOperationException("Property does not exist (" + name + ")");
    }
}

class RandomValue extends ParticleValueFunc
{
    @Override
    public double call(Particle p, ParticleEmitterConfig config)
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
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.min(a.call(p, config), b.call(p, config));
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
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.max(a.call(p, config), b.call(p, config));
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
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.abs(a.call(p, config));
    }
}

class RoundValue extends ParticleValueFunc
{
    ParticleValueFunc a;

    public RoundValue(ParticleValueFunc a)
    {
        this.a = a;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.round(a.call(p, config));
    }
}

class FloorValue extends ParticleValueFunc
{
    ParticleValueFunc a;

    public FloorValue(ParticleValueFunc a)
    {
        this.a = a;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.floor(a.call(p, config));
    }
}

class CeilValue extends ParticleValueFunc
{
    ParticleValueFunc a;

    public CeilValue(ParticleValueFunc a)
    {
        this.a = a;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.ceil(a.call(p, config));
    }
}

class CosValue extends ParticleValueFunc
{
    ParticleValueFunc a;

    public CosValue(ParticleValueFunc a)
    {
        this.a = a;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.cos(a.call(p, config));
    }
}

class SinValue extends ParticleValueFunc
{
    ParticleValueFunc a;

    public SinValue(ParticleValueFunc a)
    {
        this.a = a;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.sin(a.call(p, config));
    }
}

class TanValue extends ParticleValueFunc
{
    ParticleValueFunc a;

    public TanValue(ParticleValueFunc a)
    {
        this.a = a;
    }

    @Override
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return Math.tan(a.call(p, config));
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
    public double call(Particle p, ParticleEmitterConfig config)
    {
        double b = startValue.call(p, config);
        double c = endValue.call(p, config) - b;
        return easer.call(p.ticks, b, c, config.durationTicks);
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
    public double call(Particle p, ParticleEmitterConfig config)
    {
        return this.n;
    }
}
