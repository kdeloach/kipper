package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;
import kipper.projectiles.*;

public class MineLauncher extends Weapon
{
    Image icon;

    public MineLauncher(double x, double y, Ship c)
    {
        super(x, y, c);
        icon = Util.instance.loadImage("/assets/images/mines_icon.gif");
    }

    @Override public int getWidth() { return 15; }
    @Override public int getHeight() { return 20; }
    @Override public Image getIcon() { return icon; }
    @Override public int getDefaultDamage() { return 15; }
    @Override public int getDefaultCooldown() { return 200; }

    @Override
    public Projectile createProjectile()
    {
        return new SpaceMine(getDamage(), this);
    }

    @Override
    public String getSoundFile()
    {
        return "/assets/sounds/MineLauncher.wav";
    }
}
