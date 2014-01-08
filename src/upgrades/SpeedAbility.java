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

	public SpeedAbility(Upgradeable v)
    {
		v.addWeaponListener(new SpeedAbilityWeaponListener(this));
	}

	public static void drawIcon(Graphics g, int x, int y, int width, int height)
    {
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(getColor());
		g.fillRoundRect(x,y,width,height,15,15);
		g.setColor(Color.BLACK);
		g.drawString(getTitle(), x+width/2-26, y+height/2+5);
	}

	public static String getTitle() { return "Speed"; }
	public static Color getColor() { return Color.YELLOW; }
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
