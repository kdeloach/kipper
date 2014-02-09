package kipper.ships.controllers;

import java.awt.*;
import java.awt.geom.*;
import kipper.*;
import kipper.ships.*;
import kipper.weapons.*;

public class ElasticShipController extends AutoPilotShipController
{
    public double vx = 0;
    public double vy = 0;

    @Override
    public Point2D.Double getVelocity(Ship ship)
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
            vx = 0;
            vy = 0;
        }
        return new Point2D.Double(vx, vy);
    }

    private double distance(double x1, double y1, double x2, double y2)
    {
        double x = x2 - x1;
        double y = y2 - y1;
        return Math.sqrt(x*x + y*y);
    }
}
