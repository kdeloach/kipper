package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// By default, projectile direction is same as orientation of the firing ship (0 or PI)
// but with this upgrade projectiles can travel in any direction.
public class RotateUpgrade extends Upgrade
{
    public RotateUpgrade()
    {
    }

    public double getValue(Upgradable sender, String name, double value)
    {
        Weapon w = (Weapon)sender;
        if (name == Upgrade.HEADING) {
            return heading(w.ship());
        }
        return value;
    }

    private double heading(Ship ship)
    {
        double x1 = ship.getWeapon().getX();
        double y1 = ship.getWeapon().getY();
        double x2 = ship.mousePressed.x;
        double y2 = ship.mousePressed.y;
        return Math.atan2(y2 - y1, x2 - x1);
    }

    @Override public String getTitle() { return "Rotate"; }
    @Override public Color getColor() { return Color.YELLOW; }
}
