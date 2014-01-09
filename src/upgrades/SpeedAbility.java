package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

// Reduces weapon cooldown
public class SpeedAbility extends Ability
{
	double percent = 0.20;

	public SpeedAbility()
    {
	}

	public double getValue(Upgradable sender, String name, double value)
    {
		if (name == Ability.COOLDOWN) {
			return value * (1 - percent);
        }
		return value;
	}

	@Override public String getTitle() { return "Speed"; }
	@Override public Color getColor() { return Color.YELLOW; }
}
