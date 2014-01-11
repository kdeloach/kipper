package kipper.effects;

// Source: http://www.gizma.com/easing/
public class Easing
{
    public static double linear(double t, double b, double c, double d)
    {
        return c * t / d + b;
    }

    public static double easeInQuad(double t, double b, double c, double d)
    {
        t /= d;
        return c*t*t + b;
    }

    public static double easeOutQuad(double t, double b, double c, double d)
    {
        t /= d;
        return -c * t * (t - 2) + b;
    }

    public static double easeOutCubic(double t, double b, double c, double d)
    {
        t /= d;
        t--;
        return c * (t*t*t + 1) + b;
    }
}
