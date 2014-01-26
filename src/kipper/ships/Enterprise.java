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

public class Enterprise extends Ship
{
    Image img;
    OuterSpacePanel panel;

    public Enterprise(int x, int y, OuterSpacePanel c)
    {
        super(x, y, 1, c);
        panel = c;

        equipWeapon(new Shooter(getX(), getY(), getWidth(), getHeight() / 2 + 4, this));
        equipWeapon(new LaserGun(getX(), getY(), getWidth(), getHeight() / 2 + 4, this));
        equipWeapon(new MineLauncher(getX(), getY(), getWidth() + 15, getHeight() / 2 + 4, this));
        equipWeapon(new LightningGun(getX(), getY(), getWidth(), getHeight() / 2 + 6, this));
        selectWeapon(0);

        img = Util.instance.loadImage("/assets/enterprise.gif");
    }

    @Override
    public void die()
    {
        super.die();
        panel.respawnPlayer();
    }

    // Generated by mask tool
    @Override
    public Polygon getPolygonMask()
    {
        return new Polygon(new int[] {24, 37, 49, 26, 70, 87, 102, 133, 141, 94, 73, 0}, new int[] {9, 19, 38, 48, 47, 51, 51, 44, 32, 4, 0, 3}, 12);
    }

    @Override public int getWidth() { return 141; }
    @Override public int getHeight() { return 52; }
    @Override public String getName() { return "ENTERPRISE";}
    @Override public int getOrientation() { return Const.FACE_RIGHT; }
    @Override public int getSpeed() { return 30; }
    @Override public int getMaxHp() { return 50; }
    @Override public Image getImage() { return img; }
}
