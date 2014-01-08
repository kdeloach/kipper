import java.awt.*;
import java.awt.event.*;

public class ShipUpgradeScreen extends Scene {

	Ship player;
	OuterSpacePanel osp;

	final int weapons = 0;
	final int upgrades = 1;
	final int slots = 2;

	int selected=Const.NONE;
	int selectedIndex=Const.NONE;
	Point mouse=new Point();

	Rectangle[][] hotspots = new Rectangle[3][];

    private Font f = new Font("Arial", Font.PLAIN, 16);

	public ShipUpgradeScreen(OuterSpacePanel osp){
		this.osp=osp;
		player = osp.getPlayer();

		//

		hotspots[weapons] = new Rectangle[player.wpnList.length];
		for(int i=0;i<hotspots[weapons].length;i++)
			hotspots[weapons][i] = new Rectangle(10+50*i, osp.getHeight()-50, 40, 40);

		hotspots[slots] = new Rectangle[player.getSlotsAmt()];
		for(int i=0;i<hotspots[slots].length;i++)
			hotspots[slots][i] = new Rectangle(player.width+50+100*i, 20, 90, 90);

		// THERE  ARE ONLY 5 UPGRADES ATM
		int w = 140, h = 50, p = 5;
		hotspots[upgrades] = new Rectangle[Const.UPGRADES_QT];
		for(int i=0,y=150,x=0;i<hotspots[upgrades].length;i++,x++){
			if(player.width+50+(w+p)*x+w >= osp.getWidth()){
				x=0; y+=h+p;
			}
			hotspots[upgrades][i] = new Rectangle(player.width+50+(w+p)*x, y, w, h);
		}
	}

    public void createScene() {
		player.releaseControl();
		player.move(player.getWidth()/2+10,player.getHeight()/2+20);
		osp.addMouseListener(this);
		osp.addMouseMotionListener(this);
    }

    public void destroyScene() {
		osp.removeMouseListener(this);
		osp.removeMouseMotionListener(this);
    }

	public void paint(Graphics g){
		g.setFont(f);

		// draw empty slots
		g.setColor(Color.GRAY);
		for(int i=0;i<hotspots[slots].length;i++)
			g.fillRoundRect(hotspots[slots][i].x,hotspots[slots][i].y,hotspots[slots][i].width,hotspots[slots][i].height,15,15);

		for(int i=0;i<hotspots[slots].length;i++){
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(f);
			g.drawString("Slot "+(i+1), hotspots[slots][i].x+8*3+2, hotspots[slots][i].y+hotspots[slots][i].height/2);
			//g.drawRoundRect(hotspots[slots][i].x,hotspots[slots][i].y,hotspots[slots][i].width,hotspots[slots][i].height,15,15);

			// now fill in those slots if we can
			if(player.upgrades.get(i)!=null){
				Ability a = player.upgrades.get(i);
				if(a instanceof SpeedAbility){
					SpeedAbility.drawIcon(g,hotspots[slots][i].x,hotspots[slots][i].y,hotspots[slots][i].width,hotspots[slots][i].height);
				} else if(a instanceof DamageAbility){
					DamageAbility.drawIcon(g,hotspots[slots][i].x,hotspots[slots][i].y,hotspots[slots][i].width,hotspots[slots][i].height);
				} else if(a instanceof SpreadAbility){
					SpreadAbility.drawIcon(g,hotspots[slots][i].x,hotspots[slots][i].y,hotspots[slots][i].width,hotspots[slots][i].height);
				} else if(a instanceof RecoilAbility){
					RecoilAbility.drawIcon(g,hotspots[slots][i].x,hotspots[slots][i].y,hotspots[slots][i].width,hotspots[slots][i].height);
				} else if(a instanceof RotateAbility){
					RotateAbility.drawIcon(g,hotspots[slots][i].x,hotspots[slots][i].y,hotspots[slots][i].width,hotspots[slots][i].height);
				}
			}
		}

		// draw icons for the upgrades...manually...
		for(int i=0;i<hotspots[upgrades].length;i++){
			Rectangle r = hotspots[upgrades][i];

			switch(i){
				case Const.SPEED:
					SpeedAbility.drawIcon(g,r.x,r.y,r.width,r.height);
					break;
				case Const.DAMAGE:
					DamageAbility.drawIcon(g,r.x,r.y,r.width,r.height);
					break;
				case Const.SPREAD:
					SpreadAbility.drawIcon(g,r.x,r.y,r.width,r.height);
					break;
				case Const.RECOIL:
					RecoilAbility.drawIcon(g,r.x,r.y,r.width,r.height);
					break;
				case Const.ROTATE:
					RotateAbility.drawIcon(g,r.x,r.y,r.width,r.height);
					break;
			}
		}

		// draw the icons for the weapons
		// how many weapons are there

		for(int i=0;i<hotspots[weapons].length;i++){
			if(player.wpnList[i]!=null)
				g.drawImage(player.wpnList[i].getIcon(),hotspots[weapons][i].x,hotspots[weapons][i].y,hotspots[weapons][i].width,hotspots[weapons][i].height, osp);
		}

		// draw currently selected item
		if(selected==weapons){
			Rectangle r = hotspots[weapons][selectedIndex];
			g.drawImage(player.wpnList[selectedIndex].getIcon(),mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height, osp);
		} else if (selected==upgrades) {
			Rectangle r = hotspots[upgrades][selectedIndex];
			switch(selectedIndex){
				case Const.SPEED:
					SpeedAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
					break;
				case Const.DAMAGE:
					DamageAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
					break;
				case Const.SPREAD:
					SpreadAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
					break;
				case Const.RECOIL:
					RecoilAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
					break;
				case Const.ROTATE:
					RotateAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
					break;
			}
		} else if (selected==slots) {
			Rectangle r = hotspots[slots][selectedIndex];
			//if(player.upgrades.get(i)!=null){
				Ability a = player.upgrades.get(selectedIndex);
				if(a instanceof SpeedAbility){
					SpeedAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
				} else if(a instanceof DamageAbility){
					DamageAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
				} else if(a instanceof SpreadAbility){
					SpreadAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
				} else if(a instanceof RecoilAbility){
					RecoilAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
				} else if(a instanceof RotateAbility){
					RotateAbility.drawIcon(g,mouse.x-r.width/2,mouse.y-r.height/2,r.width,r.height);
				}
			//}
		}
	}

