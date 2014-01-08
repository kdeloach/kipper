public interface ShipListener extends Listener {
	
	public void shipMoved(Ship s);
	public void shipStopped(Ship s);
	
	public double attributeCalled(String name, double oldvalue);
}