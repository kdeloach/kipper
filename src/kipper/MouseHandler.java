package kipper;

import java.awt.*;
import java.awt.event.*;

public class MouseHandler implements MouseListener, MouseMotionListener
{
    private Point p = new Point();
    private boolean isPressed = false;
    private boolean justPressed = false;
    private boolean justReleased = false;

    @Override
    public void mouseEntered(MouseEvent evt)
    {
    }

    @Override
    public void mouseExited(MouseEvent evt)
    {
    }

    @Override
    public void mouseClicked(MouseEvent evt)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        isPressed = true;
        justPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        isPressed = false;
        justReleased = true;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        p.x = e.getX();
        p.y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        p.x = e.getX();
        p.y = e.getY();
    }

    public void update()
    {
        justPressed = false;
        justReleased = false;
    }

    public int getX() { return p.x; }
    public int getY() { return p.y; }
    public Point getPoint() { return p; }
    public boolean justPressed() { return justPressed; }
    public boolean justReleased() { return justReleased; }
    public boolean isPressed() { return isPressed; }
}
