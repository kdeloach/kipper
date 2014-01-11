package kipper;

import kipper.ships.*;

public class DemoLevel extends Scene
{
    Ship player;
    OuterSpacePanel osp;

	public DemoLevel(OuterSpacePanel osp)
    {
        this.osp = osp;
        player = osp.getPlayer();
	}

    @Override
    public void createScene()
    {
        player.gainControl();
		osp.addShip(new TriangleMan(550, 59, osp));
		osp.addShip(new TriangleMan(650, 160, osp));
		osp.addShip(new TriangleMan(550, 261, osp));
    }

    @Override
    public void destroyScene()
    {
        player.releaseControl();
		osp.removeManyShips(osp.getNPCs());
        osp.removeAllProjectiles();
    }

    @Override public String name() { return "demo"; }
}
