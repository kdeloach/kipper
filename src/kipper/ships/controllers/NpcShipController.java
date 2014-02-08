package kipper.ships.controllers;

import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

public class NpcShipController extends AutoPilotShipController
{
    int fireDelayticks = 0;

    public NpcShipController()
    {
        fireDelayticks = Util.randRange(50, 500);
    }

    @Override
    public void move(Ship ship)
    {
        move_old(ship);
    }

    @Override
    public void update(Ship ship)
    {
        Ship player = Global.game.getPlayer();
        Weapon weapon = ship.getWeapon();

        if (player == null) {
            weapon.stopFiring();
            return;
        }

        // Target player
        int px = (int)(player.getX() + player.getWidth() / 2);
        int py = (int)(player.getY() + player.getHeight() / 2);
        ship.targetLocation(px, py);

        // Toggle between firing weapon and waiting
        if (fireDelayticks-- <= 0) {
            if (weapon.isFiring()) {
                weapon.stopFiring();
            } else {
                weapon.startFiring();
            }
            fireDelayticks = Util.randRange(50, 500);
        }
    }
}
