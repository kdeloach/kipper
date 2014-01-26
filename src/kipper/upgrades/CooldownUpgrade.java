package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

public class CooldownUpgrade extends Upgrade
{
    double percent = 0.75;

    public CooldownUpgrade()
    {
    }

    public double getValue(Upgradable sender, String name, double value)
    {
        if (name == Upgrade.COOLDOWN) {
            return value * percent;
        }
        return value;
    }

    @Override public String getTitle() { return "Fire Rate"; }
    @Override public Color getColor() { return Color.CYAN; }
}
