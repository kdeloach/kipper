package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

public class SpreadAbility extends Ability
{
	// amount by which to increase spread
	double amount = 2.0;

	public SpreadAbility(Upgradeable v)
    {
		v.addWeaponListener(new SpreadAbilityWeaponListener(this));
	}

	@Override public String getTitle() { return "Spread"; }
	@Override public Color getColor() { return Color.RED; }
}

class SpreadAbilityWeaponListener extends WeaponListener
{
    private SpreadAbility ability;

    public SpreadAbilityWeaponListener(SpreadAbility ability)
    {
        this.ability = ability;
    }

	public double attributeCalled(String name, double value)
    {
		if (name == Ability.SPREAD) {
			return value + ability.amount;
        } else if (name == Ability.COOLDOWN) {
            // balancing act - you can fire more bullets at the price of slower firerate
			return value + 10;
        }
		return value;
	}
}
