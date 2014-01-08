package kipper;

// Individual particle matter
// Use to create explosions with many particles
public class Debris extends java.awt.Point {
	// angle to travel
	double theta;
	// distance from point of explosion	
	double dist;
	
	// @x					x coordinate
	// @y					y coordinate
	// @theta			trajectory
	// @dist			distance to travel
	public Debris(int x,int y,double theta,double dist){
		super(x,y);
		this.theta=theta;
		this.dist=dist;
	}
}