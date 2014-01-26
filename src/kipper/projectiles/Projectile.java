package kipper.projectiles;

import kipper.Entity;
import kipper.weapons.Weapon;

public interface Projectile extends Entity
{
    public double getDamage();
    public boolean collidesWithOwner();
    public boolean collidesWithProjectiles();
    public Weapon getOwner();
}
