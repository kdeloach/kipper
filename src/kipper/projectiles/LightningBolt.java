package kipper.projectiles;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;
import kipper.effects.*;
import kipper.effects.transitions.*;

public class LightningBolt extends LaserBeam
{
    private int length;
    private int branches;
    private int thickness;
    private EasingFunc easer = new EaseInQuad();
    private LinkedList<LightningBolt> children;

    private boolean isRootNode = false;
    private Image cacheImage;
    private Point2D.Double cacheImageOffset;

    public LightningBolt(double dmg, Weapon w)
    {
        super(dmg, w);
        init(getDefaultBranches(), getDefaultLength(), getDefaultBranches());
        this.isRootNode = true;
        setTheta(w.getTheta() + randomAngle());
        splinter();
        Rectangle2D.Double boundingBox = getBoundingBox();
        cacheImage = createImage(boundingBox);
        cacheImageOffset = new Point2D.Double(boundingBox.x, boundingBox.y);
    }

    protected LightningBolt(int branches, int length, int thickness, double dmg, Weapon w)
    {
        super(dmg, w);
        init(branches, length, thickness);
    }

    protected void init(int branches, int length, int thickness)
    {
        this.branches = branches;
        this.length = length;
        this.thickness = thickness;
        this.children = new LinkedList<LightningBolt>();
    }

    private void splinter()
    {
        LightningBolt prevBolt = this;
        for (int thickness = branches - 1; thickness > 0; thickness--) {
            int childBranches = (int)Math.floor(Math.random() * branches * 3 / 4);
            LightningBolt bolt = new LightningBolt(childBranches, length, thickness, getDamage(), weapon);
            bolt.setLocation(prevBolt.stopX(), prevBolt.stopY());
            bolt.setTheta(prevBolt.getTheta() + randomAngle());
            bolt.splinter();
            children.add(bolt);
            prevBolt = bolt;
        }
    }

    private double randomAngle()
    {
        return Math.toRadians(Math.random() * 60 - 30);
    }

    @Override
    public void move()
    {
        if (isRootNode) {
            Point2D.Double origin = weapon.projectileOrigin();
            setLocation(origin.x, origin.y);
        }
    }

    @Override
    public void collide(Entity e)
    {
        e.hit(getDamage());
    }

    @Override
    public void deathExplosion()
    {
    }

    @Override
    public void setLocation(double x, double y)
    {
        super.setLocation(x, y);
        LightningBolt prevBolt = this;
        for (LightningBolt bolt : children) {
            bolt.setLocation(prevBolt.stopX(), prevBolt.stopY());
            prevBolt = bolt;
        }
    }

    @Override
    public void update()
    {
        super.update();
        for (LightningBolt bolt : children) {
            bolt.update();
        }
        hit(1);
    }

    @Override
    public void draw(Graphics g)
    {
        if (cacheImage != null) {
            int x = (int)(getX() + cacheImageOffset.x);
            int y = (int)(getY() + cacheImageOffset.y);
            float alpha = (float)easer.call(getLife(), 0, 1, getDefaultLife());
            ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.drawImage(cacheImage, x, y, null);
            ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            return;
        }
        g.setColor(Color.WHITE);
        if (getThickness() <= 1) {
            g.drawLine((int)getX(), (int)getY(), (int)stopX(), (int)stopY());
        } else {
            Util.drawThickLine(g, getX(), getY(), stopX(), stopY(), getThickness());
        }
        for (LightningBolt bolt : children) {
            bolt.draw(g);
        }
        //drawRootBranches(g);
        //drawMask(g);
    }

    public Image createImage(Rectangle2D.Double boundingBox)
    {
        int width = (int)boundingBox.width + 15;
        int height = (int)boundingBox.height + 15;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.translate(-(int)boundingBox.x, -(int)boundingBox.y);
        draw(g);
        return img;
    }

    protected void drawMask(Graphics g)
    {
        if (isRootNode) {
            Util.drawMask(g, this);
        }
    }

    protected void drawRootBranches(Graphics g)
    {
        if (isRootNode) {
            g.setColor(Color.GREEN);
            for (LightningBolt bolt : children) {
                g.drawOval((int)bolt.getX(), (int)bolt.getY(), 2, 2);
            }
        }
    }

    @Override
    public int getDefaultLife()
    {
        return Util.msToTicks(getDefaultLifespanMs());
    }

    protected int getDefaultLifespanMs() { return 1000; }
    protected int getDefaultLength() { return 20; }
    protected int getDefaultBranches() { return ((LightningGun)weapon).getBoltBranches(); }

    public int getThickness() { return thickness; }
    @Override public int getWidth() { return getThickness(); }
    public LinkedList<LightningBolt> getChildren() { return children; }

    @Override public double getDamage() { return super.getDamage() * getLife() / getDefaultLife(); }
    @Override public int getLength() { return length; }

    @Override
    public Rectangle2D.Double[] getRectMask()
    {
        LinkedList<Rectangle2D.Double> result = new LinkedList<Rectangle2D.Double>();
        // XXX Without this, the root node won't appear in the cached image
        if (isRootNode) {
            result.add(new Rectangle2D.Double(getX() - getWidth() / 2,
                                              getY() - getHeight() / 2,
                                              getWidth(),
                                              getHeight()));
        }
        result.add(new Rectangle2D.Double(stopX() - getWidth() / 2,
                                          stopY() - getHeight() / 2,
                                          getWidth(),
                                          getHeight()));
        for (LightningBolt bolt : children) {
            Rectangle2D.Double[] childMask = bolt.getRectMask();
            for (int k = 0; k < childMask.length; k++) {
                result.add(childMask[k]);
            }
        }
        Rectangle2D.Double[] arrResult = new Rectangle2D.Double[result.size()];
        return result.toArray(arrResult);
    }

    public Rectangle2D.Double getBoundingBox()
    {
        Rectangle2D.Double[] mask = getRectMask();
        double minWidth = OuterSpacePanel.WIDTH;
        double maxWidth = getX();
        double minHeight = OuterSpacePanel.HEIGHT;
        double maxHeight = getY();
        for (Rectangle2D.Double rect : mask) {
            minWidth = Math.min(minWidth, rect.x);
            maxWidth = Math.max(maxWidth, rect.x);
            minHeight = Math.min(minHeight, rect.y);
            maxHeight = Math.max(maxHeight, rect.y);
        }
        return new Rectangle2D.Double(minWidth, minHeight, maxWidth - minWidth, maxHeight - minHeight);
    }
}
