package kipper.ships.controllers;

import java.awt.*;
import java.awt.geom.*;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// Takes the ship to its destination
public class AutoPilotShipController extends ShipController
{
    @Override
    public void move(Ship ship)
    {
        Point2D.Double vel = getVelocity(ship);
        double mx = ship.getX() + vel.x;
        double my = ship.getY() + vel.y;
        ship.setLocation(mx, my);
    }

    public Point2D.Double getVelocity(Ship ship)
    {
        Point destination = ship.getDestination();
        double vx = (destination.x - ship.getX()) / ship.getSpeed();
        double vy = (destination.y - ship.getY()) / ship.getSpeed();
        return new Point2D.Double(vx, vy);
    }
}
