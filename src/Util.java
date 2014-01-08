package kipper;

import java.awt.*;
import java.io.File;

public class Util
{
    public final static Util instance = new Util();

    public Image loadImage(String filename)
    {
        String basePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        return Toolkit.getDefaultToolkit().createImage(new File(basePath, filename).getPath());
    }

	// inclusive lo, inclusive hi
	public static int randRange(int lo, int hi)
    {
		return (int)(Math.random() * (hi - lo + 1) + lo);
	}
}