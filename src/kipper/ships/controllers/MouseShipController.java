package kipper.ships.controllers;

import java.awt.*;
import java.awt.event.*;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

public class MouseShipController extends ElasticShipController
{
    @Override
    public void handleInput(Ship ship)
    {
        Point p = Util.scalePoint(Global.mouse.getPoint());
        ship.setMouseLocation(p.x, p.y);
        ship.setMousePressedLocation(p.x, p.y);
        ship.setDestination(p.x - ship.getWidth() / 2,
                            p.y - ship.getHeight() / 2);

        Weapon weapon = ship.getWeapon();
        if (weapon != null) {
            weapon.setMouseLocation(p.x, p.y);

            if (Global.mouse.isPressed()) {
                weapon.startFiring();
            } else {
                weapon.stopFiring();
            }

            int code = Global.key.justPressed(KeyEvent.VK_1) ? 0
                     : Global.key.justPressed(KeyEvent.VK_2) ? 1
                     : Global.key.justPressed(KeyEvent.VK_3) ? 2
                     : Global.key.justPressed(KeyEvent.VK_4) ? 3
                     : -1;

            if (code >= 0 && ship.getWeaponAt(code) != ship.getWeapon()) {
                boolean isFiring = weapon.isFiring();
                weapon.stopFiring();
                ship.selectWeapon(code);
                if (isFiring) {
                    ship.getWeapon().startFiring();
                }
            }
        }
    }

    @Override
    public void move(Ship ship)
    {
        if (!Global.key.isShiftDown()) {
            super.move(ship);
        }
    }
}
