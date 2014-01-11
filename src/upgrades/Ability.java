package kipper.upgrades;

import java.awt.Color;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

public abstract class Ability
{
	public static final String COOLDOWN = "COOLDOWN";
	public static final String DAMAGE = "DAMAGE";
	public static final String SPREAD = "SPREAD";
	public static final String GUARD = "GUARD";
	public static final String HEADING = "HEADING";

    public void weaponFired(Weapon w) {}
    // Note: Not really needed anymore
    public void destroy() {}

    // usage ex: when getSpeed() is called this function gets called first with argument "speed" and is added to weapon speed/cooldown
	public double getValue(Upgradable sender, String name, double oldvalue)
    {
        return oldvalue;
    }

	abstract public String getTitle();
	abstract public Color getColor();

	public AbilityIconDrawer getIconDrawer()
    {
        return new AbilityIconDrawer(getTitle(), getColor());
    }

    public static Ability createInstance(int index)
    {
        switch (index) {
            case Const.SPEED: return new SpeedAbility();
            case Const.DAMAGE: return new DamageAbility();
            case Const.SPREAD: return new SpreadAbility();
            case Const.RECOIL: return new RecoilAbility();
            case Const.ROTATE: return new RotateAbility();
        }
        throw new UnsupportedOperationException("Unable to create ability instance: " + index);
    }
}
