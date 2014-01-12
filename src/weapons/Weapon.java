package kipper.weapons;

import java.awt.Rectangle;
import java.awt.Point;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import kipper.*;
import kipper.ships.*;
import kipper.upgrades.*;

// WEAPONS should be cycled through with the 1-9 keys
// Weapons can be designed with "natural abilities" and "optional abilities"
// Naturals cannot be changed,optionals are accessed with the UPGRADE screen
// Exmples of weapons to be created are:
//		+shooter			- basic; has rotate
//		+blaster			- shotgun,has explode+recoil+dmg abi
//		+laser				-	shooter with diff bullet graphic,has dmg+speed abi
//		+mines				- mini bombs (destroyable),has spread+spread+recoil abi
//		+lightning		- fires a white line (TRY to write recursive algorithm for this),has shock+dmg+penetrate
//		+shell				- surrounds ship with (destroyable) rockets,has spread+spread+spread+dmg
//		+swirl				- rockets with a curious swirling path,has speed+speed+dmg
//		+seekers			- rockets that seek the enemy,has seek+explode
//		+intercept		-	launch sattelite that destroys rockets,has shield+regen+recoil
//		+zodiac				-	massive damage to all enemies,instant..LONG cooldown

// Every weapon has n amount of slots for upgrades
// Upgrades have chance to fall from enemy
// Possibly have special updgrade dropped by boss
// Upgrades can be used across every weapon
// To prevent overpowering, some upgrades may take more than 1 slot
// EXAMPLES of upgrades are:
//		+speed				- cooldown time is reduced,can shoot bullets faster
//		+dmg					- bullets do more damage
//		+spread				- can shoot x more bullets with each fire;MAY increase COOLDOWN time,ex Splitter,Triple Shooter,Quad Cannon
//		+explode			- increases shrapenel dmg from bullets,ex
//		+guard				- increases ship defense,used strategically against bosses,enemyDamage*(1/(guard+1))
//		+rotate				- ability to have gun rotate for more accurate firing
//		+regen				- ability to regen hp,ex heal [currentLevel] hit points over 30seconds
//		+shield				- ability to hold shield when not firing
//		+penetrate		- bullets can be used twice,ex after killing someone keep going and chance to hit another
//		+seek					- bullets can find nearest prey
//		+poison				- bullets become toxic,ex enemy hit with bullet will drain 2% hitpoints or such every 3 seconds
//		+shock				- bullet attacks nearby enemies on explode,ex when hit with shock the dmg will only be 40% or such
//		+cloak				- bullets become undetectable,lose speed;might be useful if support-drones can intercept ammo
//		+recoil				- sends the ship back with each shot,can be used to move around more strategically

// !!!SOON - start implementing upgrades

public abstract class Weapon implements Upgradable
{
    private double x, y;

	// (default) damage this baby can do
	private int damage;

	// spread is the amount of bullets you can shoot at once
	// for some added appeal the game should 'split' the stream of bullets by altering the heading()
	private int spread = 1;

	// percent cooled down
	public int percCooled = 0;

	// time to cooldown
	private int cooldown;

	// control variable,keep shooting gun while true
	// also a control variable to check if thread is still active
	private boolean fire = false;

	// position relative to Ship x&y,this is only set once
	private Point rel;

	// position of the mouse
	private Point mouse;

	// list of upgrades
	private ArrayList<Ability> upgrades;

	// owner
	private Ship ship;

	////////////

	public Weapon(double x, double y, int rx, int ry, Ship s)
    {
		this.x = x + rx;
		this.y = y + ry;
		this.ship = s;
		this.rel = new Point(rx, ry);

		setLocation(x, y);

		upgrades = new ArrayList<Ability>();
		mouse = new Point();
		cooldown = getDefaultCooldown();
		damage = getDefaultDamage();
	}

	////////////

    abstract public int getWidth();
    abstract public int getHeight();
	abstract public void fireProjectile(double heading);
	abstract public void draw(Graphics g);
	abstract public Image getIcon();
	abstract public int getDefaultCooldown();
	abstract public int getDefaultDamage();

	////////////

    public double getX() { return x; }
    public double getY() { return y; }
	public boolean isFiring() { return fire; }
	public void startFiring() { fire = true; }
	public void stopFiring() { fire = false; }
	public double percentCooled() { return (double)percCooled / (double)getCooldown(); }
	public int amountSlots() { return 12; }

	public int getCooldown() { return (int)getValue(Ability.COOLDOWN, cooldown); }
	public double getDamage() { return getValue(Ability.DAMAGE, damage); }
	public int getSpread() { return (int)getValue(Ability.SPREAD, spread); }
	public double heading() { return getValue(Ability.HEADING, ship.heading()); }
	public double getSizeBonus() { return getValue(Ability.SIZE, 1); }

	public void update()
    {
        if (percCooled > 0) {
            percCooled--;
            return;
        }

		if (fire) {
			for (Ability a : upgrades) {
				a.weaponFired(this);
            }

			if (!fire) {
				return;
            }

			int spread = getSpread();
			double heading = heading();

			if (spread <= 1) {
				fireProjectile(heading);
			} else {
				for (int i = 0; i < spread; i++) {
					fireProjectile(Math.toRadians(180 * ship.getOrientation() - (60 / (spread - 1) * i - 30)) + heading);
                }
			}

            percCooled = getCooldown();
		}
	}

	////////////
	// Setters

	public void setLocation(double x, double y)
    {
		this.x = x + rel.x;
		this.y = y + rel.y;
	}

	public void setMouseLocation(int x, int y)
    {
		mouse.x = x;
		mouse.y = y;
	}

    @Override
    public Ability upgradeAt(int index)
    {
        if (index >= 0 && index < upgrades.size()) {
            return upgrades.get(index);
        }
        return null;
    }

    @Override
    public void addUpgrade(Ability a)
    {
        if (upgrades.size() < amountSlots()) {
            upgrades.add(a);
        }
	}

    @Override
    public void removeUpgrade(int index)
    {
        if (index >= 0 && index < upgrades.size()) {
            Ability a = upgrades.get(index);
            if (a != null) {
                a.destroy();
                upgrades.remove(index);
            }
        }
    }

	// all attributes are int's (basically)
	// here's the chance to alter speed,damage,spread,cooldown,enemy damage,heading(?),etc
	protected double getValue(String attr, double n)
    {
		for (Ability a : upgrades) {
			n = a.getValue(this, attr, n);
        }
		return n;
	}

	public Ship ship() { return ship; }
	public OuterSpacePanel panel() { return ship().panel(); }
}
