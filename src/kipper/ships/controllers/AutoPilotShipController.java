package kipper.ships.controllers;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// Takes the ship to its destination
public class AutoPilotShipController extends ShipController
{
    @Override
    public void move(Ship ship)
    {
        Point destination = ship.getDestination();
        double mx = ship.getX() + (destination.x - ship.getX()) / ship.getSpeed();
        double my = ship.getY() + (destination.y - ship.getY()) / ship.getSpeed();
        ship.setLocation(mx, my);
    }
}
