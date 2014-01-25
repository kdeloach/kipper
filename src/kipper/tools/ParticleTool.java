package kipper.tools;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
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
    JCheckBox cbxFollowMouse;

    Action followMouseAction;
    Action saveAction;
    Action saveAsAction;
    Action openAction;
    Action updateConfigAction;

    final JFileChooser fc = new JFileChooser();
    File file;

    public static void main(String[] argv)
    {
        new ParticleTool();
    }

    public ParticleTool()
    {
        fc.setCurrentDirectory(new File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Python file", "py");
        fc.setFileFilter(filter);

        txtPy = new JTextArea();
        txtPy.setColumns(75);
        txtPy.setLineWrap(true);
        txtPy.setDocument(new TabDocument());
        txtPy.setFont(new Font("Courier New", Font.PLAIN, 14));
        JScrollPane txtPyScroll = new JScrollPane(txtPy);
        txtPyScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        file = new File("./src/kipper/effects/SampleConfigImpl.py");
        openFile();

        drawpanel = new ParticleDrawPanel();
        updateDrawPanel();

        followMouseAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                drawpanel.setFollowMouse(cbxFollowMouse.isSelected());
            }
        };
        saveAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (file == null) {
                    int returnVal = fc.showSaveDialog(frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        file = fc.getSelectedFile();
                    } else {
                        return;
                    }
                }
                saveFile();
                updateDrawPanel();
                updateWindowTitle();
            }
        };
        saveAsAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                    saveFile();
                    updateDrawPanel();
                    updateWindowTitle();
                }
            }
        };
        openAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                    openFile();
                    updateDrawPanel();
                    updateWindowTitle();
                }
            }
        };
        updateConfigAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                updateDrawPanel();
            }
        };

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEADING));

        JButton btnOpen = new JButton();
        btnOpen.setAction(openAction);
        btnOpen.setMnemonic(KeyEvent.VK_O);
        btnOpen.setText("Open");
        toolbar.add(btnOpen);

        JButton btnSave = new JButton();
        btnSave.setAction(saveAction);
        btnSave.setMnemonic(KeyEvent.VK_S);
        btnSave.setText("Save");
        toolbar.add(btnSave);

        JButton btnSaveAs = new JButton();
        btnSaveAs.setAction(saveAsAction);
        btnSaveAs.setMnemonic(KeyEvent.VK_A);
        btnSaveAs.setText("Save As");
        toolbar.add(btnSaveAs);

        JButton btnUpdate = new JButton();
        btnUpdate.setAction(updateConfigAction);
        btnUpdate.setMnemonic(KeyEvent.VK_R);
        btnUpdate.setText("Refresh");
        toolbar.add(btnUpdate);

        cbxFollowMouse = new JCheckBox();
        cbxFollowMouse.setAction(followMouseAction);
        cbxFollowMouse.setMnemonic(KeyEvent.VK_F);
        cbxFollowMouse.setText("Follow mouse");
        toolbar.add(cbxFollowMouse);

        JPanel editors = new JPanel();
        editors.setLayout(new GridLayout(1, 2));
        editors.add(drawpanel);
        editors.add(txtPyScroll);

        frame = new JFrame();
        updateWindowTitle();
        pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.add(toolbar);
        pane.add(editors);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("F5"), "updateConfig");
        inputMap.put(KeyStroke.getKeyStroke("ctrl R"), "updateConfig");
        inputMap.put(KeyStroke.getKeyStroke("ctrl S"), "save");
        inputMap.put(KeyStroke.getKeyStroke("ctrl alt S"), "saveAs");
        inputMap.put(KeyStroke.getKeyStroke("ctrl O"), "open");
        actionMap.put("updateConfig", updateConfigAction);
        actionMap.put("save", saveAction);
        actionMap.put("saveAs", saveAsAction);
        actionMap.put("open", openAction);

        new Thread((Runnable)drawpanel).start();
    }

    public void updateWindowTitle()
    {
        String title = "particle tool";
        if (file != null) {
            title += " - " + file.getAbsolutePath();
        }
        frame.setTitle(title);
    }

    public void saveFile()
    {
        try {
            Writer w = new BufferedWriter(new FileWriter(file));
            w.write(txtPy.getText());
            w.close();
        } catch (IOException ex) {
            System.out.println("Unable to save file");
            ex.printStackTrace();
        }
    }

    public void openFile()
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            br.close();
            txtPy.setText(sb.toString());
        } catch (IOException ex) {
            System.out.println("Unable to open file");
            ex.printStackTrace();
        }
    }

    public void updateDrawPanel()
    {
        drawpanel.updateConfig(txtPy.getText());
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
                System.out.println("Set emitter config");
                this.config = config;
                this.emitter.setConfig(this.config);
            }
        } catch (Exception ex) {
            System.out.println("Error interpreting jython");
            ex.printStackTrace();
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
        } catch (Exception ex) {
            this.config = new SampleConfigImpl();
            this.emitter.setConfig(this.config);
            System.out.println("Exception in update method");
            //ex.printStackTrace();
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
        } catch (Exception ex) {
            this.config = new SampleConfigImpl();
            this.emitter.setConfig(this.config);
            System.out.println("Exception in draw method");
            //ex.printStackTrace();
        }

        g.setColor(Color.WHITE);
        g.drawString("Ticks: " + emitter.getTicks(), 5, 15);
        g.drawString("# Particles: " + emitter.getNumAlive(), 5, 30);
        g.drawString("Max # Particles: " + emitter.getMaxNumAlive(), 5, 45);
        g.drawString("Population: " + emitter.getPopulation(), 5, 60);
    }

    public void setFollowMouse(boolean enabled)
    {
        followMouse = enabled;
    }
}
