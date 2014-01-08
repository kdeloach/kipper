import java.awt.Rectangle;
import java.awt.Point;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

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

public abstract class Weapon implements Upgradeable, Runnable {
	
	protected int x, y, width, height;
	
	// experience of gun, used to determine no. of slots 
	protected int exp;
	
	// (default) damage this baby can do
	private int damage;
	
	// spread is the amount of bullets you can shoot at once
	// for some added appeal the game should 'split' the stream of bullets by altering the heading()
	private int spread=1;
	
	// percent cooled down
	protected int percCooled=0;
	
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
	protected ArrayList<Ability> natural, upgrades;
	
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
		
		natural=new ArrayList<Ability>();
		upgrades=new ArrayList<Ability>();
		listeners = new ArrayList<WeaponListener>();
		
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
	
	final public void startFiring(){
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
	final public void stopFiring(){
		fire=false;	
	}
	final public void run(){
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

	final void setSize(int w,int h){
			this.width=w;
			this.height=h;
	}
	final public void setLocation(int x,int y){
		this.x=x+rel.x;
		this.y=y+rel.y;
	}
	final public void setMouseLocation(int x,int y){
		mouse.x=x;
		mouse.y=y;
	}
	final public void setCooldown(int n){
		cooldown=n;	
	}
	final protected void setSpread(int n){
		if(n<Const.MIN_SPREAD||n>Const.MAX_SPREAD)
			throw new IllegalArgumentException("Cannot set spread to "+n+".  Acceptable values are ["+Const.MIN_SPREAD+"-"+Const.MAX_SPREAD+"]");
		
		spread=n;
	}
	
	// LATER: check if exp is enough to levelUp()
	final public void addExperience(int xp){
		exp+=xp;
	}
	
	private int abid=0;
	
	final public void addUpgrade(Ability a){
		addAbility(upgrades,a);
	}
	final public void addNaturalAbility(Ability a){
		addAbility(natural,a);
	}
	final private void addAbility(ArrayList<Ability> l, Ability a){
		a.setId(abid++);
		upgrades.add(a);
	}
	final public void addListener(Listener w){
		listeners.add((WeaponListener)w);
	}
	final public void addWeaponListener(WeaponListener l){
		addListener(l);	
	}
	final public void addShipListener(ShipListener l){
		ship.addListener(l);	
	}
	
	final public void removeAbility(Ability a){
		for(int i=0;i<upgrades.size();i++){
			if(upgrades.get(i).getId()==a.getId()){
				upgrades.remove(i);
				break;	
			}
		}
	}
	
	////////////
	// Getters
	
	final public int getCooldown(){
		return (int)requestAttribute(Ability.COOLDOWN, cooldown);
	}
	final public double getDamage(){
		return requestAttribute(Ability.DAMAGE, damage);
	}
	final public int getSpread(){
		return (int)requestAttribute(Ability.SPREAD, spread);
	}
	final public int getExperience(){
		return exp;
	}
	final public double percentCooled(){
		return (double)percCooled/(double)getCooldown();
	}
	final public int amountSlots(){
		return 0;
	}
	final public double heading(){
		return requestAttribute(Ability.HEADING, ship.heading());
	}
	
	final public boolean isFiring(){
		return fire;	
	}
	
	// all attributes are int's (basically)
	// here's the chance to alter speed,damage,spread,cooldown,enemy damage,heading(?),etc
	final private double requestAttribute(String attr, double n){
		for(WeaponListener a : listeners)
			n=a.attributeCalled(attr,n);
		return n;
	}
	
	////////////
	
	final public Ship ship(){
		return ship;		
	}
	final public OuterSpacePanel panel(){
		return ship().panel();		
	}
}