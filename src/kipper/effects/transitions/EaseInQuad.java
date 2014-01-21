package kipper.effects.transitions;

// Source: http://www.gizma.com/easing/
public class EaseInQuad extends EasingFunc
{
    @Override
    public double call(double t, double b, double c, double d)
    {
        t /= d;
        return c*t*t + b;
    }
}
