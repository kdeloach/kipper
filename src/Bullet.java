import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

public class Bullet implements Projectile, Runnable
{
	protected int width, height;
	private int speed, team;
	private double damage;
	protected double dx, dy;

	// id of bullet,is set by master panel
	protected int id;

	// bullet trajectory
	protected double theta;

	// master panel
	protected Weapon weapon;

	public Bullet(int x, int y, double t, double dmg, Weapon w)
    {
		this.weapon=w;
		this.theta=t;
		dx=x;
		dy=y;

		setLocation(x, y);

		if (getSize() != null)
			setSize(getSize().width, getSize().height);

		speed = getDefaultSpeed();
		damage = dmg;

		weapon.ship().panel().registerProjectile(this);

		new Thread(this).start();
	}

	final public void explode()
    {
		weapon.ship().panel().unregisterProjectile(this);
		die();
	}

	public void move()
    {
		setLocation(
			dx + Const.BULLET_SPEED * Math.cos(theta),
			dy + Const.BULLET_SPEED * Math.sin(theta)
		);
	}

	final public void run()
    {
		while (weapon.ship.panel().contains(getX(), getY()) && registered()) {
			Ship o = weapon.ship().panel().intersects(this);
			if (o != null && o.getId() != weapon.ship().getId()) {
				o.hit(getDamage(), getX(), getY());
				weapon.ship.target = o;
				explode();
				break;
			}
			move();
	 		try { Thread.sleep(getSpeed()); } catch (Exception ie) {}
		}
        weapon.ship().panel().unregisterProjectile(this);
	}

	public void draw(Graphics g)
    {
		g.setColor(Color.YELLOW);
		g.drawOval(getX(), getY(), width, height);
	}

	public void die()
    {
		new Explosion(getX(), getY(), panel());
	}

	public int getDefaultSpeed() { return 15; }
	public Dimension getSize() { return new Dimension(1, 1); }
	public int getDefaultTeam() { return Const.PLAYER; }

	final public boolean registered(){ return id!=Const.UNREGISTERED; }
	final public double getDamage(){ return damage; }
	final public int getSpeed(){ return speed; }
	final public int getTeam(){ return team; }
	final public int getX(){ return (int)dx; }
	final public int getY(){ return (int)dy; }
	final public int getId(){ return id; }
	final Rectangle getRectangle(){ return new Rectangle(getX() - width / 2, getY() - height / 2, width, height); }

	public boolean intersects(Ship s)
    {
		return s.intersects(getRectangle());
	}

	final public boolean intersects(Projectile p)
    {
		return p.contains(getX(), getY());
	}

	final public boolean contains(double x, double y)
    {
		return contains((int)x, (int)y);
	}

	final public boolean contains(int x, int y)
    {
		return getRectangle().contains(x,y);
	}

	final public void setSize(int w,int h) { this.width = w; this.height = h; }
	public void setLocation(int x,int y){ setLocation((double)x, (double)y); }
	public void setLocation(double x,double y){ dx = x; dy = y; }
	final public void setId(int k){ id = k; }

	final public OuterSpacePanel panel() { return weapon().ship().panel(); }
	final public Weapon weapon(){ return weapon; }
	final public Ship ship(){ return weapon().ship(); }
}