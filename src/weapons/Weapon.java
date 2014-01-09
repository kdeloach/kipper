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

public abstract class Weapon implements Upgradable, Runnable {

	protected int x, y, width, height;

	// experience of gun, used to determine no. of slots
	protected int exp;

	// (default) damage this baby can do
	private int damage;

	// spread is the amount of bullets you can shoot at once
	// for some added appeal the game should 'split' the stream of bullets by altering the heading()
	private int spread=1;

	// percent cooled down
	public int percCooled=0;

	// time to cooldown
	private int cooldown;

	// control variable,keep shooting gun while true
	// also a control variable to check if thread is still active
	protected boolean fire=false, fireThreadActive=false;

	// position relative to Ship x&y,this is only set once
	protected Point rel;

	// position of the mouse
	protected Point mouse;

	// list of upgrades
	protected ArrayList<Ability> upgrades;

	private ArrayList<WeaponListener> listeners;

	// owner
	Ship ship;

	////////////

	public Weapon(int x, int y, int rx, int ry, Ship s){
		this.x=x+rx;
		this.y=y+ry;
		this.ship=s;
		rel=new Point(rx,ry);

		setLocation(x,y);

		upgrades=new ArrayList<Ability>();
		listeners = new ArrayList<WeaponListener>();

        for (int i = 0; i < amountSlots(); i++) {
            upgrades.add(null);
        }

		mouse=new Point();
		cooldown=getDefaultCooldown();
		damage=getDefaultDamage();
	}

	////////////

	abstract public void fireProjectile(double heading);
	abstract public void draw(Graphics g);
	abstract public Image getIcon();
	abstract public int getDefaultCooldown();
	abstract public int getDefaultDamage();

	////////////

	public void startFiring(){
		// if user releases the mouse real quick,they can
		// resume shooting without starting a new thread
		fire=true;

		// if firethread is active,dont start a new thread
		if(fireThreadActive)
			return;

		new Thread(this).start();
		fire=true;
		fireThreadActive=true;
	}
	public void stopFiring(){
		fire=false;
	}
	public void run(){
		while(fire){

			// call every listener
			for(WeaponListener a : listeners)
				a.weaponFired(this);

			if(!fire)
				return;

			// lay down the law
			int n = getSpread();
			double h = heading();

			if(n<=1){
				fireProjectile( h );
			} else {
				for(int i=0;i<n;i++)
					fireProjectile(Math.toRadians( 180*ship.getOrientation()-(90/(n-1)*i-45) ) + h );
			}

			// catch your breathe
			while(percCooled<getCooldown()){
				try{ Thread.sleep(getCooldown()/10); } catch (Exception ie) {}
				percCooled+=10;
			}
			percCooled=0;
		}

		fireThreadActive=false;
	}

	////////////
	// Setters

	void setSize(int w,int h){
			this.width=w;
			this.height=h;
	}
	public void setLocation(int x,int y){
		this.x=x+rel.x;
		this.y=y+rel.y;
	}
	public void setMouseLocation(int x,int y){
		mouse.x=x;
		mouse.y=y;
	}
	public void setCooldown(int n){
		cooldown=n;
	}
	protected void setSpread(int n){
		if(n<Const.MIN_SPREAD||n>Const.MAX_SPREAD)
			throw new IllegalArgumentException("Cannot set spread to "+n+".  Acceptable values are ["+Const.MIN_SPREAD+"-"+Const.MAX_SPREAD+"]");

		spread=n;
	}

	// LATER: check if exp is enough to levelUp()
	public void addExperience(int xp){
		exp+=xp;
	}

    @Override
    public void addUpgrade(Ability a)
    {
        for (int i = 0; i < amountSlots(); i++) {
            if (upgrades.get(i) == null) {
                upgrades.set(i, a);
                return;
            }
        }
	}

    @Override
    public void removeUpgrade(int index)
    {
        if (index >= 0 && index < amountSlots()) {
            upgrades.set(index, null);
        }
    }

    @Override
	public void addWeaponListener(WeaponListener l)
    {
        listeners.add(l);
	}

    @Override
	public void removeWeaponListener(WeaponListener l)
    {
        for (WeaponListener listener : listeners) {
            if (listener == l) {
                listeners.remove(l);
                return;
            }
        }
	}

    @Override
	public void addShipListener(ShipListener l) { throw new UnsupportedOperationException(); }

	public int getCooldown() { return (int)requestAttribute(Ability.COOLDOWN, cooldown); }
	public double getDamage() { return requestAttribute(Ability.DAMAGE, damage); }
	public int getSpread() { return (int)requestAttribute(Ability.SPREAD, spread); }
	public int getExperience() { return exp; }
	public double percentCooled() { return (double)percCooled/(double)getCooldown(); }
	public int amountSlots() { return 6; }
	public double heading() { return requestAttribute(Ability.HEADING, ship.heading()); }
	public boolean isFiring() { return fire; }

	// all attributes are int's (basically)
	// here's the chance to alter speed,damage,spread,cooldown,enemy damage,heading(?),etc
	private double requestAttribute(String attr, double n)
    {
		for (WeaponListener a : listeners) {
			n = a.attributeCalled(attr, n);
        }
		return n;
	}

	public Ship ship() { return ship; }
	public OuterSpacePanel panel() { return ship().panel(); }
}
