import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Asteroid {
	int x;
	int y;
	int xChange=0;
	int yChange=0;
	int dir;
	double roidCount=0;
	Rectangle hitBox;
	BufferedImage roids;
	BufferedImage[] roid=new BufferedImage[64];
	MainGame main;
	
	public Asteroid(int xPos,int yPos, int dir, MainGame main, BufferedImage[] roid){
		x=xPos;
		y=yPos;
		this.dir=dir;
		this.main=main;
		
		if(dir==0) {
			while(xChange==0 && yChange==0) {
				xChange=(int)(Math.random()*6);
				yChange=(int)(Math.random()*6)-3;
			}
		}
		else if(dir==1) {
			while(xChange==0 && yChange==0) {
				xChange=-(int)(Math.random()*6);
				yChange=(int)(Math.random()*6)-3;
			}
		}
		else if(dir==2) {
			while(xChange==0 && yChange==0) {
				xChange=(int)(Math.random()*6)-3;
				yChange=(int)(Math.random()*6);
			}
		}
		else if(dir==3) {
			while(xChange==0 && yChange==0) {
				xChange=(int)(Math.random()*6)-3;
				yChange=-(int)(Math.random()*6);
			}
		}
		
		this.roid=roid;
		
		hitBox=new Rectangle(x,y,roid[(int)roidCount].getWidth(),roid[(int)roidCount].getHeight());
		
	}
	
	public void move() {
			x+=xChange;
			y+=yChange;
			roidCount+=0.1;
			hitBox=new Rectangle(x+20,y+20,roid[(int)roidCount%64].getWidth()-50,roid[(int)roidCount%64].getHeight()-50);
	}
	
	
	public void paint(Graphics2D g2d) {
		move();
		g2d.drawImage(roid[(int)roidCount%64], x, y,null);
		//g2d.setColor(Color.RED);
		//g2d.draw(hitBox);
	}
	
	public boolean inFrame() {
		if(x<-200 || x>main.frame.getWidth()+200)
			return false;
		else if(y<-200 || y>main.frame.getHeight()+200)
			return false;
		return true;
	}
	
}