	/////

	public void mousePressed(MouseEvent evt){
		mouse.setLocation(evt.getPoint());

		// if a weapon is selected
		for(int i=0;i<hotspots[weapons].length;i++){
			if(hotspots[weapons][i].contains(mouse)&&player.wpnList[i]!=null){
				selected=weapons;
				selectedIndex=i;
				return;
			}
		}

		// if an upgrade is selected
		for(int i=0;i<hotspots[upgrades].length;i++){
			if(hotspots[upgrades][i].contains(mouse)){
				selected=upgrades;
				selectedIndex=i;
				return;
			}
		}

		// if a slot is selected
		for(int i=0;i<hotspots[slots].length;i++){
			if(hotspots[slots][i].contains(mouse)){
				selected=slots;
				selectedIndex=i;
				return;
			}
		}
	}
	public void mouseReleased(MouseEvent evt){
		mouse.setLocation(evt.getPoint());

		if(selected==weapons){
			for(int i=0;i<hotspots[weapons].length;i++){
				if(hotspots[weapons][i].contains(mouse)){
					Weapon a = player.wpnList[i];
					player.wpnList[i] = player.wpnList[selectedIndex];
					player.wpnList[selectedIndex] = a;
					break;
				}
			}
		} else if (selected==upgrades) {
			for(int i=0;i<hotspots[slots].length;i++){
				if(hotspots[slots][i].contains(mouse)){
					switch(selectedIndex){
						case Const.SPEED:
							player.upgrades.set(i, new SpeedAbility(player));
							break;
						case Const.DAMAGE:
							player.upgrades.set(i, new DamageAbility(player));
							break;
						case Const.SPREAD:
							player.upgrades.set(i, new SpreadAbility(player));
							break;
						case Const.RECOIL:
							player.upgrades.set(i, new RecoilAbility(player));
							break;
						case Const.ROTATE:
							player.upgrades.set(i, new RotateAbility(player));
							break;
					}
					break;
				}
			}
		} else if (selected==slots){
			for(int i=0;i<hotspots[slots].length;i++){
				if(hotspots[slots][i].contains(mouse)){
					Ability a = player.upgrades.get(i);
					player.upgrades.set(i, player.upgrades.get(selectedIndex));
					player.upgrades.set(selectedIndex, a);

					selected=Const.NONE;
					selectedIndex=Const.NONE;
					return;
				}
			}
			player.upgrades.set(selectedIndex, null);
		}

		selected=Const.NONE;
		selectedIndex=Const.NONE;
	}
	public void mouseDragged(MouseEvent evt){
		mouse.setLocation(evt.getPoint());
	}

    public String name() {
        return "upgrade";
    }
}