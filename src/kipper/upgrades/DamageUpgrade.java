package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

public class DamageUpgrade extends Upgrade
{
    double percent = 1.25;

    public DamageUpgrade()
    {
    }

    public double getValue(Upgradable sender, String name, double value)
    {
        if (name == Upgrade.DAMAGE) {
            return value * percent;
        }
        return value;
    }

    @Override public String getTitle() { return "Damage"; }
    @Override public Color getColor() { return Color.GREEN; }
}
