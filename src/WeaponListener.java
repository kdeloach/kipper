public interface WeaponListener extends Listener {
	
	public void weaponFired(Weapon w);
	
	public void bulletFired(Bullet b);
	public void bulletHit(Bullet b);
	
	// usage ex: when getSpeed() is called this function gets called first with argument "speed" and is added to weapon speed/cooldown
	public double attributeCalled(String name, double oldvalue);
}