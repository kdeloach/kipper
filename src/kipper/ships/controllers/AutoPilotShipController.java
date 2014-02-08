package kipper.ships.controllers;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// Takes the ship to its destination
public class AutoPilotShipController extends ShipController
{
    public double vx = 0;
    public double vy = 0;

    @Override
    public void move(Ship ship)
    {
        move_elastic(ship);
    }

    public void move_elastic(Ship ship)
    {
        Point destination = ship.getDestination();
        double x1 = ship.getX();
        double y1 = ship.getY();
        double x2 = destination.x;
        double y2 = destination.y;
        double d1 = distance(x1, y1, x2, y2);
        //d1 = Math.min(100, d1);
        double theta = Math.atan2(y2 - y1, x2 - x1);
        //vel += new kipper.effects.transitions.Linear().call(d1, 0, 10, 100)-5;
        double acc = d1/1000;
        vx = vx + Math.cos(theta) * acc;
        vy = vy + Math.sin(theta) * acc;
        double mx = x1 + vx;
        double my = y1 + vy;
        ship.setLocation(mx, my);
        //System.out.println("vel="+vel+", d1="+d1);
        //move_old(ship);
    }

    private double distance(double x1, double y1, double x2, double y2)
    {
        double x = x2 - x1;
        double y = y2 - y1;
        return Math.sqrt(x*x + y*y);
    }

    protected void move_old(Ship ship)
    {
        Point destination = ship.getDestination();
        double mx = ship.getX() + (destination.x - ship.getX()) / ship.getSpeed();
        double my = ship.getY() + (destination.y - ship.getY()) / ship.getSpeed();
        ship.setLocation(mx, my);
    }
}
