package kipper;

import kipper.ships.*;

public class DemoLevel extends Scene {

    Ship player;
    OuterSpacePanel osp;

	public DemoLevel(OuterSpacePanel osp){
        this.osp = osp;
        player = osp.getPlayer();
	}

    public void createScene() {
        player.gainControl();
        osp.registerShip(player);
		osp.registerShip(new TriangleMan(550,59,osp));
		osp.registerShip(new TriangleMan(650,160,osp));
		osp.registerShip(new TriangleMan(550,261,osp));
    }

    public void destroyScene() {
		// problem with clear() is that it doesn't kill the thread runnin these things
		// they must be properly disposed of
		osp.killNPCs();
        osp.clearProjectiles();
		//osp.explosionList.clear();
    }

    public String name() {
        return "demo";
    }
}