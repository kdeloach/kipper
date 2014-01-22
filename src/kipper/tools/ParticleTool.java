package kipper.tools;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import kipper.*;
import kipper.ships.*;
import kipper.effects.*;
import particledsl.*;

public class ParticleTool implements ChangeListener, ActionListener
{
    JFrame frame;
    Container pane;
    JPanel controls;
    JTextArea txt;
    ParticleDrawPanel drawpanel;
    ParticleEmitterConfig config;
    JSlider maxParticlesSlider, durationTicksSlider, spawnRateSlider, continuousSlider;
    JLabel lblMaxParticles, lblDuration, lblSpawnRate, lblContinuous;
    JTextField txtTheta, txtHue, txtSaturation, txtBrightness, txtSize, txtSpeed;

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
        addTextControls();
        updateLabels();
        updateConfig();

        pane.add(drawpanel, BorderLayout.CENTER);
        pane.add(controls, BorderLayout.LINE_END);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread((Runnable)drawpanel).start();
    }

    public void stateChanged(ChangeEvent e)
    {
        updateConfig();
        updateLabels();
    }

    public void actionPerformed(ActionEvent e)
    {
        updateConfig();
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

    private void addTextControls()
    {
        txtTheta = new JTextField("random()");
        txtTheta.addActionListener(this);
        controls.add(new JLabel("Theta"));
        controls.add(txtTheta);

        txtSpeed = new JTextField("1");
        txtSpeed.addActionListener(this);
        controls.add(new JLabel("Speed"));
        controls.add(txtSpeed);

        txtSize = new JTextField("linear(10, 1)");
        txtSize.addActionListener(this);
        controls.add(new JLabel("Size"));
        controls.add(txtSize);

        txtHue = new JTextField("60/360");
        txtHue.addActionListener(this);
        controls.add(new JLabel("Hue"));
        controls.add(txtHue);

        txtSaturation = new JTextField("1");
        txtSaturation.addActionListener(this);
        controls.add(new JLabel("Saturation"));
        controls.add(txtSaturation);

        txtBrightness = new JTextField("1");
        txtBrightness.addActionListener(this);
        controls.add(new JLabel("Brightness"));
        controls.add(txtBrightness);
    }

    private void updateLabels()
    {
        lblMaxParticles.setText("Max Particles (" + config.maxParticles + ")");
        lblDuration.setText("Duration (" + config.durationTicks + ")");
        lblSpawnRate.setText("Spawn Rate (" + config.spawnRate + ")");
        lblContinuous.setText("Continuous? (" + (config.continuous ? "Yes" : "No") + ")");
    }

    public void updateConfig()
    {
        config.maxParticles = maxParticlesSlider.getValue();
        config.durationTicks = durationTicksSlider.getValue();
        config.spawnRate = (double)(spawnRateSlider.getValue() / 100.0);
        config.continuous = continuousSlider.getValue() == 1 ? true : false;
        config.theta = new ParticleLang(txtTheta.getText()).getValue();
        config.speed = new ParticleLang(txtSpeed.getText()).getValue();
        config.size = new ParticleLang(txtSize.getText()).getValue();
        config.hue = new ParticleLang(txtHue.getText()).getValue();
        config.saturation = new ParticleLang(txtSaturation.getText()).getValue();
        config.brightness = new ParticleLang(txtBrightness.getText()).getValue();
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
    @Override public Dimension getPreferredSize() { return new Dimension(400, 400); }
}
