package kipper.upgrades;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

public class AbilityIconDrawer
{
    Color color;
    String title;

    Font font = new Font("Arial", Font.PLAIN, 18);

    public AbilityIconDrawer(String title, Color color)
    {
        this.title = title;
        this.color = color;
    }

	public void drawIcon(Graphics g, int x, int y, int width, int height)
    {
		g.setFont(font);
		g.setColor(color);
		g.fillRoundRect(x, y, width, height, 15, 15);
		g.setColor(Color.BLACK);
        int titleWidth = g.getFontMetrics(font).stringWidth(title);
		g.drawString(title, x + width / 2 - titleWidth / 2, y + height / 2 + 5);
	}
}
