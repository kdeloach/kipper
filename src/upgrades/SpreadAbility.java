package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

public class SpreadAbility extends Ability
{
    double amount = 2.0;

    public SpreadAbility()
    {
    }

    public double getValue(Upgradable sender, String name, double value)
    {
        if (name == Ability.SPREAD) {
            return value + amount;
        } else if (name == Ability.COOLDOWN) {
            return value + 10;
        }
        return value;
    }

    @Override public String getTitle() { return "Spread"; }
    @Override public Color getColor() { return Color.RED; }
}
