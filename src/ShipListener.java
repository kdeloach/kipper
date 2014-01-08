package kipper;


public class ShipListener
{
	public void shipMoved(Ship s) {}
	public void shipStopped(Ship s) {}

	public double attributeCalled(String name, double oldvalue) {
        return oldvalue;
    }
}
