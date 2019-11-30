import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AnchorPoint{
	String type;
	int x;
	int y;
	int xChange=0;
	int yChange=0;
	int dir;
	MainGame main;
	boolean mover=true;
	Rectangle hitBox;
	BufferedImage img;
	
	
	public AnchorPoint(int xPos,int yPos, MainGame main, int dir){
		int rand=(int)(Math.random()*100);
		if (rand<10)
			type="fuel";
		else if(rand<25)
			type="metal";
		else if(rand<40)
			type="rocket fuel";
		else 
			type="norm";
		x=xPos;
		y=yPos;
		this.main=main;
		this.dir=dir;
		mover=true;
		xChange=(int)(Math.random()*5)-2;
		yChange=(int)(Math.random()*5)-2;
		
		if(dir==0) {
			while(xChange==0 && yChange==0) {
				xChange=(int)(Math.random()*5);
				yChange=(int)(Math.random()*5)-2;
			}
		}
		else if(dir==1) {
			while(xChange==0 && yChange==0) {
				xChange=-(int)(Math.random()*5);
				yChange=(int)(Math.random()*5)-2;
			}
		}
		else if(dir==2) {
			while(xChange==0 && yChange==0) {
				xChange=(int)(Math.random()*5)-2;
				yChange=(int)(Math.random()*5);
			}
		}
		else if(dir==3) {
			while(xChange==0 && yChange==0) {
				xChange=(int)(Math.random()*5)-2;
				yChange=-(int)(Math.random()*5);
			}
		}
		
		
		if(type.equals("norm")) {
		try{
			int rando=(int)(Math.random()*4)+1;
			img=ImageIO.read(new File("src/warped_metal_"+rando+".png"));
		}
		catch(IOException e){e.printStackTrace();}}
		
		else if(type.equals("fuel")) {
			try{
				img=ImageIO.read(new File("src/Goods Icons.png"));
			}
			catch(IOException e){System.out.println("FUEL ERR");}}
		
		else if(type.equals("metal")) {
			try{
				img=ImageIO.read(new File("src/metal.png"));
			}
			catch(IOException e){System.out.println("METAL ERR");}}
		else if(type.equals("rocket fuel")) {
			try{
				img=ImageIO.read(new File("src/fuel cell.png"));
			}
			catch(IOException e){System.out.println("RKT FUEL ERR");}}
		
		hitBox=new Rectangle(x,y,img.getWidth(),img.getHeight());
		
	}
	
	public void move() {
		if (mover) {
			x+=xChange;
			y+=yChange;
			hitBox=new Rectangle(x,y,img.getWidth(),img.getHeight());
		}
	}
	
	
	
	public void paint(Graphics2D g2d) {
		move();
		g2d.drawImage(img, x, y,null);
		if(!type.equals("norm")) {
			g2d.setStroke(new BasicStroke(10));
			g2d.setColor(Color.BLUE);
			g2d.draw(hitBox);
		}
	}
	
	public boolean inFrame() {
		if(x<-200 || x>main.frame.getWidth()+200)
			return false;
		else if(y<-200 || y>main.frame.getHeight()+200)
			return false;
		return true;
	}
	
	
}
