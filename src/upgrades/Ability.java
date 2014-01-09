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

    private Upgradeable vehicle;
    private WeaponListener listener;

    protected void attachListener(Upgradeable vehicle, WeaponListener listener)
    {
        if (this.listener != null) {
            throw new UnsupportedOperationException("Should not call attachListener more than once");
        }
        this.vehicle = vehicle;
        this.listener = listener;
        vehicle.addWeaponListener(listener);
    }

    public void destroy()
    {
        vehicle.removeWeaponListener(listener);
    }

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
