package kipper.scenes;

import java.awt.Graphics;

public abstract class Scene
{
    abstract public String getName();
    abstract public void createScene();
    abstract public void destroyScene();
    abstract public void handleInput();
    abstract public void draw(Graphics g);
}
