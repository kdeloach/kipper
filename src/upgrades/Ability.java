package kipper.upgrades;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Ability
{
	public final static String COOLDOWN = "COOLDOWN";
	public final static String DAMAGE = "DAMAGE";
	public final static String SPREAD = "SPREAD";
	public final static String GUARD = "GUARD";
	public final static String HEADING = "HEADING";

	public static void drawIcon(Graphics g, int x, int y, int width, int height) {}

	private int id;
	public void setId(int n) { id = n; }
	public int getId() { return id; }
}
