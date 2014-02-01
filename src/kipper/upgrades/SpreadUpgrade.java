package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

public class SpreadUpgrade extends Upgrade
{
    public SpreadUpgrade()
    {
    }

    public double getValue(Upgradable sender, String name, double value)
    {
        if (name == Upgrade.SPREAD) {
            return value + 1;
        } else if (name == Upgrade.COOLDOWN) {
            //return value + 10;
        }
        return value;
    }

    @Override public String getTitle() { return "Spread"; }
    @Override public Color getColor() { return Color.RED; }
}
