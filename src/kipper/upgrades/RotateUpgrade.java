package kipper.upgrades;

import java.awt.*;
import java.awt.geom.*;
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
        Point2D.Double origin = ship.getWeapon().projectileOrigin();
        double x1 = origin.x;
        double y1 = origin.y;
        double x2 = ship.getMousePressedLocation().x;
        double y2 = ship.getMousePressedLocation().y;
        return Math.atan2(y2 - y1, x2 - x1);
    }

    @Override public String getTitle() { return "Rotate"; }
    @Override public Color getColor() { return Color.YELLOW; }
}
