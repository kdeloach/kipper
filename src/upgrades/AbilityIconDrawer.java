package kipper.upgrades;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

public class AbilityIconDrawer
{
    Color color;
    String title;

    public AbilityIconDrawer(String title, Color color)
    {
        this.title = title;
        this.color = color;
    }

	public void drawIcon(Graphics g, int x, int y, int width, int height)
    {
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(color);
		g.fillRoundRect(x, y, width, height, 15, 15);
		g.setColor(Color.BLACK);
		g.drawString(title, x + width / 2 - 30, y + height / 2 + 5);
	}
}
