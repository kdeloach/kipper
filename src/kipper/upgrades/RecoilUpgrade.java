package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// Send a ship flying back
public class RecoilUpgrade extends Upgrade
{
    // distance to send ship backwards
    int distance = 100;

    public RecoilUpgrade()
    {
    }

    public void weaponFired(Weapon w)
    {
        Ship s = w.ship();

        // if its a bot, make sure it isn't recoiling offscreen
        if (!s.isUnderControl() && s.getX() + s.getWidth() >= OuterSpacePanel.WIDTH) {
            return;
        }

        s.setDestination(s.getX() - distance * Math.cos(w.getTheta()),
                         s.getY() - distance * Math.sin(w.getTheta()));

        s.freezeMovement(100);
    }

    @Override public String getTitle() { return "Recoil"; }
    @Override public Color getColor() { return Color.MAGENTA; }
}
