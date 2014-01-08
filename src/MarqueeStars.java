package kipper;

import java.awt.Point;
import java.awt.geom.Point2D;

public class MarqueeStars extends Thread {
	Point[] starsfg;
	OuterSpacePanel osp;
	public MarqueeStars(Point[] starsfg,OuterSpacePanel c){
		this.starsfg=starsfg;
		this.osp=c;
	}
	public void run(){
		double angle = Math.toRadians(125);
		
		while(true){

			// move stars
			for(int i=0;i<starsfg.length;i++){
				starsfg[i].x+=Math.cos(angle)*3;
				starsfg[i].y+=Math.sin(angle)*3;
				
				// if a star goes out of bounds reset
				if(starsfg[i].x < 0)
					starsfg[i]=new Point(OuterSpacePanel.WIDTH, (int)(Math.random()*OuterSpacePanel.HEIGHT));
				if(starsfg[i].x > OuterSpacePanel.WIDTH)
					starsfg[i]=new Point(0, (int)(Math.random()*OuterSpacePanel.HEIGHT));
					
				if(starsfg[i].y > OuterSpacePanel.HEIGHT)
					starsfg[i]=new Point((int)(Math.random()*OuterSpacePanel.WIDTH), 0);
				if(starsfg[i].y < 0)
					starsfg[i]=new Point((int)(Math.random()*OuterSpacePanel.WIDTH), OuterSpacePanel.HEIGHT);
			}
			
			try { Thread.sleep(100); } catch (Exception ie){}		
		}
	}	
}