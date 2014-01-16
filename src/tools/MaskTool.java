package kipper.tools;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Point;
import java.awt.Image;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener ;
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
    JButton btn;
    JComboBox<Ship> cbxShips;
    JPanel pane1, pane2;
    JTextArea txt;
    DrawPanel drawpanel;

    public static void main(String[] argv)
    {
        new MaskTool();
    }

    public MaskTool()
    {
        txt = new JTextArea("");
        drawpanel = new DrawPanel(txt);

        Ship[] shipsData = new Ship[] {
            new Darkwing(0, 0, null),
            new Enterprise(0, 0, null)
        };

		frame = new JFrame("mask tool");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        pane1 = new JPanel();
        pane2 = new JPanel();

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
        pane1.add(cbxShips);

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

        frame.add(pane1);
		frame.add(drawpanel);
        frame.add(pane2);

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

        // Display grid lines
		// g.setColor(Color.BLACK);
        // for (int y = imgY; y <= imgHeight + imgY; y += factor) {
            // for (int x = imgX; x <= imgWidth + imgX; x += factor) {
                // g.drawRect(x, y, factor, factor);
            // }
        // }

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

		sb.add("private static _mask = new Polygon(new int[] {");
		sb.add(Util.join(xz, ", "));
        sb.add("}, new int[] {");
        sb.add(Util.join(yz, ", "));
        sb.add("}, ");
        sb.add(maskPoints.size());
        sb.add(");\n}");

		sb.add("@Override\npublic Polygon getPolygonMask()\n{\n");
        sb.add("    return _mask;\n}");
        return Util.join(sb, "");
    }

    public int getWidth() { return 800; }
    public int getHeight() { return 600; }
	public Dimension getMinimumSize() { return new Dimension(getWidth(), getHeight()); }
	public Dimension getPreferredSize() { return getMinimumSize(); }

	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
}
