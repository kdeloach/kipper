package kipper.projectiles;

import kipper.Entity;
import kipper.weapons.Weapon;

public interface Projectile extends Entity
{
    public double getTheta();
    public void setTheta(double theta);
    public double getDamage();
    public boolean collidesWithOwner();
    public boolean collidesWithProjectiles();
    public Weapon getOwner();
}
