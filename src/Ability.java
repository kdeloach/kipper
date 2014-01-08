import java.awt.Color;
import java.awt.Graphics;

public abstract class Ability
{
	final static String COOLDOWN = "COOLDOWN";
	final static String DAMAGE = "DAMAGE";
	final static String SPREAD = "SPREAD";
	final static String GUARD = "GUARD";
	final static String HEADING = "HEADING";

	public static void drawIcon(Graphics g, int x, int y, int width, int height) {}

	private int id;
	public void setId(int n) { id = n; }
	public int getId() { return id; }
}
