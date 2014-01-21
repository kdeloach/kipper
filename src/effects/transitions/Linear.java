package kipper.effects.transitions;

// Source: http://www.gizma.com/easing/
public class Linear extends EasingFunc
{
    @Override
    public double call(double t, double b, double c, double d)
    {
        return c * t / d + b;
    }
}