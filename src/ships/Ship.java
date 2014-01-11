package kipper.ships;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import kipper.*;
import kipper.weapons.*;
import kipper.upgrades.*;

// IT hasnt been decided yet BUT Ships may have special abilities
// Abilities applied to ship affect all equiped weapons
// Every 3 levels or such you get an extra slot, CAPPED
// Special abilities will showup on a panel underneath game with cooldown clocks
// the keys Q-P will activate special abilities
//
// Special Abilites MAY include the following:
//		+hide					- cloak/protect ship from bullets for 3sec,30sec cooldown
//		+turret				- summon turret to help fight for 30sec,60sec cooldown
//		+dual-wield		-	can hold 2 weapons,no cooldown
//		+repair				-	repair 10%hp,cooldown 90sec
//		+slow					- slow everything down slightly for 10sec,5min cooldown
//		+stabalizer		- make movement 1:1 ratio instead of gravity
//		+exp inc			-	ship gets +5% more exp than normal

// NATURAL SHIP ABILITY CANNOT DISABLE
//		+zone select	- enemies stop spawning and in the BG different zones appear,Spacebar changes zones...

// A standard template all ships can extend
public abstract class Ship implements
    Destructable, Controllable, Upgradable, MouseListener, MouseMotionListener, KeyListener
{
	// arbitrary dimensions
	public double x, y;
    public int width, height;

	// amount of damage taken
	protected double dmg;

	// speed of ship
	protected int speed;

	private int exp;

	// amount of slots
	private int slots = 6;

	// maximum health
	private int maxhp;

	// ship id
	protected int id;

	// direction ship face
	private int orientation;

	// friend or foe?
	private int team;

	// formality
	protected boolean underControl = false;

	// controller variables
	protected boolean alive = true;
    protected boolean disabled = false;

	// current position of the mouse,destination
	public Point destination = new Point();
    public Point mouse = new Point();
    public Point mousePressed = new Point();

	// arsenel,cycle with 0-9
	public Weapon wpn;
    public Weapon[] wpnList = new Weapon[10];

	// master panel, access to other ships and elements
	protected OuterSpacePanel osp;

	// last ship hit
	public Ship target = null;

	// all the upgrades picked up from battle go here
	private ArrayList<Ability> inventory = new ArrayList<Ability>();

	// mask
	private Polygon mask;

	////////////

    public Ship()
    {
    }

	public Ship(double x, double y, OuterSpacePanel c)
    {
		this.osp = c;

		// defaults
		mask = getDefaultMask();
		orientation = getDefaultOrientation();
		speed = getDefaultSpeed();
		maxhp = defaultMaxHp();
		team = defaultTeam();

		setLocation(x, y);
		// hack; changes initial value of [desination] so NPC's don't float to 0,0 onload
		setDestination(getX(), getY());

        osp.registerShip(this);
	}

    public void update()
    {
		if (!alive) {
            return;
        }

        if (!underControl()) {
            think();
        }

        move();

        Ship o = osp.intersects(this);
        if (o != null) {
            double dmg = Math.min(o.getHp(), getHp());
            o.hit(dmg);
            hit(dmg);
        }
    }

    public void move()
    {
        double mx = x + (destination.x - x - width / 2) / getSpeed();
        double my = y + (destination.y - y - height / 2) / getSpeed();
        setLocation(mx, my);
        if (getWeapon() != null) {
            getWeapon().setLocation(x, y);
        }
    }

	public void setDestination(double mx, double my)
    {
		// don't let the player go off-screen
		if (underControl()) {
			if (mx < width / 2) {
                mx = width / 2;
            } else if (mx > OuterSpacePanel.WIDTH - width / 2) {
                mx = OuterSpacePanel.WIDTH - width / 2;
            }
			if (my < height / 2) {
                my = height / 2;
            } else if (my > OuterSpacePanel.HEIGHT - height / 2) {
                my = OuterSpacePanel.HEIGHT - height / 2;
            }
		}
        destination = new Point((int)mx, (int)my);
	}

	// Here is where you can put AI for a NPC
	// think() is called every "round"
	public void think() {}

	// NPC's ONLY Please; hack(ish)
	public void targetLocation(int x, int y)
    {
		setMousePressedLocation(x, y);
	}

	public void hit(double dmg)
    {
		if (getHp() <= 0) {
            return;
        }
		if (getHp() - dmg <= 0) {
			this.dmg = maxHp();
			explode();
		} else {
			this.dmg += dmg;
        }
	}

	public void explode()
    {
		alive = false;
		disabled = true;

		die();

		getWeapon().percCooled = getWeapon().getCooldown();
		getWeapon().stopFiring();
		osp.unregisterShip(this);
	}

	public void equipWeapon(Weapon w)
    {
		for (int i = 0; i < wpnList.length; i++) {
			if (wpnList[i] == null) {
				wpnList[i] = w;
				return;
			}
		}
	}

	public void setSize(int w, int h)
    {
        this.width = w;
        this.height = h;
	}

	public void setLocation(double x, double y)
    {
		this.x = x;
		this.y = y;
	}

	public void selectWeapon(int n)
    {
		if (n < 0 || n >= wpnList.length) {
			throw new IllegalArgumentException("Weapon [" + n + "] does not exist");
        }
		if (wpnList[n] == null) {
			return;
        }
		wpn = wpnList[n];
		wpn.setLocation(x, y);
	}

	public void setMouseLocation(int x, int y)
    {
		mouse.x = x;
		mouse.y = y;
	}

	public void setMousePressedLocation(int x, int y)
    {
		mousePressed.x = x;
		mousePressed.y = y;
	}

	public double heading() {
		// 0 or 1 default
		return Math.toRadians(orientation * 180);
	}

	public OuterSpacePanel panel() { return osp; }

	/////////////

	abstract public int getDefaultOrientation();
	abstract public int defaultMaxHp();
	abstract public int defaultTeam();
	abstract public int getDefaultSpeed();
	abstract public Polygon getDefaultMask();
	abstract public String getName();
	abstract public void draw(Graphics g);

	/////////////

	public Weapon getWeapon(int n)
    {
		if(n < 0 || n >= wpnList.length) {
			throw new IllegalArgumentException("Weapon [" + n + "] does not exist");
        }
		return wpnList[n];
	}

    // TODO: Incorrect reporting of X & Y...Remove
	public double getX() { return x + width / 2; }
	public double getY() { return y + height / 2; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getSlotsAmt() { return slots; }
	public int getTeam() { return team; }
	public Ship getTarget() { return target; }
	public Point getDesination() { return destination; }
	public int getOrientation() { return orientation; }
	public int maxHp() { return maxhp; }
	public int getSpeed() { return speed; }
	public Weapon getWeapon() { return wpn; }
	public double getHp() { return maxHp() - dmg; }
	public boolean isAlive() { return alive; }
	public boolean isDisabled() { return disabled; }
	public double percentHealth() { return (double)getHp() /( double)maxHp(); }

    // TODO: Remove
	public int getId() { return id; }
    public void setId(int k) { id = k; }

    // Destructable
    /////////////
    @Override abstract public void die();
    @Override public Polygon getMask() { return mask; }

    @Override
	public boolean contains(int x, int y)
    {
		return contains(new Rectangle(x, y, 1, 1));
	}

    @Override
	public boolean contains(Point p)
    {
		return contains(new Rectangle(p.x, p.y, 1, 1));
	}

    @Override
	public boolean contains(Rectangle r)
    {
        Polygon tmp = new Polygon(mask.xpoints, mask.ypoints, mask.npoints);
        tmp.translate((int)x, (int)y);
		return tmp.contains(r);
	}

    @Override
	public boolean intersects(Destructable s)
    {
        Polygon tmp = new Polygon(mask.xpoints, mask.ypoints, mask.npoints);
        tmp.translate((int)x, (int)y);
		for (int i = 0; i < tmp.npoints; i++) {
			if (s.contains(tmp.xpoints[i], tmp.ypoints[i])) {
				return true;
            }
		}
		return false;
	}

    @Override
	public boolean intersects(Rectangle r)
    {
        Polygon tmp = new Polygon(mask.xpoints, mask.ypoints, mask.npoints);
        tmp.translate((int)x, (int)y);
		return tmp.intersects(r);
	}

    // Controllable
	/////////////
    @Override public boolean underControl() { return underControl; }

    @Override
	public void gainControl()
    {
		osp.addMouseListener(this);
		osp.addMouseMotionListener(this);
		underControl = true;
	}

    @Override
	public void releaseControl()
    {
		osp.removeMouseListener(this);
		osp.removeMouseMotionListener(this);
		underControl = false;
	}

    // MouseListener
    /////////////
	@Override public void mouseEntered(MouseEvent evt) {}
	@Override public void mouseExited(MouseEvent evt) {}
	@Override public void mouseClicked(MouseEvent evt) {}

    @Override
	public void mousePressed(MouseEvent evt)
    {
		setMousePressedLocation(evt.getX(), evt.getY());
		if (evt.getButton() == MouseEvent.BUTTON1 && !disabled) {
			getWeapon().startFiring();
		}
	}

    @Override
	public void mouseReleased(MouseEvent evt)
    {
		if (evt.getButton() == MouseEvent.BUTTON1) {
			getWeapon().stopFiring();
		}
	}

    // MouseMotionListener
    /////////////
    @Override public void mouseDragged(MouseEvent evt) { mouseMoved(evt); }

    @Override
	public void mouseMoved(MouseEvent evt)
    {
		setMouseLocation(evt.getX(), evt.getY());
		getWeapon().setMouseLocation(evt.getX(), evt.getY());
        setDestination(evt.getX(), evt.getY());
	}

    // KeyListener
    /////////////
    @Override public void keyTyped(KeyEvent evt) {}
    @Override public void keyReleased(KeyEvent evt) {}

    @Override
	public void keyPressed(KeyEvent evt)
    {
		int code = evt.getKeyCode() - KeyEvent.VK_1;

		// only need keys 1-5	(0-4)
		// and abort if weapon request is already selected
		if (code < 0 || code >= 5 || wpnList[code] == getWeapon()) {
			return;
        }

		// resume firing if weapons switches while the fire button is held down
		if (getWeapon().isFiring()) {
			getWeapon().stopFiring();
			selectWeapon(code);
			getWeapon().startFiring();
		} else {
			selectWeapon(code);
		}
	}

    // Upgradable
    /////////////
    @Override public Ability upgradeAt(int index) { throw new UnsupportedOperationException("Not implemented"); }
    @Override public void addUpgrade(Ability a) { throw new UnsupportedOperationException("Not implemented"); }
    @Override public void removeUpgrade(int index) { throw new UnsupportedOperationException("Not implemented"); }
}
