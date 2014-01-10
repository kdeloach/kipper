package kipper;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;
import kipper.ships.*;
import kipper.weapons.*;

public class BottomPanel extends JPanel
{
    Ship player;

    Font smallFont = new Font("Lucida Console", Font.PLAIN, 11);

    public BottomPanel()
    {
        setSize(getMinumumSize());
    }

    public void update(Ship player)
    {
        this.player = player;
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(smallFont);

        if (player != null && player.isAlive()) {
            // text
            g.setColor(Color.GRAY);
            g.drawString(player.getName(), 3, 9);
            g.drawString("life", 3, 21);
            g.drawString("exp", 3, 29);

            // bars
            drawBar(g, 40, 13, player.percentHealth(), Color.GRAY);
            drawBar(g, 40, 23, 1, Color.LIGHT_GRAY);

            g.setColor(Color.DARK_GRAY);
            g.drawLine(124, 12, 124, getHeight() - 4);

            // cooldowns
            g.setColor(Color.GRAY);
            drawCooldown(g, 135, 11, player.getWeapon(0));
            drawCooldown(g, 135 + (20 + 3) * 1, 11, player.getWeapon(1));
            drawCooldown(g, 135 + (20 + 3) * 2, 11, player.getWeapon(2));
            drawCooldown(g, 135 + (20 + 3) * 3, 11, player.getWeapon(3));
            drawCooldown(g, 135 + (20 + 3) * 4, 11, player.getWeapon(4));
        }

        Ship target = player.getTarget();
        if (target != null && target.isAlive()) {
            // offset
            int xoff = getWidth() - 250;

            // text
            g.setColor(Color.GRAY);
            g.drawString(target.getName(), 3 + xoff, 9);
            g.drawString("life", 3 + xoff, 21);
            g.drawString("exp", 3 + xoff, 29);

            // bars
            drawBar(g, 40 + xoff, 13, target.percentHealth(), Color.GRAY);
            drawBar(g, 40 + xoff, 23, 1, Color.LIGHT_GRAY);

            g.setColor(Color.DARK_GRAY);
            g.drawLine(124 + xoff, 12, 124 + xoff, getHeight() - 4);

            // cooldowns
            g.setColor(Color.GRAY);
            drawCooldown(g, 135 + xoff, 11, target.getWeapon(0));
            drawCooldown(g, 135 + xoff + (20 + 3) * 1, 11, target.getWeapon(1));
            drawCooldown(g, 135 + xoff + (20 + 3) * 2, 11, target.getWeapon(2));
            drawCooldown(g, 135 + xoff + (20 + 3) * 3, 11, target.getWeapon(3));
            drawCooldown(g, 135 + xoff + (20 + 3) * 4, 11, target.getWeapon(4));
        }
    }

    public void drawBar(Graphics g, int x, int y, double perc, Color c)
    {
        g.setColor(c);
        g.fillRect(x, y, (int)(75 * perc), 8);
    }

    public void drawCooldown(Graphics g, int x, int y, Weapon w)
    {
        if (w != null) {
            // if this weapon is selected
            if (w == w.ship().getWeapon()) {
                g.drawRect(x-1, y-1, 21, 21);
            }
            g.drawImage(w.getIcon(), x, y, this);
            g.fillArc(x, y, 20, 20, 90, (int)(-360 * w.percentCooled()));
        }
    }

    public Dimension getMinumumSize() { return getPreferredSize(); }
    public Dimension getPreferredSize() { return new Dimension(OuterSpacePanel.WIDTH, 33); }
}
