import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;

// REASON this doesnt implement Destructable is because pellets are week
// rockets on the other hand, MAY or MAY NOT be destructable

public abstract class Bullet implements Projectile, Runnable {
	protected int width, height;
	
	private int speed, team;
	
	private double damage;
	
	// id of bullet,is set by master panel
	protected int id;
	
	// double x, double y; higher accuracy
	protected double dx, dy;
	
	// bullet trajectory
	protected double theta;
	
	// master panel
	protected Weapon weapon;

	/////
		
	public Bullet(int x, int y, double t, double dmg, Weapon w){
		this.weapon=w;
		this.theta=t;
		dx=x;
		dy=y;
			
		setLocation(x, y);
		if(getSize()!=null)
			setSize(getSize().width,getSize().height);
			
		//width=getSize().width;
		//height=getSize().height;
		speed=getDefaultSpeed();
		damage=dmg;
			
		weapon.ship().panel().registerProjectile(this);
			
		new Thread(this).start();
	}
	
	///////
	
	final public void explode(){
		weapon.ship().panel().unregisterProjectile(this);
		die();
	}
	public void move(){
		setLocation(
			dx + Const.BULLET_SPEED*Math.cos(theta),
			dy + Const.BULLET_SPEED*Math.sin(theta)
		);
	}
	final public void run(){
		while(weapon.ship.panel().contains(getX(),getY()) && registered()){
			
			Ship o=weapon.ship().panel().intersects(this);
			if(o!=null&&o.getId()!=weapon.ship().getId()){
				o.hit(getDamage(),getX(),getY());
				weapon.ship.target=o;
				explode();
				break;
			}
			
			move();
			
			try{Thread.sleep(getSpeed());}catch(Exception ie){}
		}
		
		// if it didn't hit anything unregister before ending thread
		if(registered())
			weapon.ship().panel().unregisterProjectile(this);
	}
	
	///////
	// Abstracts
	
	abstract public int getDefaultSpeed();
	abstract public int getDefaultTeam();
	abstract public Dimension getSize();
	abstract public void draw(Graphics g);
	abstract public void die();
	
	///////
	// Getters
	
	final public boolean registered(){
		return id!=Const.UNREGISTERED;	
	}
	final public double getDamage(){
		return damage;	
	}
	final public int getSpeed(){
		return speed;	
	}
	final public int getTeam(){
		return team;	
	}
	final public int getX(){
		return (int)dx;
	}
	final public int getY(){
		return (int)dy;
	}
	final public int getId(){
		return id;
	}
	final Rectangle getRectangle(){
		return new Rectangle(getX()-width/2,getY()-height/2,width,height);
	}

	public boolean intersects(Ship s){
		return s.intersects(getRectangle());	
	}
	final public boolean intersects(Projectile p){
		//return getRectangle().intersects(p.getX(),p.getY(),1,1);
		return p.contains(getX(),getY());
	}
	final public boolean contains(double x, double y){
		return contains((int)x,(int)y);	
	}
	final public boolean contains(int x, int y){
		// lazy
		return getRectangle().contains(x,y);
	}
	
	///////
	// Setters
	
	final public void setSize(int w,int h){
		this.width=w;
		this.height=h;
	}
	public void setLocation(int x,int y){
		setLocation((double)x, (double)y);
	}
	public void setLocation(double x,double y){
		dx=x;
		dy=y;
	}
	final public void setId(int k){
		id=k;
	}
	
	///////
	
	final public OuterSpacePanel panel(){
		return weapon().ship().panel();		
	}
	final public Weapon weapon(){
		return weapon;		
	}
	final public Ship ship(){
		return weapon().ship();		
	}
}