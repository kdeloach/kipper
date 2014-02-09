package kipper.ships.controllers;

import java.awt.*;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

// Takes the ship to its destination
public class AutoPilotShipController extends ShipController
{
    double acc = 0;
    public double vx = 0;
    public double vy = 0;

    @Override
    public void move(Ship ship)
    {
        move_elastic(ship);
        //move_old(ship);
    }

    public void move_elastic2(Ship ship)
    {
        Point destination = ship.getDestination();
        double x1 = ship.getX();
        double y1 = ship.getY();
        double x2 = destination.x;
        double y2 = destination.y;
        double d1 = distance(x1, y1, x2, y2);
        double theta = Math.atan2(y2 - y1, x2 - x1);
        acc = d1/1000;
        vx += Math.cos(theta) * acc;
        vy += Math.sin(theta) * acc;

        double mx = x1 + vx;
        double my = y1 + vy;
        ship.setLocation(mx, my);
        //System.out.println("vel="+vel+", d1="+d1);
    }

    public void move_elastic(Ship ship)
    {
        Point destination = ship.getDestination();
        double x1 = ship.getX();
        double y1 = ship.getY();
        double x2 = destination.x;
        double y2 = destination.y;
        double d1 = distance(x1, y1, x2, y2);
        double theta = Math.atan2(y2 - y1, x2 - x1);
        double acc = d1/100;
        vx -= vx / 10;
        vy -= vy / 10;
        vx += Math.cos(theta) * acc;
        vy += Math.sin(theta) * acc;
        if (Math.abs(vx) < 0.01 || Math.abs(vy) < 0.01) {
            return;
        }
        double mx = x1 + vx;
        double my = y1 + vy;
        ship.setLocation(mx, my);
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
        vx = (destination.x - ship.getX()) / ship.getSpeed();
        vy = (destination.y - ship.getY()) / ship.getSpeed();
        double mx = ship.getX() + vx;
        double my = ship.getY() + vy;
        ship.setLocation(mx, my);
    }
}
