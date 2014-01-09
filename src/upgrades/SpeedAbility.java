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

	public SpeedAbility(Upgradable v)
    {
        attachListener(v, new SpeedAbilityWeaponListener(this));
	}

	@Override public String getTitle() { return "Speed"; }
	@Override public Color getColor() { return Color.YELLOW; }
}

class SpeedAbilityWeaponListener extends WeaponListener
{
    private SpeedAbility ability;

    public SpeedAbilityWeaponListener(SpeedAbility ability)
    {
        this.ability = ability;
    }

	public double attributeCalled(String name, double value)
    {
		if (name == Ability.COOLDOWN) {
			return value * (1 - ability.percent);
        }
		return value;
	}
}
