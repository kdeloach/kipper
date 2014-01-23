package kipper.tools;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyInteger;
import org.python.core.PyClass;
import org.python.util.PythonInterpreter;

class SampleConfigImpl extends ParticleEmitterConfig
{
}

public class ParticleTool
{
    JFrame frame;
    Container pane;
    ParticleDrawPanel drawpanel;
    JTextArea txtPy;

    public static void main(String[] argv)
    {
        new ParticleTool();
    }

    public ParticleTool()
    {
        frame = new JFrame("particle tool");
        pane = frame.getContentPane();
        pane.setLayout(new GridLayout(1, 1));
        drawpanel = new ParticleDrawPanel();

        // TODO: Load this from file or class
        StringBuilder defaultImpl = new StringBuilder();
        defaultImpl.append("from kipper.effects import ParticleEmitterConfig\n");
        defaultImpl.append("from kipper.effects.transitions import Linear\n");
        defaultImpl.append("\n");
        defaultImpl.append("import math\n");
        defaultImpl.append("import random\n");
        defaultImpl.append("\n");
        defaultImpl.append("L = Linear()\n");
        defaultImpl.append("\n");
        defaultImpl.append("class SampleConfigImpl(ParticleEmitterConfig):\n");
        defaultImpl.append("    def __init__(self):\n");
        defaultImpl.append("        pass\n");
        defaultImpl.append("\n");
        defaultImpl.append("    def getTheta(self, p):\n");
        defaultImpl.append("        if p.ticks < 1:\n");
        defaultImpl.append("            return random.random()*2*math.pi\n");
        defaultImpl.append("        return p.theta\n");
        defaultImpl.append("\n");
        defaultImpl.append("    def getSize(self, p):\n");
        defaultImpl.append("        return int(L.call(p.ticks, 15, -15, self.getDurationTicks()))\n");
        defaultImpl.append("\n");
        defaultImpl.append("    def getSpeed(self, p):\n");
        defaultImpl.append("        if p.ticks < 1:\n");
        defaultImpl.append("            return random.random()*2\n");
        defaultImpl.append("        return p.speed\n");
        defaultImpl.append("\n");
        defaultImpl.append("    def isRectShape(self, p):\n");
        defaultImpl.append("        return False\n");
        defaultImpl.append("\n");
        defaultImpl.append("    def getDurationTicks(self):\n");
        defaultImpl.append("        return 45\n");
        defaultImpl.append("\n");
        defaultImpl.append("    def getMaxParticles(self):\n");
        defaultImpl.append("        return 48\n");

        txtPy = new JTextArea();
        txtPy.setColumns(75);
        txtPy.setLineWrap(true);
        txtPy.setDocument(new TabDocument());
        txtPy.setFont(new Font("Courier New", Font.PLAIN, 14));
        txtPy.setText(defaultImpl.toString());
        JScrollPane scroll = new JScrollPane(txtPy);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        drawpanel.updateConfig(txtPy.getText());

        pane.add(drawpanel, BorderLayout.CENTER);
        pane.add(scroll, BorderLayout.LINE_END);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Action updateConfigAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("F5 Pressed");
                drawpanel.updateConfig(txtPy.getText());
            }
        };
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("F5"),
            "updateConfig");
        frame.getRootPane().getActionMap().put("updateConfig", updateConfigAction);

        new Thread((Runnable)drawpanel).start();
    }
}

// Source: http://stackoverflow.com/questions/363784/setting-the-tab-policy-in-swings-jtextpane
class TabDocument extends DefaultStyledDocument
{
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
        str = str.replaceAll("\t", "    ");
        super.insertString(offs, str, a);
    }
}

class ParticleDrawPanel extends JComponent implements Runnable, MouseMotionListener
{
    private int pX = 0, pY = 0;
    private ParticleEmitter emitter;
    private ParticleEmitterConfig config;
    private boolean followMouse = false;

    PythonInterpreter interpreter = new PythonInterpreter();

    public ParticleDrawPanel()
    {
        super();
        setIgnoreRepaint(true);
        addMouseMotionListener(this);
        this.config = new SampleConfigImpl();
        this.emitter = new ParticleEmitter(0, 0, config);
    }

    public void updateConfig(String input)
    {
        try {
            interpreter.exec(input);
            PyObject pyClass = interpreter.get("SampleConfigImpl", PyObject.class);
            if (pyClass == null) {
                System.out.println("Could not parse SampleConfigImpl class");
                return;
            }
            PyObject pyObj = pyClass.__call__();
            ParticleEmitterConfig config = (ParticleEmitterConfig)pyObj.__tojava__(ParticleEmitterConfig.class);
            if (config != null) {
                this.config = config;
                this.emitter.setConfig(config);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (followMouse) {
            pX = e.getX();
            pY = e.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
    }

    // Author: Bob Nystrom
    // Source: http://gameprogrammingpatterns.com/game-loop.html
    @Override
    public void run()
    {
        long previous = System.currentTimeMillis();
        long lag = 0;

        while (true) {
            long current = System.currentTimeMillis();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            while (lag >= OuterSpacePanel.FPS) {
                update();
                lag -= OuterSpacePanel.FPS;
            }

            int w = getWidth();
            int h = getHeight();
            if (w > 0 && h > 0) {
                Image img = createImage(w, h);
                draw(img.getGraphics());
                getGraphics().drawImage(img, 0, 0, w, h, this);
            }
        }
    }

    public void update()
    {
        try {
            if (!followMouse) {
                pX = getWidth() / 2;
                pY = getHeight() / 2;
            }
            if (emitter.isAlive()) {
                emitter.setLocation(pX, pY);
                emitter.update();
            } else {
                emitter = new ParticleEmitter(0, 0, config);
                emitter.setLocation(pX, pY);
            }
        } catch (Exception e) {
            System.out.println("Exception in update method: " + e);
        }
    }

    public void draw(Graphics g)
    {
        if (emitter == null) {
            return;
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        try {
            emitter.draw(g);
        } catch (Exception e) {
            System.out.println("Exception in draw method: " + e);
        }
    }

    @Override public Dimension getMaximumSize() { return getPreferredSize(); }
    @Override public Dimension getPreferredSize() { return new Dimension(800, 600); }
}
