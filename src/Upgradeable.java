package kipper;

import kipper.ships.*;
import kipper.weapons.*;

public interface Upgradeable
{
	public void addShipListener(ShipListener l);
	public void addWeaponListener(WeaponListener l);
}
