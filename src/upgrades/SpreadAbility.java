package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;

public class SpreadAbility extends Ability
{
	// amount by which to increase spread
	double amount = 2.0;

	public SpreadAbility(Upgradeable v)
    {
		v.addWeaponListener(new SpreadAbilityWeaponListener(this));
	}

	public static void drawIcon(Graphics g, int x, int y, int width, int height)
    {
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(getColor());
		g.fillRoundRect(x,y,width,height,15,15);
		g.setColor(Color.BLACK);
		g.drawString(getTitle(), x+width/2-26, y+height/2+5);
	}

	public static String getTitle() { return "Spread"; }
	public static Color getColor() { return Color.RED; }
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
