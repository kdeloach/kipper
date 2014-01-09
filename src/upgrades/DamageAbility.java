package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.weapons.*;

// Increase weapon damage
public class DamageAbility extends Ability
{
	double percent = 1.20;

	public DamageAbility(Upgradeable v)
    {
		attachListener(v, new DamageAbilityWeaponListener(this));
	}

    @Override public String getTitle() { return "Damage"; }
    @Override public Color getColor() { return Color.GREEN; }
}

class DamageAbilityWeaponListener extends WeaponListener
{
    private DamageAbility ability;

    public DamageAbilityWeaponListener(DamageAbility ability)
    {
        this.ability = ability;
    }

	public double attributeCalled(String name, double value)
    {
		if (name == Ability.DAMAGE) {
			return value * ability.percent;
        }
		return value;
	}
}
