package kipper;

import kipper.ships.*;
import kipper.weapons.*;
import kipper.upgrades.*;

public interface Upgradable
{
    public void addUpgrade(Ability a);
    public void removeUpgrade(int index);

	public void addShipListener(ShipListener l);
	//public void removeShipListener(ShipListener l);
	public void addWeaponListener(WeaponListener l);
	public void removeWeaponListener(WeaponListener l);
}
