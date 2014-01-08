package kipper;

import kipper.weapons.*;

public interface Upgradeable
{
	public void addShipListener(ShipListener l);
	public void addWeaponListener(WeaponListener l);
}
