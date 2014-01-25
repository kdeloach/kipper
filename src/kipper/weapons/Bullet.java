package kipper.weapons;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;
import kipper.upgrades.*;

public class Bullet implements Entity, Projectile
{
    private int life;
    private double x, y, theta, damage;
    protected Weapon weapon;

    public Bullet(double x, double y, double theta, double dmg, Weapon w)
    {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.weapon = w;

        setLocation(x, y);

        life = 1;
        damage = dmg;

        weapon.ship().panel().addProjectile(this);
    }

    @Override
    public void die()
    {
        double px = getX() + getWidth() / 2;
        double py = getY() + getHeight() / 2;
        ParticleEmitter pe = new ParticleEmitter(px, py, new SampleConfigImpl());
        weapon.ship().panel().addEmitter(pe);
    }

    public void move()
    {
        setLocation(getX() + getSpeed() * Math.cos(getTheta()),
                    getY() + getSpeed() * Math.sin(getTheta()));
    }

    @Override
    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update()
    {
        move();
    }

    @Override
    public void collide(Entity e)
    {
        e.hit(getDamage());
        hit(getLife());
    }

    @Override
    public void draw(Graphics g)
    {
        g.setColor(getColor());
        g.fillOval((int)getX(), (int)getY(), getWidth(), getHeight());
    }

    public Color getColor() { return Color.YELLOW; }
    public double getTheta() { return theta; }
    public double getSpeed() { return weapon.getValue(Ability.SPEED, Const.BULLET_SPEED); }

    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public int getWidth() { return (int)(6 * weapon.getSizeBonus()); }
    @Override public int getHeight() { return (int)(6 * weapon.getSizeBonus()); }
    @Override public int getLife() { return life; }
    @Override public int getTeam() { return weapon.ship().getTeam(); }
    @Override public double getDamage() { return damage; }
    @Override public boolean collidesWithOwner() { return false; }
    @Override public boolean collidesWithProjectiles() { return false; }
    @Override public Weapon getOwner() { return weapon; }

    @Override
    public boolean isAlive()
    {
        return life > 0 && weapon.ship().panel().contains(getX(), getY());
    }

    @Override
    public void hit(double damage)
    {
        if (isAlive()) {
            life -= damage;
            if (life <= 0) {
                die();
            }
        }
    }
}
