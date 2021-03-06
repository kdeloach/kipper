package kipper.weapons;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;
import kipper.projectiles.*;

public class LightningGun extends Weapon
{
    Image icon;

    public LightningGun(double x, double y, Ship c)
    {
        super(x, y, c);
        icon = Util.instance.loadImage("/assets/images/lightning_icon.gif");
        addUpgrade(new CooldownUpgrade());
        addUpgrade(new CooldownUpgrade());
        addUpgrade(new CooldownUpgrade());
        addUpgrade(new CooldownUpgrade());
    }

    @Override public int getWidth() { return 45; }
    @Override public int getHeight() { return 15; }
    @Override public Image getIcon() { return icon; }
    @Override public int getDefaultDamage() { return 1; }
    @Override public int getDefaultCooldown() { return 200; }

    public int getBoltBranches() { return (int)(3 + getSizeBonus()); }

    @Override
    public Projectile createProjectile()
    {
        return new LightningBolt(getDamage(), this);
    }

    @Override
    public String getSoundFile()
    {
        return "/assets/sounds/LightningGun.wav";
    }
}
