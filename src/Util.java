package kipper;

import java.awt.*;
import java.io.File;

public class Util
{
    public final static Util instance = new Util();

    public Image loadImage(String filename)
    {
        return Toolkit.getDefaultToolkit().getImage(getClass().getResource(filename));
    }

	// inclusive lo, inclusive hi
	public static int randRange(int lo, int hi)
    {
		return (int)(Math.random() * (hi - lo + 1) + lo);
	}
}