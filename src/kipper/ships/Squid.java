package kipper.ships;

import java.awt.Image;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import kipper.*;
import kipper.effects.*;
import kipper.weapons.*;
import kipper.upgrades.*;

public class Squid extends Kirby
{
    Image img;

    public Squid(int x, int y, OuterSpacePanel c)
    {
        super(x, y, c);
        Weapon w1 = new MineLauncher(0, 0, this);
        w1.setLocation(-w1.getWidth(), getHeight() / 2);
        w1.addUpgrade(new RotateUpgrade());
        equipWeapon(w1);
        selectWeapon(1);
        img = Util.instance.loadImage("/assets/images/squid.png");
    }

    // Generated by mask tool
    @Override
    public Polygon getPolygonMask()
    {
        return new Polygon(new int[] {49, 36, 11, 2, 0, 5, 38}, new int[] {31, 36, 34, 27, 17, 10, 0}, 7);
    }

    @Override public int getWidth() { return 49; }
    @Override public int getHeight() { return 36; }
    @Override public String getName() { return "SQUID";}
    @Override public int getOrientation() { return Const.FACE_LEFT; }
    @Override public int getSpeed() { return 20; }
    @Override public int getMaxLife() { return 50; }
    @Override public Image getImage() { return img; }
}
