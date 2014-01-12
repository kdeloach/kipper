package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

public class ProjectileSpeedUpgrade extends Ability
{
	double percent = 1.10;

	public ProjectileSpeedUpgrade()
    {
	}

	public double getValue(Upgradable sender, String name, double value)
    {
		if (name == Ability.SPEED) {
			return value * percent;
        }
		return value;
	}

	@Override public String getTitle() { return "Bullet Speed"; }
	@Override public Color getColor() { return Color.LIGHT_GRAY; }
}
