package kipper;

import java.awt.*;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Iterator;
import kipper.ships.*;
import kipper.weapons.*;

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

    public static String join(Iterable<?> sb, String delim)
    {
        StringBuilder result = new StringBuilder();
        Iterator<?> iter = sb.iterator();
        while (iter.hasNext()) {
            result.append(iter.next().toString());
            if (iter.hasNext()) {
                result.append(delim);
            }
        }
        return result.toString();
    }

    public static void drawMask(Graphics g, Entity e)
    {
        g.setColor(Color.GREEN);
        if (e instanceof PolygonMaskedEntity) {
            Polygon p = ((PolygonMaskedEntity)e).getPolygonMask();
            p.translate((int)e.getX(), (int)e.getY());
            g.drawPolygon(p);
        }
        if (e instanceof EllipseMaskedEntity) {
            Ellipse2D.Double[] ez = ((EllipseMaskedEntity)e).getEllipseMask();
            for (int i = 0; i < ez.length; i++) {
                g.drawOval((int)ez[i].getX(),
                           (int)ez[i].getY(),
                           (int)ez[i].getWidth(),
                           (int)ez[i].getHeight());
            }
        }
    }

    public static boolean intersects(Entity a, Entity b)
    {
        if (a instanceof Ship) {
            return shipIntersects((Ship)a, b);
        } else if (a instanceof Bolt) {
            return boltIntersects((Bolt)a, b);
        } else if (a instanceof Bullet) {
            return bulletIntersects((Bullet)a, b);
        }
        Rectangle2D.Double r1 = new Rectangle2D.Double(a.getX(), a.getY(), a.getWidth(), a.getHeight());
        Rectangle2D.Double r2 = new Rectangle2D.Double(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        return r1.intersects(r2);
    }

	public static boolean shipIntersects(Ship s, Entity e)
    {
        if (!s.isAlive()) {
            return false;
        }
        Polygon tmp = s.getPolygonMask();
        Polygon mask = new Polygon(tmp.xpoints, tmp.ypoints, tmp.npoints);
        mask.translate((int)s.getX(), (int)s.getY());
        if (e instanceof PolygonMaskedEntity) {
            tmp = ((PolygonMaskedEntity)e).getPolygonMask();
            Polygon otherMask = new Polygon(tmp.xpoints, tmp.ypoints, tmp.npoints);
            otherMask.translate((int)e.getX(), (int)e.getY());
            for (int i = 0; i < mask.npoints; i++) {
                if (otherMask.contains(mask.xpoints[i], mask.ypoints[i])) {
                    return true;
                }
            }
            return false;
        }
        return mask.intersects(e.getX(), e.getY(), e.getWidth(), e.getHeight());
	}

	public static boolean boltIntersects(Bolt b, Entity e)
    {
        if (!b.isAlive()) {
            return false;
        }
        if (e instanceof PolygonMaskedEntity) {
            Polygon tmp = ((PolygonMaskedEntity)e).getPolygonMask();
            Polygon mask = new Polygon(tmp.xpoints, tmp.ypoints, tmp.npoints);
            mask.translate((int)e.getX(), (int)e.getY());
            return mask.intersects((int)b.startX(), (int)b.startY(), b.getWidth(), b.getHeight())
                || mask.intersects((int)b.stopX(), (int)b.stopY(), b.getWidth(), b.getHeight());
        }
        Rectangle2D.Double boundingBox = new Rectangle2D.Double(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        return boundingBox.intersects(b.startX(), b.startY(), b.getWidth(), b.getHeight())
            || boundingBox.intersects(b.stopX(), b.stopY(), b.getWidth(), b.getHeight());
	}

	public static boolean bulletIntersects(Bullet b, Entity e)
    {
        if (!b.isAlive()) {
            return false;
        }
        if (e instanceof PolygonMaskedEntity) {
            Polygon tmp = ((PolygonMaskedEntity)e).getPolygonMask();
            Polygon mask = new Polygon(tmp.xpoints, tmp.ypoints, tmp.npoints);
            mask.translate((int)e.getX(), (int)e.getY());
            return mask.intersects(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }
        Rectangle2D.Double boundingBox = new Rectangle2D.Double(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        return boundingBox.intersects(b.getX(), b.getY(), b.getWidth(), b.getHeight());
	}
}
