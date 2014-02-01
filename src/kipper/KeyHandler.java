package kipper;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KeyHandler implements KeyListener
{
    private boolean isShiftDown = false;
    private Hashtable<Integer, Boolean> justPressed = new Hashtable<Integer, Boolean>();
    private Hashtable<Integer, Boolean> justReleased = new Hashtable<Integer, Boolean>();

    @Override
    public void keyTyped(KeyEvent evt)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        isShiftDown = e.isShiftDown();
        justPressed.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        isShiftDown = e.isShiftDown();
        justReleased.put(e.getKeyCode(), true);
    }

    public void update()
    {
        justPressed.clear();
        justReleased.clear();
    }

    public boolean isShiftDown() { return isShiftDown; }
    public boolean justPressed(int keyCode) { return justPressed.containsKey(keyCode); }
    public boolean justReleased(int keyCode) { return justReleased.containsKey(keyCode); }
}
