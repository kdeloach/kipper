package kipper.upgrades;

import java.awt.Color;
import kipper.*;
import kipper.ships.*;

public abstract class Ability
{
	public static final String COOLDOWN = "COOLDOWN";
	public static final String DAMAGE = "DAMAGE";
	public static final String SPREAD = "SPREAD";
	public static final String GUARD = "GUARD";
	public static final String HEADING = "HEADING";

	abstract public String getTitle();
	abstract public Color getColor();

	private int id;
	public void setId(int n) { id = n; }
	public int getId() { return id; }

	public AbilityIconDrawer getIconDrawer()
    {
        return new AbilityIconDrawer(getTitle(), getColor());
    }

    public static Ability createInstance(int index, Ship player)
    {
        switch (index) {
            case Const.SPEED: return new SpeedAbility(player);
            case Const.DAMAGE: return new DamageAbility(player);
            case Const.SPREAD: return new SpreadAbility(player);
            case Const.RECOIL: return new RecoilAbility(player);
            case Const.ROTATE: return new RotateAbility(player);
        }
        throw new UnsupportedOperationException("Unable to create ability instance: " + index);
    }
}
