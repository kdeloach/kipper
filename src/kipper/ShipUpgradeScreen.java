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

    Rectangle[] emptySlotRects = new Rectangle[12]; // Arbitrary
    Rectangle[] availableUpgradeRects = new Rectangle[Const.UPGRADES_QT];

    private Font f = new Font("Arial", Font.PLAIN, 16);

    // Needed because we may have to render information about these abilities before an instance
    // has been created and attached to the player and dealing with static objects in Java is a nightmare.
    // Upgrade instance at index N corresponds to rectangle N in availableUpgradeRects array.
    private Ability[] upgradeInstances = new Ability[Const.UPGRADES_QT];

    public ShipUpgradeScreen(OuterSpacePanel osp)
    {
        this.osp = osp;
        player = osp.getPlayer();

        for (int i = 0; i < emptySlotRects.length; i++) {
            int x = player.getWidth() + 50 + 100 * (i % 6);
            int y = 20 + 55 * (int)Math.floor(i / 6);
            emptySlotRects[i] = new Rectangle(x, y, 90, 45);
        }

        int w = 140, h = 50, p = 5;
        for (int i = 0, y = 150, x = 0; i < availableUpgradeRects.length; i++, x++) {
            if (player.getWidth() + 50 + (w + p) * x + w >= osp.getWidth()) {
                x = 0;
                y += h + p;
            }
            upgradeInstances[i] = Ability.createInstance(i);
            availableUpgradeRects[i] = new Rectangle(player.getWidth() + 50 + (w + p) * x, y, w, h);
        }
    }

    public void createScene()
    {
        player.setDestination(player.getWidth() / 2 + 10,
                              player.getHeight() / 2 + 20);
        osp.addMouseListener(this);
    }

    public void destroyScene()
    {
        osp.removeMouseListener(this);
    }

    public void paint(Graphics g)
    {
        // draw upgrade slots
        for (int i = 0; i  < player.getWeapon().amountSlots(); i++) {
            Rectangle slot = emptySlotRects[i];
            g.setFont(f);
            g.setColor(Color.GRAY);
            g.fillRoundRect(slot.x, slot.y, slot.width, slot.height, 15, 15);
            g.setColor(Color.LIGHT_GRAY);
            String title = "Slot " + (i + 1);
            int titleWidth = g.getFontMetrics(f).stringWidth(title);
            g.drawString(title, slot.x + slot.width / 2 - titleWidth / 2, slot.y + slot.height / 2);
            Ability a = player.getWeapon().upgradeAt(i);
            if (a != null) {
                AbilityIconDrawer drawer = a.getIconDrawer();
                drawer.drawIcon(g, slot.x, slot.y, slot.width, slot.height);
            }
        }
        // draw available upgrades
        for (int i = 0; i < availableUpgradeRects.length; i++) {
            Rectangle r = availableUpgradeRects[i];
            AbilityIconDrawer drawer = upgradeInstances[i].getIconDrawer();
            drawer.drawIcon(g, r.x, r.y, r.width, r.height);
        }
    }

    public void mousePressed(MouseEvent e)
    {
        // if a slot is selected
        for (int i = 0; i < emptySlotRects.length; i++) {
            if (emptySlotRects[i].contains(e.getPoint())) {
                player.getWeapon().removeUpgrade(i);
                return;
            }
        }
        // if an upgrade is selected
        for (int i = 0; i < availableUpgradeRects.length; i++) {
            if (availableUpgradeRects[i].contains(e.getPoint())) {
                Weapon w = player.getWeapon();
                w.addUpgrade(Ability.createInstance(i));
                return;
            }
        }
    }

    @Override public String name() { return "upgrade"; }
}
