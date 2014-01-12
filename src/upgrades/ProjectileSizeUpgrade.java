package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

public class ProjectileSizeUpgrade extends Ability
{
	double amount = 1.5;

	public ProjectileSizeUpgrade()
    {
	}

	public double getValue(Upgradable sender, String name, double value)
    {
		if (name == Ability.SIZE) {
			return value * amount;
        }
		return value;
	}

	@Override public String getTitle() { return "Bullet Size"; }
	@Override public Color getColor() { return Color.ORANGE; }
}
