import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Point2D;

// LaserBeams are basically 2 points, whereas the first point is x,y
//   and the second point is x+length*cos(theta),y+length*sin(theta)

public abstract class LaserBeam implements Projectile, Runnable {

	protected int length;

	private int speed;

	// id of bullet, is set by master panel
	protected int id;

	private double damage;

	// bullet trajectory
	protected double theta;

	// start and end of beam
	protected Point2D.Double start, stop, contact;

	// master panel
	protected Weapon weapon;

	/////

	public LaserBeam(int x, int y, double t, double dmg, Weapon w){
		this.weapon=w;
		this.theta=t;

		speed=getDefaultSpeed();
		this.length=getLength();
		damage=dmg;

		start=new Point2D.Double(x, y);
		stop=new Point2D.Double(x+length*Math.cos(theta), y+length*Math.sin(theta));

		setLocation(x,y);
		weapon.ship.panel().registerProjectile(this);

		new Thread(this).start();
	}

	///////

	final public void explode(){
		weapon.ship.panel().unregisterProjectile(this);
		die();
	}
	final public void move(){
		start.x += Const.BULLET_SPEED*Math.cos(theta);
		start.y += Const.BULLET_SPEED*Math.sin(theta);
		stop.x  += Const.BULLET_SPEED*Math.cos(theta);
		stop.y  += Const.BULLET_SPEED*Math.sin(theta);
	}
	final public void run(){
		while(weapon.ship().panel().contains(getX(),getY())){

			Ship o=weapon.ship.panel().intersects(this);
			if(o!=null&&o.getId()!=weapon.ship().getId()){
				o.hit(getDamage(),(int)contact.x,(int)contact.y);
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
	abstract public int getLength();
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
	final public int getX(){
		return (int)start.x;
	}
	final public int getY(){
		return (int)start.y;
	}
	final public int getId(){
		return id;
	}

	final public boolean intersects(Ship s){
		if(s.contains((int)start.x, (int)start.y)){
			contact=start;
			return true;
		} else if (s.contains((int)stop.x, (int)stop.y)){
			contact=stop;
			return true;
		}
		return false;
	}

    public boolean intersects(Projectile p) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean contains(int x, int y) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean contains(double x, double y) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

	///////
	// Setters

	private void setLocation(int x,int y){
		setLocation((double)x, (double)y);
	}
	final void setLocation(double x,double y){
		start.setLocation(x,y);
		stop.setLocation(x+length*Math.cos(theta),y+length*Math.sin(theta));
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