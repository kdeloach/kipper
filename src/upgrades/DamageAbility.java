package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

// Increase weapon damage
public class DamageAbility extends Ability
{
	double percent = 1.25;

	public DamageAbility()
    {
	}

	public double getValue(Upgradable sender, String name, double value)
    {
		if (name == Ability.DAMAGE) {
			return value * percent;
        }
		return value;
	}

    @Override public String getTitle() { return "Damage"; }
    @Override public Color getColor() { return Color.GREEN; }
}
