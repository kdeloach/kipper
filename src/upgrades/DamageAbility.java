package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;

// Increase weapon damage
public class DamageAbility extends Ability
{
	double percent = 1.20;

	public DamageAbility(Upgradeable v)
    {
		v.addWeaponListener(new DamageAbilityWeaponListener(this));
	}

	public static void drawIcon(Graphics g, int x, int y, int width, int height)
    {
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(getColor());
		g.fillRoundRect(x,y,width,height,15,15);
		g.setColor(Color.BLACK);
		g.drawString(getTitle(), x+width/2-30, y+height/2+5);
	}

	public static String getTitle() { return "Damage"; }
	public static Color getColor() { return Color.GREEN; }
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
