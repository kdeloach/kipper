package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// Allows weapons to fire at any angle instead of fixed on X axis
public class RotateAbility extends Ability
{
	public RotateAbility()
    {
	}

	public double getValue(Upgradable sender, String name, double value)
    {
        Weapon w = (Weapon)sender;
		if (name == Ability.HEADING) {
			return heading(w.ship());
		}
		return value;
	}

	private double heading(Ship ship)
    {
		double x = ship.mousePressed.x - (ship.x + ship.width);
		double y = ship.mousePressed.y - (ship.y + ship.height / 2);
		return Math.PI * ship.getOrientation() + Math.atan(y / x);
	}

	@Override public String getTitle() { return "Rotate"; }
	@Override public Color getColor() { return Color.CYAN; }
}
