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
import kipper.ships.controllers.*;

public class Kirby extends Ship
{
    Image img;

    public Kirby(int x, int y, OuterSpacePanel c)
    {
        super(x, y, Const.TEAM_NPC, c);
        img = Util.instance.loadImage("/assets/images/kirby.png");
        Weapon w1 = new Shooter(0, 0, this);
        w1.setLocation(-w1.getWidth(), -w1.getHeight() / 2);
        w1.addUpgrade(new RotateUpgrade());
        equipWeapon(w1);
        selectWeapon(0);
        setController(new NpcShipController());
    }

    @Override
    protected void deathExplosion()
    {
        double px = getX() + getWidth() / 2;
        double py = getY() + getHeight() / 2;
        ParticleEmitter pe = new ParticleEmitter(px, py, new KirbyExplosion());
        osp.addEmitter(pe);
    }

    // Generated by mask tool
    @Override
    public Polygon getPolygonMask()
    {
        return new Polygon(new int[] {39, 15, 0, 0, 15, 40}, new int[] {41, 34, 25, 19, 8, 0}, 6);
    }

    @Override public int getWidth() { return 41; }
    @Override public int getHeight() { return 41; }
    @Override public String getName() { return "KIRBY";}
    @Override public int getOrientation() { return Const.FACE_LEFT; }
    @Override public int getSpeed() { return 20; }
    @Override public int getMaxLife() { return 50; }
    @Override public Image getImage() { return img; }

    @Override
    public Point getWeaponMountPoint()
    {
        return new Point(0, getHeight() / 2);
    }
}
