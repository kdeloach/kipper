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

    public static int msToTicks(int ms)
    {
        return (int)((long)ms / OuterSpacePanel.FPS);
    }

    // Note: Using Graphics2D to draw shape objects is SLOOOW
    // Source: http://www.rgagnon.com/javadetails/java-0260.html
    public static void drawThickLine(Graphics g, double x1, double y1, double x2, double y2, int thickness)
    {
        double dX = x2 - x1;
        double dY = y2 - y1;
        // line length
        double lineLength = Math.sqrt(dX * dX + dY * dY);

        double scale = ((double)thickness) / (2.0 * lineLength);

        // The x,y increments from an endpoint needed to create a rectangle...
        double ddx = -scale * dY;
        double ddy = scale * dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        int dx = (int)ddx;
        int dy = (int)ddy;

        // Now we can compute the corner points...
        int xPoints[] = new int[4];
        int yPoints[] = new int[4];

        xPoints[0] = (int)x1 + dx; yPoints[0] = (int)y1 + dy;
        xPoints[1] = (int)x1 - dx; yPoints[1] = (int)y1 - dy;
        xPoints[2] = (int)x2 - dx; yPoints[2] = (int)y2 - dy;
        xPoints[3] = (int)x2 + dx; yPoints[3] = (int)y2 + dy;

        g.fillPolygon(xPoints, yPoints, 4);
    }
}
