package kipper.effects.transitions;

// Source: http://www.gizma.com/easing/
public class EaseOutQuad extends EasingFunc
{
    @Override
    public double call(double t, double b, double c, double d)
    {
        t /= d;
        return -c * t * (t - 2) + b;
    }
}
