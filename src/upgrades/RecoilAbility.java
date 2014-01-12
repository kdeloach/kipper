package kipper.upgrades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// Send a ship flying back
public class RecoilAbility extends Ability
{
	// distance to send ship backwards
	int distance = 100;

	public RecoilAbility()
    {
	}

	public void weaponFired(Weapon w)
    {
		Ship s = w.ship();

		// prevents player from moving ship while shooting and in-recoil
		// HAD to remove this because: theres a bug where you can Fire a recoil-capable weapon,
		//    then let go of the leftmousebutton and still be able to move&shoot with just the rightmousebutton
		//    PLUS you could switch weapons and keep the effect; I don't want inconsistent controls
		//    The DOWNSIDE is that players can "break" out of recoil by moving as soon as weapon is fired
		//s.leftmousedown=false;

		// if its a bot, make sure it isn't recoiling offscreen
		if (!s.underControl() && s.getX() + s.getWidth() >= OuterSpacePanel.WIDTH) {
			return;
        }

        s.setDestination(s.getX() - distance * Math.cos(w.heading()),
                         s.getY() - distance * Math.sin(w.heading()));

        s.freezeMovement(100);
	}

	@Override public String getTitle() { return "Recoil"; }
	@Override public Color getColor() { return Color.MAGENTA; }
}
