package kipper.effects;

import kipper.Entity;
import kipper.OuterSpacePanel;

public class EntityWobble
{
    private int ticks = 0;
    private double b, c, d, amount;

    public EntityWobble()
    {
        b = Math.random() * (Math.PI * 2);
        c = Math.PI * 2;
        d = 2000.0;
        amount = 0.75;
    }

    public void move(Entity e)
    {
        double t = (double)ticks++ * OuterSpacePanel.FPS;
        if (t > d) {
            ticks = 0;
        }
        double n = Easing.linear(t, b, c, d);
        e.setLocation(e.getX(), e.getY() + Math.sin(n) * amount);
    }
}
