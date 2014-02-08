package kipper;

import java.io.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import javax.sound.sampled.*;
import kipper.ships.*;
import kipper.weapons.*;
import kipper.projectiles.*;

public class Util
{
    public final static Util instance = new Util();

    public Image loadImage(String filename)
    {
        return Toolkit.getDefaultToolkit().getImage(getClass().getResource(filename));
    }

    public void playSound(String filename)
    {
        try {
            InputStream file = getClass().getResourceAsStream(filename);
            InputStream bufferedIn = new BufferedInputStream(file);
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
        } catch (Exception ex) {}
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
        if (e instanceof MaskedEntity) {
            Rectangle2D.Double[] rz = ((MaskedEntity)e).getRectMask();
            for (int i = 0; i < rz.length; i++) {
                g.drawRect((int)rz[i].x,
                           (int)rz[i].y,
                           (int)rz[i].width,
                           (int)rz[i].height);
            }
        }
        if (e instanceof PolygonMaskedEntity) {
            Polygon p = ((PolygonMaskedEntity)e).getPolygonMask();
            p.translate((int)e.getX(), (int)e.getY());
            g.drawPolygon(p);
        }
    }

    public static boolean intersects(Entity a, Entity b)
    {
        if (a.equals(b) || !a.isAlive() || !b.isAlive()) {
            return false;
        }
        if (a.getTeam() == b.getTeam()) {
            return false;
        }
        if (a instanceof PolygonMaskedEntity) {
            return polygonIntersect((PolygonMaskedEntity)a, b);
        } else if (a instanceof MaskedEntity) {
            return rectMaskIntersect((MaskedEntity)a, b);
        }
        return boundingBoxIntersect(a, b);
    }

    public static boolean polygonIntersect(PolygonMaskedEntity a, Entity b)
    {
        Polygon aMask = a.getPolygonMask();
        aMask.translate((int)a.getX(), (int)a.getY());
        if (b instanceof PolygonMaskedEntity) {
            Polygon bMask = ((PolygonMaskedEntity)b).getPolygonMask();
            bMask.translate((int)b.getX(), (int)b.getY());
            Area area = new Area(aMask);
            area.intersect(new Area(bMask));
            return !area.isEmpty();
        } else if (b instanceof MaskedEntity) {
            Rectangle2D.Double[] rz = ((MaskedEntity)b).getRectMask();
            for (int i = 0; i < rz.length; i++) {
                if (aMask.intersects(rz[i])) {
                    return true;
                }
            }
            return false;
        }
        return aMask.intersects(new Rectangle2D.Double(b.getX(), b.getY(), b.getWidth(), b.getHeight()));
    }

    public static boolean boundingBoxIntersect(Entity a, Entity b)
    {
        Rectangle2D.Double r1 = new Rectangle2D.Double(a.getX(), a.getY(), a.getWidth(), a.getHeight());
        Rectangle2D.Double r2 = new Rectangle2D.Double(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        return r1.intersects(r2);
    }

    public static boolean rectMaskIntersect(MaskedEntity a, Entity b)
    {
        Rectangle2D.Double[] ar = a.getRectMask();

        Rectangle2D.Double[] br;
        if (b instanceof MaskedEntity) {
            br = ((MaskedEntity)b).getRectMask();
        } else {
            br = new Rectangle2D.Double[] {
                new Rectangle2D.Double(b.getX(), b.getY(), b.getWidth(), b.getHeight())
            };
        }

        for (int i = 0; i < ar.length; i++) {
            for (int k = 0; k < br.length; k++) {
                if (ar[i].intersects(br[k])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static double getAspectRatio(OuterSpacePanel osp)
    {
        return Math.min((double)osp.getWidth() / osp.WIDTH, (double)osp.getHeight() / osp.HEIGHT);
    }

    public static Point boxOffset(OuterSpacePanel osp, double ratio)
    {
        return new Point(
            (int)((osp.getWidth() - (double)osp.WIDTH * ratio) / 2.0),
            (int)((osp.getHeight() - (double)osp.HEIGHT * ratio) / 2.0));
    }

    public static Point scalePoint(Point p)
    {
        double ratio = Util.getAspectRatio(Global.game);
        Point offset = Util.boxOffset(Global.game, ratio);
        return new Point(
            (int)((p.x - offset.x) * 1 / ratio),
            (int)((p.y - offset.y) * 1 / ratio));
    }
}
