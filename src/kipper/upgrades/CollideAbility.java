package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

public class CollideAbility extends Ability
{
    public CollideAbility()
    {
    }

    public double getValue(Upgradable sender, String name, double value)
    {
        if (name == Ability.COLLIDE) {
            return 1;
        }
        return value;
    }

    @Override public String getTitle() { return "Collide"; }
    @Override public Color getColor() { return Color.WHITE; }
}
