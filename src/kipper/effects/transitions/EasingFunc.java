package kipper.effects.transitions;

abstract public class EasingFunc
{
    abstract public double call(double t, double b, double c, double d);
}
