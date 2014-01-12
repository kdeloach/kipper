package kipper.weapons;

import kipper.Entity;

public interface Projectile extends Entity
{
    public double getDamage();
    public boolean collidesWithOwner();
}
