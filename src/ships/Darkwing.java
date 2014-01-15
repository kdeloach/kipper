package kipper.ships;

import java.awt.Image;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import kipper.*;
import kipper.effects.*;
import kipper.weapons.*;

public class Darkwing extends Enterprise
{
	Image img;
    OuterSpacePanel panel;

	public Darkwing(int x, int y, OuterSpacePanel c)
    {
		super(x, y, c);
		img = Util.instance.loadImage("/assets/darkwing.png");
	}

	// ** generated with MaskMachine ** //
    @Override
	public Polygon getDefaultMask()
    {
		return new Polygon(
            new int[]{0,13,29,40,47,52,38,25,5},
            new int[]{0,0,6,8,14,30,31,38,41},
            9
		);
	}

	@Override public int getWidth() { return 53; }
	@Override public int getHeight() { return 42; }
	@Override public String getName() { return "DARKWING";}
	@Override public int defaultTeam() { return Const.PLAYER; }
    @Override public int getDefaultOrientation() { return Const.LEFT_TO_RIGHT; }
	@Override public int getDefaultSpeed() { return 30; }
	@Override public int defaultMaxHp() { return 50; }
    @Override public Image getImage() { return img; }
}
