package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// By default, projectile direction is same as orientation of the firing ship (0 or PI)
// but with this upgrade projectiles can travel in any direction.
public class RotateAbility extends Ability
{
    public RotateAbility()
    {
    }

    public double getValue(Upgradable sender, String name, double value)
    {
        Weapon w = (Weapon)sender;
        if (name == Ability.HEADING) {
            return heading(w.ship());
        }
        return value;
    }

    private double heading(Ship ship)
    {
        // TODO: Replace x1 and y1 with weapon endpoint (initial bullet position)
        // Note: This is the hard-coded position of the Triangle Man weapon
        double x1 = ship.getX();
        double y1 = ship.getY() + ship.getHeight() / 2;
        double x2 = ship.mousePressed.x;
        double y2 = ship.mousePressed.y;
        return Math.atan2(y2 - y1, x2 - x1);
    }

    @Override public String getTitle() { return "Rotate"; }
    @Override public Color getColor() { return Color.YELLOW; }
}
