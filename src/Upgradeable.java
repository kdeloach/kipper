// All Upgradeable Objects should have a list of listeners 
public interface Upgradeable {
	public void addListener(Listener l);
	
	public void addShipListener(ShipListener l);
	public void addWeaponListener(WeaponListener l);
}