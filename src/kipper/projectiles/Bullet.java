package kipper.projectiles;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;
import kipper.upgrades.*;
import kipper.weapons.*;

public class Bullet implements Entity, Projectile
{
    private int life;
    private double x;
    private double y;
    private double theta;
    private double damage;
    protected Weapon weapon;

    public Bullet(double dmg, Weapon w)
    {
        this.damage = dmg;
        this.weapon = w;
        this.life = getDefaultLife();
        setTheta(w.getTheta());
    }

    @Override
    public void die()
    {
        life = 0;
        deathExplosion();
    }

    public void deathExplosion()
    {
        double px = getX() + getWidth() / 2;
        double py = getY() + getHeight() / 2;
        ParticleEmitter pe = new ParticleEmitter(px, py, new SampleConfigImpl());
        weapon.ship().panel().addEmitter(pe);
    }

    public void playSound()
    {
        if (getSoundFile().length() > 0) {
            Util.instance.playSound(getSoundFile());
        }
    }

    public String getSoundFile()
    {
        switch (getOwner().ship().getTeam()) {
            case Const.TEAM_PLAYER:
                return "/assets/sounds/Hurt2.wav";
            case Const.TEAM_NPC:
                return "/assets/sounds/Hurt1.wav";
        }
        return "";
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
    public double getTheta()
    {
        return this.theta;
    }

    @Override
    public void setTheta(double theta)
    {
        this.theta = theta;
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
        if (e instanceof Projectile) {
            hit(((Projectile)e).getDamage());
        } else {
            hit(getLife());
        }
        playSound();
    }

    @Override
    public void draw(Graphics g)
    {
        g.setColor(getColor());
        g.fillOval((int)getX(), (int)getY(), getWidth(), getHeight());
    }

    public Color getColor() { return Color.YELLOW; }
    public double getSpeed() { return weapon.getValue(Upgrade.SPEED, Const.BULLET_SPEED); }

    public int getDefaultLife() { return 1; }

    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public int getWidth() { return (int)(6 * weapon.getSizeBonus()); }
    @Override public int getHeight() { return (int)(6 * weapon.getSizeBonus()); }
    @Override public int getLife() { return life; }
    @Override public int getTeam() { return weapon.ship().getTeam(); }
    @Override public double getDamage() { return damage; }
    @Override public boolean collidesWithOwner() { return false; }
    @Override public boolean collidesWithProjectiles() { return weapon.collidesWithProjectiles(); }
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
