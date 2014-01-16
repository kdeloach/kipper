package kipper.tools;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import java.awt.Point;
import java.awt.Image;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import kipper.Util;
import kipper.ships.*;

public class MaskTool
{
    JFrame frame;
    Container pane1, pane2;
    JButton btn;
    JComboBox<Ship> cbxShips;
    JTextArea txt;
    DrawPanel drawpanel;

    public static void main(String[] argv)
    {
        new MaskTool();
    }

    public MaskTool()
    {
        frame = new JFrame("mask tool");
        pane1 = frame.getContentPane();
        pane1.setLayout(new BorderLayout());

        pane2 = new JPanel();
        txt = new JTextArea("");
        drawpanel = new DrawPanel(txt);

        Ship[] shipsData = new Ship[] {
            new Darkwing(0, 0, null),
            new Enterprise(0, 0, null)
        };

        cbxShips = new JComboBox<Ship>(shipsData);
        cbxShips.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JComboBox cb = (JComboBox)e.getSource();
                    drawpanel.loadShip((Ship)cb.getSelectedItem());
                }
            }
        );
        cbxShips.setSelectedIndex(0);

        txt.setRows(10);
        txt.setColumns(50);
        txt.setLineWrap(true);
        pane2.add(txt);

        btn = new JButton("clear");
        btn.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    txt.setText("");
                    drawpanel.clearMask();
                }
            }
        );
        pane2.add(btn);

        pane1.add(cbxShips, BorderLayout.PAGE_START);
        pane1.add(drawpanel, BorderLayout.CENTER);
        pane1.add(pane2, BorderLayout.PAGE_END);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class DrawPanel extends JPanel implements MouseListener, MouseWheelListener
{
    private Ship ship;
    private LinkedList<Point> maskPoints;
    private JTextArea txt;
    private int factor = 10;
    private int imgX, imgY, imgWidth, imgHeight;

    public DrawPanel(JTextArea t)
    {
        this.txt = t;
        addMouseListener(this);
        addMouseWheelListener(this);
    }

    public void loadShip(Ship ship)
    {
        this.ship = ship;
        maskPoints = new LinkedList<Point>();
        Polygon mask = ship.getPolygonMask();
        for (int i = 0; i < mask.npoints; i++) {
            maskPoints.push(new Point(mask.xpoints[i], mask.ypoints[i]));
        }
        txt.setText(getMaskCode());
        repaint();
    }

    public void clearMask()
    {
        maskPoints = new LinkedList<Point>();
        txt.setText(getMaskCode());
        repaint();
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        if (ship == null) {
            return;
        }

        imgWidth = ship.getImage().getWidth(this) * factor;
        imgHeight = ship.getImage().getHeight(this) * factor;

        imgX = getWidth() / 2 - imgWidth / 2;
        imgY = getHeight() / 2 - imgHeight / 2;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(ship.getImage(), imgX, imgY, imgWidth, imgHeight, this);

        Polygon scaledMask = getScaledMask();
        g.setColor(Color.GRAY);
        g.drawPolygon(scaledMask);
        g.setColor(Color.RED);
        for (int i = 0; i < scaledMask.npoints; i++) {
            g.fillRect(scaledMask.xpoints[i]-3, scaledMask.ypoints[i]-3, 6, 6);
        }
    }

    public Polygon getScaledMask()
    {
        Polygon result = new Polygon();
        for (int i = 0; i < maskPoints.size(); i++) {
            Point p = maskPoints.get(i);
            int x = p.x * factor + imgX;
            int y = p.y * factor + imgY;
            result.addPoint(x, y);
        }
        return result;
    }

    public void mouseReleased(MouseEvent e)
    {
        if (e.getButton() > MouseEvent.BUTTON1) {
            try {
                maskPoints.pop();
            } catch (NoSuchElementException ex) {}
        } else {
            int x = (e.getX() - imgX) / factor;
            int y = (e.getY() - imgY) / factor;
            maskPoints.push(new Point(x, y));
        }
        txt.setText(getMaskCode());
        repaint();
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        factor += -e.getWheelRotation();
        factor = Math.max(factor, 1);
        factor = Math.min(factor, 30);
        repaint();
    }

    String getMaskCode()
    {
        List<Object> sb = new ArrayList<Object>();
        List<Object> xz = new ArrayList<Object>();
        List<Object> yz = new ArrayList<Object>();

        for (int i = 0; i < maskPoints.size(); i++) {
            Point p = maskPoints.get(i);
            xz.add(p.x);
            yz.add(p.y);
        }

        sb.add("// Generated by mask tool\n");
        sb.add("@Override\npublic Polygon getPolygonMask()\n{\n");

        sb.add("    return new Polygon(new int[] {");
        sb.add(Util.join(xz, ", "));
        sb.add("}, new int[] {");
        sb.add(Util.join(yz, ", "));
        sb.add("}, ");
        sb.add(maskPoints.size());
        sb.add(");\n}");
        return Util.join(sb, "");
    }

    public Dimension getPreferredSize() { return new Dimension(800, 600); }

    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
}
