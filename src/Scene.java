package kipper;

import java.awt.event.*;
import java.awt.Graphics;

// superclass for all levels,scenes,screens,menus,etc.
// Just have to remember to transfer all ship data and arsenal data to each new screen
// for example, when the game starts have a DEFAULT screen
// then, for debug purposes have an esc key to launch the Upgrade screen or w/e

// question: should the weaponList be an ArrayList on an array
// is there a maximum amount of weapons a Ship can hold?
// ARRAY

public abstract class Scene implements MouseListener, MouseMotionListener, KeyListener
{
    //public abstract transition(Ship s, Weapon[] arsenal);

    //public abstract Ship getPlayer1();
    //public abstract Weapon[] getArsenal();

    public abstract String name();
    public abstract void createScene();
    public abstract void destroyScene();

    ///

    public void mouseEntered(MouseEvent evt){}
    public void mouseExited(MouseEvent evt){}
    public void mouseClicked(MouseEvent evt){}

    ///

    public void mousePressed(MouseEvent evt){}
    public void mouseReleased(MouseEvent evt){}
    public void mouseDragged(MouseEvent evt){}
    public void mouseMoved(MouseEvent evt){}

    ///

    public void keyTyped(KeyEvent evt){}
    public void keyPressed(KeyEvent evt){}
    public void keyReleased(KeyEvent evt){}

    ///

    public void paint(Graphics g){}
}