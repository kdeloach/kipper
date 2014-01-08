package kipper.upgrades;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Ability
{
	public static final String COOLDOWN = "COOLDOWN";
	public static final String DAMAGE = "DAMAGE";
	public static final String SPREAD = "SPREAD";
	public static final String GUARD = "GUARD";
	public static final String HEADING = "HEADING";

	public static void drawIcon(Graphics g, int x, int y, int width, int height) {}

	private int id;
	public void setId(int n) { id = n; }
	public int getId() { return id; }
}
