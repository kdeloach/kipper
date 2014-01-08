package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// Right now only works when applied to a ship
public class RotateAbility extends Ability
{
	Ship ship;

	public RotateAbility(Upgradeable v)
    {
        this.ship = (Ship)v;
		v.addWeaponListener(new RotateAbilityWeaponListener(this));
	}

	@Override public String getTitle() { return "Rotate"; }
	@Override public Color getColor() { return Color.CYAN; }
}

class RotateAbilityWeaponListener extends WeaponListener
{
    private RotateAbility ability;

    public RotateAbilityWeaponListener(RotateAbility ability)
    {
        this.ability = ability;
    }

	public double attributeCalled(String name, double value)
    {
		if (name == Ability.HEADING) {
			return heading();
		}
		return value;
	}

	private double heading()
    {
        Ship ship = ability.ship;
		double x = ship.mousePressed.x - (ship.x + ship.width);
		double y = ship.mousePressed.y - (ship.y + ship.height / 2);
		return Math.PI * ship.getOrientation() + Math.atan(y / x);
	}
}
