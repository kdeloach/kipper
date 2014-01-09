package kipper;

import java.awt.*;
import java.awt.event.*;
import kipper.ships.*;
import kipper.weapons.*;
import kipper.upgrades.*;

public class ShipUpgradeScreen extends Scene {

	Ship player;
	OuterSpacePanel osp;

	int upgrades = 0;
	int slots = 1;

	Rectangle[][] hotspots = new Rectangle[2][];

    private Font f = new Font("Arial", Font.PLAIN, 16);

    private Ship nullShip = new NullShip();
    // Needed because we may have to render information about these abilities before an instance
    // has been created and attached to the player and dealing with static objects in Java is a nightmare.
    private Ability[] upgradeInstances = new Ability[Const.UPGRADES_QT];

	public ShipUpgradeScreen(OuterSpacePanel osp)
    {
		this.osp = osp;
		player = osp.getPlayer();

		hotspots[slots] = new Rectangle[player.getSlotsAmt()];
		for (int i = 0; i < hotspots[slots].length; i++) {
			hotspots[slots][i] = new Rectangle(player.width + 50 + 100 * i, 20, 90, 90);
        }

		// THERE  ARE ONLY 5 UPGRADES ATM
		int w = 140, h = 50, p = 5;
		hotspots[upgrades] = new Rectangle[Const.UPGRADES_QT];
		for (int i = 0, y = 150, x = 0; i < hotspots[upgrades].length; i++, x++) {
			if (player.width + 50 + (w + p) * x + w >= osp.getWidth()) {
				x = 0;
                y += h + p;
			}
			hotspots[upgrades][i] = new Rectangle(player.width + 50 + (w + p) * x, y, w, h);
            upgradeInstances[i] = Ability.createInstance(i, nullShip);
		}
	}

    public void createScene()
    {
		player.releaseControl();
		player.move(player.getWidth() / 2 + 10, player.getHeight() / 2 + 20);
		osp.addMouseListener(this);
    }

    public void destroyScene()
    {
		osp.removeMouseListener(this);
    }

	public void paint(Graphics g)
    {
		// draw upgrade slots
		for (int i = 0; i  < hotspots[slots].length; i++) {
            Rectangle slot = hotspots[slots][i];
            g.setFont(f);
            g.setColor(Color.GRAY);
			g.fillRoundRect(slot.x, slot.y, slot.width, slot.height, 15, 15);
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("Slot " + (i + 1), slot.x + 8 * 3 + 2, slot.y + slot.height / 2);
			if (player.upgrades.get(i) != null) {
				Ability a = player.upgrades.get(i);
                AbilityIconDrawer drawer = a.getIconDrawer();
                drawer.drawIcon(g, slot.x, slot.y, slot.width, slot.height);
			}
		}

        // draw available upgrades
		for (int i = 0; i < hotspots[upgrades].length; i++) {
			Rectangle r = hotspots[upgrades][i];
            AbilityIconDrawer drawer = upgradeInstances[i].getIconDrawer();
            drawer.drawIcon(g, r.x, r.y, r.width, r.height);
		}
	}

	public void mousePressed(MouseEvent e)
    {
		// if an upgrade is selected
		for (int i = 0; i < hotspots[upgrades].length; i++) {
			if (hotspots[upgrades][i].contains(e.getPoint())) {
                player.addUpgrade(Ability.createInstance(i, player));
				return;
			}
		}

		// if a slot is selected
		for (int i = 0; i < hotspots[slots].length; i++) {
			if (hotspots[slots][i].contains(e.getPoint())) {
				player.removeUpgrade(i);
				return;
			}
		}
	}

    public String name()
    {
        return "upgrade";
    }
}