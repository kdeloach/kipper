package kipper;

public class Random {
	// inclusive lo, inclusive hi
	static int range(int lo, int hi){
		return (int)(Math.random()*(hi-lo+1)+lo);
	}
}