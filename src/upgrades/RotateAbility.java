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

	public static void drawIcon(Graphics g, int x, int y, int width, int height)
    {
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(getColor());
		g.fillRoundRect(x,y,width,height,15,15);
		g.setColor(Color.BLACK);
		g.drawString(getTitle(), x+width/2-23, y+height/2+5);
	}

	public static String getTitle() { return "Rotate"; }
	public static Color getColor() { return Color.CYAN; }
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
