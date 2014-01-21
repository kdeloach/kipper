package kipper.effects.transitions;

// Source: http://www.gizma.com/easing/
public class EaseOutCubic extends EasingFunc
{
    @Override
    public double call(double t, double b, double c, double d)
    {
        t /= d;
        t--;
        return c * (t*t*t + 1) + b;
    }
}
