package kipper.effects;

import kipper.Entity;
import kipper.OuterSpacePanel;
import kipper.effects.transitions.Linear;
import kipper.effects.transitions.EasingFunc;

public class EntityWobble
{
    private int ticks = 0;
    private double b, c, d, amount;
    private EasingFunc easer;

    public EntityWobble()
    {
        b = Math.random() * Math.PI * 2;
        c = Math.PI * 2;
        d = 2000.0;
        amount = 0.3;
        easer = new Linear();
    }

    public void move(Entity e)
    {
        double t = (double)ticks++ * OuterSpacePanel.FPS;
        if (t > d) {
            ticks = 0;
        }
        double n = easer.call(t, b, c, d);
        e.setLocation(e.getX(), e.getY() + Math.sin(n) * amount);
    }
}
