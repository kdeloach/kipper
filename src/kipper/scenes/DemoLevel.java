package kipper.scenes;

import java.awt.Graphics;
import kipper.*;
import kipper.ships.*;
import kipper.ships.controllers.*;

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
        player.setController(new MouseShipController());

        int x = OuterSpacePanel.WIDTH+50;
        int y = OuterSpacePanel.HEIGHT/2;

        Ship bot1 = new Kirby(x, y, osp);
        Ship bot2 = new Squid(x, y, osp);
        Ship bot3 = new Kirby(x, y, osp);
        Ship bot4 = new Squid(x, y, osp);
        Ship bot5 = new Kirby(x, y, osp);

        int x2 = osp.WIDTH - 175;
        int y2 = (osp.HEIGHT - 5 * 60) / 2 + 10;

        bot1.setDestination(x2,    y2+60*0);
        bot2.setDestination(x2+75, y2+60*1);
        bot3.setDestination(x2,    y2+60*2);
        bot4.setDestination(x2+75, y2+60*3);
        bot5.setDestination(x2,    y2+60*4);

        osp.addShip(bot1);
        osp.addShip(bot2);
        osp.addShip(bot3);
        osp.addShip(bot4);
        osp.addShip(bot5);
    }

    @Override
    public void destroyScene()
    {
        player.setController(null);
        osp.removeManyShips(osp.getNPCs());
        osp.removeAllProjectiles();
    }

    @Override
    public void draw(Graphics g)
    {
    }

    @Override
    public void handleInput()
    {
    }

    @Override
    public String getName()
    {
        return "demo";
    }
}
