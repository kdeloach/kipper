package kipper.tools;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Point;
import java.awt.Image;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Container;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import kipper.Util;
import kipper.OuterSpacePanel;
import kipper.ships.*;
import kipper.effects.*;

public class ParticleTool implements ChangeListener
{
    JFrame frame;
    Container pane;
    JPanel controls;
    JTextArea txt;
    ParticleDrawPanel drawpanel;
    ParticleEmitterConfig config;
    JSlider maxParticlesSlider, durationTicksSlider, spawnRateSlider, continuousSlider;
    JLabel lblMaxParticles, lblDuration, lblSpawnRate, lblContinuous;
    JSlider sizeStartSlider;
    JLabel lblSizeStart, lblSizeEnd, lblSizeEase;

    public static void main(String[] argv)
    {
        new ParticleTool();
    }

    public ParticleTool()
    {
        frame = new JFrame("particle tool");
        pane = frame.getContentPane();
        pane.setLayout(new BorderLayout(10, 10));
        txt = new JTextArea("");
        config = new ParticleEmitterConfig();
        drawpanel = new ParticleDrawPanel(config);

        controls = new JPanel();
        controls.setLayout(new GridLayout(5, 2));

        addBasicControls();
        addSizeControls();
        updateLabels();

        pane.add(drawpanel, BorderLayout.CENTER);
        pane.add(controls, BorderLayout.LINE_END);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread((Runnable)drawpanel).start();
    }

    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        if (source == maxParticlesSlider) {
            config.maxParticles = (int)source.getValue();
        } else if (source == durationTicksSlider) {
            config.durationTicks = (int)source.getValue();
        } else if (source == spawnRateSlider) {
            config.spawnRate = (double)((int)source.getValue() / 100.0);
        } else if (source == continuousSlider) {
            config.continuous = (int)source.getValue() == 1 ? true : false;
        } else if (source == sizeStartSlider) {
            //
        }
        updateLabels();
    }

    private void addBasicControls()
    {
        lblMaxParticles = new JLabel();
        maxParticlesSlider = new JSlider(JSlider.HORIZONTAL, 0, 300, 20);
        maxParticlesSlider.setPaintTicks(true);
        maxParticlesSlider.setPaintLabels(true);
        maxParticlesSlider.setSnapToTicks(true);
        maxParticlesSlider.setMinorTickSpacing(10);
        maxParticlesSlider.setMajorTickSpacing(50);
        maxParticlesSlider.addChangeListener(this);
        controls.add(lblMaxParticles);
        controls.add(maxParticlesSlider);

        lblDuration = new JLabel();
        durationTicksSlider = new JSlider(JSlider.HORIZONTAL, 0, 400, 180);
        durationTicksSlider.setPaintTicks(true);
        durationTicksSlider.setPaintLabels(true);
        durationTicksSlider.setSnapToTicks(true);
        durationTicksSlider.setMinorTickSpacing(10);
        durationTicksSlider.setMajorTickSpacing(100);
        durationTicksSlider.addChangeListener(this);
        controls.add(lblDuration);
        controls.add(durationTicksSlider);

        lblSpawnRate = new JLabel();
        spawnRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        spawnRateSlider.setPaintTicks(true);
        spawnRateSlider.setPaintLabels(true);
        spawnRateSlider.setSnapToTicks(true);
        spawnRateSlider.setMinorTickSpacing(5);
        spawnRateSlider.setMajorTickSpacing(20);
        spawnRateSlider.addChangeListener(this);
        controls.add(lblSpawnRate);
        controls.add(spawnRateSlider);

        lblContinuous = new JLabel();
        continuousSlider = new JSlider(JSlider.HORIZONTAL, 0, 1, 0);
        continuousSlider.setPaintTicks(true);
        continuousSlider.setPaintLabels(true);
        continuousSlider.setSnapToTicks(true);
        continuousSlider.setMajorTickSpacing(1);
        Hashtable<Integer, JLabel> continuousLabels = new Hashtable<Integer, JLabel>();
        continuousLabels.put(0, new JLabel("No"));
        continuousLabels.put(1, new JLabel("Yes"));
        continuousSlider.setLabelTable(continuousLabels);
        continuousSlider.addChangeListener(this);
        controls.add(lblContinuous);
        controls.add(continuousSlider);
    }

    private void addSizeControls()
    {
        lblSizeStart = new JLabel();
        sizeStartSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
        sizeStartSlider.setPaintTicks(true);
        sizeStartSlider.setPaintLabels(true);
        sizeStartSlider.setSnapToTicks(true);
        sizeStartSlider.setMinorTickSpacing(1);
        sizeStartSlider.setMajorTickSpacing(2);
        sizeStartSlider.addChangeListener(this);
        controls.add(lblSizeStart);
        controls.add(sizeStartSlider);
    }

    private void updateLabels()
    {
        lblMaxParticles.setText("Max Particles (" + config.maxParticles + ")");
        lblDuration.setText("Duration (" + config.durationTicks + ")");
        lblSpawnRate.setText("Spawn Rate (" + config.spawnRate + ")");
        lblContinuous.setText("Continuous? (" + (config.continuous ? "Yes" : "No") + ")");
        lblSizeStart.setText("Size Start");
        //lblSizeEnd.setText("Size End");
        //lblSizeEase.setText("Size Ease");
    }
}


class ParticleDrawPanel extends JComponent implements Runnable
{
    private int pX = 0, pY = 0;
    private ParticleEmitter emitter;
    private ParticleEmitterConfig config;

    public ParticleDrawPanel(ParticleEmitterConfig config)
    {
        super();
        setIgnoreRepaint(true);

        this.config = config;
        this.emitter = new ParticleEmitter(pX, pY, config);
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
            Image img = createImage(w, h);
            draw(img.getGraphics());
            getGraphics().drawImage(img, 0, 0, w, h, this);
        }
    }

    public void update()
    {
        if (emitter.isAlive()) {
            emitter.update();
        } else {
            emitter = new ParticleEmitter(pX, pY, config);
        }
    }

    public void draw(Graphics g)
    {
        if (emitter == null) {
            return;
        }

        pX = getWidth() / 2;
        pY = getHeight() / 2;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        emitter.setLocation(pX, pY);
        emitter.draw(g);
    }

    @Override public Dimension getMaximumSize() { return getPreferredSize(); }
    @Override public Dimension getPreferredSize() { return new Dimension(250, 250); }
}
