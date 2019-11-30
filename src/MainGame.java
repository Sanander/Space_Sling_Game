import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


import java.awt.Rectangle;

import java.awt.geom.AffineTransform;

public class MainGame extends JPanel implements Runnable, MouseListener, ActionListener
{
	private double x;
	private double y;
	JFrame frame;
	Thread t;
	private boolean gameOn;
	BufferedImage guy;
	BufferedImage[] guys=new BufferedImage[11];
	//BufferedImage hook;
	//BufferedImage[] hooks=new BufferedImage[2];
	BufferedImage openHook;
	BufferedImage closeHook;
	BufferedImage[] airs=new BufferedImage[40];
	BufferedImage introShip;
	BufferedImage air;
	boolean restart=false;
	int imgCount=0;
	
	boolean w;
	boolean s;
	boolean a;
	boolean d;
	int wCount=0;
	int sCount=0;
	int aCount=0;
	int dCount=0;
	
	double time=0;
	double wTime=0;
	double aTime=0;
	double sTime=0;
	double dTime=0.5;

	double imgAngle;
	double shootAngle=0.0;

	boolean shooting=false;
	boolean goodAim;
	boolean ropeDrawn=false;
	boolean floating=false;
	double finalAimX;
	double finalAimY;
	double shootSlope;
	int tempX;
	int tempY;
	
	int imgX;
	int imgY;
	int fuel=800;
	int condition=10;
	int safeCount=0;
	
	int rFuelCount=0;
	int metalCount=0;
	int rFuelGoal=5;
	int metalGoal=5;
	
	boolean refilling;
	int rectX;
	int shootDistance;
	int r=0;
	int introXChange;
	
	boolean right;
	BufferedImage back;
	BufferedImage gameOver;
	boolean death=false;
	boolean win=false;

	boolean playerHit=false;
	Rectangle playerBox;
	
	BufferedImage roids;
	BufferedImage [] roid=new BufferedImage[64];
	
	BufferedImage splode;
	BufferedImage [] explosions=new BufferedImage[13];
	
	ArrayList <AnchorPoint> apList=new ArrayList<>();
	ArrayList <Asteroid> asList=new ArrayList<>();
	
	InputMap iMap=getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	ActionMap aMap=getActionMap();
	Clip collectSound;
	Clip refillSound;
	Clip clip;
	Clip hurtSound;
	
	BufferedImage title;
	BufferedImage monitor;
	BufferedImage fuelCell;
	BufferedImage metal;
	BufferedImage instructions;
	BufferedImage winText;
	BufferedImage continueText;
	BufferedImage restartText;
	BufferedImage surviveShip;
	
	JButton start;
	JPanel p2;
	JButton howToPlay;
	JPanel p3;
	boolean showInstructions;
	boolean startAnimation=false;
	boolean asteroidAnimation=false;
	boolean explosionAnimation=false;
	boolean floatAnimation=false;
	boolean transitionAnimation=false;
	boolean animateUp;
	
	double vertAnimation;
	double roidCount=0.0;
	double explodeCount=0.0;
	

	public MainGame()
	{
		frame=new JFrame();
		x=100;
		y=500;
		gameOn=false;
		
		try{
			title=ImageIO.read(new File("src/title.png"));
			introShip=ImageIO.read(new File("src/intro_ship.png"));
			roids = ImageIO.read(new File("src/asteroid_01_no_moblur.png"));
			for(int x=0;x<64;x++)
				roid[x]=roids.getSubimage((x%8)*128,0,128,128);
			splode=ImageIO.read(new File("src/explosion.png"));
			for(int i=0;i<13;i++)
				explosions[i]=splode.getSubimage(i*194, 0, 194, 194);
			metal=ImageIO.read(new File("src/metal.png"));
			fuelCell=ImageIO.read(new File("src/fuel cell.png"));
			instructions=ImageIO.read(new File("src/instructions.png"));
			winText=ImageIO.read(new File("src/win.png"));
			continueText=ImageIO.read(new File("src/continue.png"));
			restartText=ImageIO.read(new File("src/restart.png"));
			surviveShip=ImageIO.read(new File("src/escape pod.png"));
		}
		catch(IOException e){e.printStackTrace();}
		
		try{
    	     clip = AudioSystem.getClip();
    	     clip.open(AudioSystem.getAudioInputStream(new File("src/David_Bowie_-_Starman_8-bit_Mp3Converter.net_.wav")));
    	     clip.loop(Clip.LOOP_CONTINUOUSLY);
    	     clip.start( ); 
    	     collectSound=AudioSystem.getClip();
    	     collectSound.open(AudioSystem.getAudioInputStream(new File("src/146723__fins__coin-object.wav")));
    	     refillSound=AudioSystem.getClip();
    	     refillSound.open(AudioSystem.getAudioInputStream(new File("src/Health-Potion-and-Magic-Drink-Sound-Effects-High-Quality-Free-Download.wav")));
    	     refillSound.setMicrosecondPosition(2200000);
    	     hurtSound=AudioSystem.getClip();
    	     hurtSound.open(AudioSystem.getAudioInputStream(new File("src/oww-ow-Sound-Effect.wav")));
    	     hurtSound.setMicrosecondPosition(500000);
    	    }
    	   catch(Exception ex)
    	   {ex.printStackTrace();  }
		
		
		try {
			guy = ImageIO.read(new File("src/char.png"));
			for(int x=0;x<1;x++)
				guys[x]=guy.getSubimage(x*81,0,81,81);
		}
		catch (IOException e) {
		}
		
		try {
			openHook=ImageIO.read(new File("src/open claw.png"));
			closeHook=ImageIO.read(new File("src/close claw.png"));
			monitor=ImageIO.read(new File("src/monitor.png"));
		}
		catch (IOException e) {System.out.println("dfvdv");
		}
		
		try {
			air = ImageIO.read(new File("src/smoke_1_40_128_corrected.png"));
			int ct=0;
			for(int y=0;y<5;y++) {
				for(int x=0;x<8;x++) {
					airs[ct]=air.getSubimage((x)*128,y*128,128,128);
					ct++;
				}
			}
		}
		catch (IOException e) {System.out.println("dfvdv");
		}
		
		try{
			back=ImageIO.read(new File("src/xChy58V.png"));
			gameOver=ImageIO.read(new File("src/dead.png"));
		}
		catch(IOException e){}

		frame.add(this);
		
		iMap.put(KeyStroke.getKeyStroke("W"),"up");
		aMap.put("up",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(!w)
					wCount=0;
				w=true;
				//ropeDrawn=false; ALLOWS YOU TO STOP GRAPPLE
			}
		});
		iMap.put(KeyStroke.getKeyStroke("S"),"down");
		aMap.put("down",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(!s)
					sCount=0;
				s=true;
			}
		});
		iMap.put(KeyStroke.getKeyStroke("A"),"left");
		aMap.put("left",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(!a)
					aCount=0;
				a=true;
			}
		});
		iMap.put(KeyStroke.getKeyStroke("D"),"right");
		aMap.put("right",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(!d)
					dCount=0;
				d=true;
			}
		});
		
		iMap.put(KeyStroke.getKeyStroke("released W"),"released up");
		aMap.put("released up",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				w=false;
			}
		});
		iMap.put(KeyStroke.getKeyStroke("released S"),"released down");
		aMap.put("released down",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				s=false;
			}
		});
		iMap.put(KeyStroke.getKeyStroke("released A"),"released left");
		aMap.put("released left",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				a=false;
			}
		});
		iMap.put(KeyStroke.getKeyStroke("released D"),"released right");
		aMap.put("released right",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				d=false;
			}
		});
		
		playerBox=new Rectangle((int)x,(int)y,guys[imgCount].getWidth(),guys[imgCount].getHeight());
		
		start=new JButton("START");
		p2=new JPanel();
		
		howToPlay=new JButton("INSTRUCTIONS");
		p3=new JPanel();

		frame.setSize(800,500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addMouseListener(this);
		setVisible(true);
		imgX=0;	
			
		this.setLayout(null);
		p2.setBounds(frame.getWidth()/2-300, frame.getHeight()-300, 200, 100);
		start.setPreferredSize(new Dimension(200,90));
		p2.setBackground(Color.YELLOW);
		start.setBorderPainted(false);
		start.setFont(new Font("Monospaced",Font.PLAIN,40));
		start.setBackground(Color.YELLOW);
		start.setActionCommand("start game");
		start.addActionListener(this);
		p2.add(start);
		this.add(p2);
		
		p3.setBounds(frame.getWidth()/2+100, frame.getHeight()-300, 200, 100);
		howToPlay.setPreferredSize(new Dimension(200,90));
		howToPlay.setActionCommand("show instructions");
		howToPlay.setFont(new Font("Monospaced",Font.PLAIN,20));
		howToPlay.setBorderPainted(false);
		howToPlay.setBackground(Color.RED);
		p3.setBackground(Color.RED);
		howToPlay.addActionListener(this);
		p3.add(howToPlay);
		this.add(p3);

		
		rectX=-frame.getWidth();
		t=new Thread(this);
		t.start();
	}
	
	public double findAngle(Point mouseLoc) {
		return Math.atan2(mouseLoc.getY()-25-(y+40),mouseLoc.getX()-(x+40));
	}
	
	public void run()
	
	{
		while(true)
		{
			imgAngle=findAngle(MouseInfo.getPointerInfo().getLocation());
			frame.getContentPane().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			if(gameOn)
			{
				if (fuel>0) {		
					
				if(w) {
					floating=false;
					fuel--;
					wTime+=0.1;
					if(sTime>0)
						sTime-=0.08;
				}
				
				if(s) {
					floating=false;
					fuel--;
					sTime+=0.1;
					if(wTime>0)
						wTime-=0.08;
				}
				
				if(a) {
					floating=false;
					fuel--;
					aTime+=.1;
					if(dTime>0)
						dTime-=0.08;
				}
				
				if(d) {
					floating=false;
					fuel--;
					dTime+=0.1;
					if(aTime>0)
						aTime-=0.08;
				}
				
				if(fuel<=0)
					fuel=0;
				}
				
				y=y-wTime+sTime;
				x=x-aTime+dTime;				
				
			}
			repaint();
			if(restart)
			{
				x=100;
				y=500;
				if(death) {
					gameOn=false;
					this.add(p2);
					this.add(p3);
				}
				
				if(win) {
					gameOn=true;
					rFuelGoal+=(int)(Math.random()*5)+1;
					metalGoal+=(int)(Math.random()*5)+1;
				}
				restart=false;
				death=false;
				win=false;
				metalCount=0;
				rFuelCount=0;
				condition=10;
				fuel=800;
			}
			
			try{
				t.sleep(10);
			}catch(InterruptedException e){}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		g2d.drawImage(back, imgX, imgY, null);
		if(!gameOn) {
			if(!showInstructions) {
				g2d.drawImage(title, frame.getWidth()/2-title.getWidth()/2, 150, null);
				g2d.drawImage(introShip, frame.getWidth()/2-introShip.getWidth()/2, frame.getHeight()/2-introShip.getHeight()/2+(int)vertAnimation, null);
				if (animateUp) 
					vertAnimation-=0.5;
				else vertAnimation+=0.5;
				
				if(vertAnimation<=-20)
					animateUp=false;
				if(vertAnimation>=20)
					animateUp=true;
			}
				imgX-=2;
				if(imgX<-back.getWidth()/2)
					imgX=0;
				
			if(showInstructions) {
				g2d.drawImage(instructions, frame.getWidth()/2-400, 0,null);
			}
		}
		
		else if(startAnimation) {
			imgX-=2;
			if(imgX<-back.getWidth()/2)
				imgX=0;
			
			if(asteroidAnimation) {
				this.remove(p2);
				this.remove(p3);
				g2d.drawImage(title, frame.getWidth()/2-title.getWidth()/2, 150, null);
				g2d.drawImage(introShip, frame.getWidth()/2-introShip.getWidth()/2, frame.getHeight()/2-introShip.getHeight()/2+(int)vertAnimation, null);
				if (animateUp) 
					vertAnimation-=0.5;
				else vertAnimation+=0.5;
				
				if(vertAnimation<=-20)
					animateUp=false;
				if(vertAnimation>=20)
					animateUp=true;
				g2d.drawImage(roid[(int)roidCount%64], tempX, tempY-roid[(int)roidCount%64].getHeight()/2, null);
				roidCount+=0.1;
				tempX+=5;
				if(tempX>=frame.getWidth()/2-introShip.getWidth()/2+150) {
					asteroidAnimation=false;
					explosionAnimation=true;
					tempX=0;
				}
			}
			else if(explosionAnimation) {
				g2d.drawImage(title, frame.getWidth()/2-title.getWidth()/2, 150, null);
				g2d.drawImage(introShip, frame.getWidth()/2-introShip.getWidth()/2, frame.getHeight()/2-introShip.getHeight()/2+(int)vertAnimation, null);
				
				if (animateUp) 
					vertAnimation-=0.5;
				else vertAnimation+=0.5;
				
				if(vertAnimation<=-20)
					animateUp=false;
				if(vertAnimation>=20)
					animateUp=true;
				
				g2d.drawImage(explosions[(int)explodeCount%13], frame.getWidth()/2-150, frame.getHeight()/2-50+(int)vertAnimation, null);
				explodeCount+=0.1;
				if(explodeCount>=5) {
					g2d.drawImage(explosions[(int)(explodeCount-5)%13], frame.getWidth()/2-introShip.getWidth()/2+75, frame.getHeight()/2-250+(int)vertAnimation, null);
				}
				if(explodeCount>=8) {
					g2d.drawImage(explosions[(int)(explodeCount-8)%13], frame.getWidth()/2+introShip.getWidth()/2-250, frame.getHeight()/2-introShip.getHeight()/2+100+(int)vertAnimation, null);
				}
				if(explodeCount>=12) {
					g2d.drawImage(explosions[(int)(explodeCount-12)%13].getScaledInstance(introShip.getWidth(), introShip.getHeight(), Image.SCALE_DEFAULT), frame.getWidth()/2-400, frame.getHeight()/2-introShip.getHeight()/2+(int)vertAnimation, null);
				}
				if(explodeCount>=18 && !transitionAnimation) {
					//explosionAnimation=false;
					floatAnimation=true;
				}
			}
			
			if(floatAnimation) {
				tempX+=2;
				AffineTransform backup=g2d.getTransform();
				AffineTransform trans=new AffineTransform();
				trans.rotate(Math.toRadians(tempX),frame.getWidth()/2+introShip.getWidth()/2+tempX+40,frame.getHeight()/2+40);
				g2d.transform(trans);
				g2d.drawImage(guys[imgCount].getScaledInstance(40, 40, Image.SCALE_DEFAULT), frame.getWidth()/2+introShip.getWidth()/2+tempX, frame.getHeight()/2, null);
				g2d.setTransform(backup);
				if(frame.getWidth()/2+introShip.getWidth()/2+tempX>frame.getWidth()) {
					floatAnimation=false;
					transitionAnimation=true;
					rectX=frame.getWidth();
				}
			}
			
			if(transitionAnimation) {
				x=100;
				y=500;
				if(rectX+frame.getWidth()>=0) {
					g2d.setColor(Color.BLACK);
					//getSize().System.out.println(rectX);
					g2d.fillRect(rectX, 0, frame.getWidth(), frame.getHeight());
					rectX-=10;
				}
				if(rectX<0) {
					transitionAnimation=false;
					explosionAnimation=false;
					startAnimation=false;
				}	
			}
			
			else{
				gameOn=true;
				wTime=0;
				sTime=0;
				aTime=0;
				dTime=0.3;
			}
		}
		
		else {		
		
		if(!death && !win) {
			g2d.drawImage(monitor, frame.getWidth()-monitor.getWidth()-50, 0, null);
			g2d.setFont(new Font("Futura",Font.BOLD,20));
			g2d.setColor(Color.GREEN);
			g2d.drawImage(fuelCell.getScaledInstance(50, 60, Image.SCALE_DEFAULT), frame.getWidth()-monitor.getWidth()-40, 30, null);
			g2d.drawString(rFuelCount+" OUT OF "+rFuelGoal, frame.getWidth()-monitor.getWidth()+10, 80);
			g2d.drawImage(metal.getScaledInstance(40, 40, Image.SCALE_DEFAULT), frame.getWidth()-monitor.getWidth()-40, 130, null);
			g2d.drawString(metalCount+" OUT OF "+metalGoal, frame.getWidth()-monitor.getWidth()+10, 150);
		}
		
		if (collectSound.getMicrosecondPosition()>=collectSound.getMicrosecondLength()) {
   	     try {
    	     collectSound=AudioSystem.getClip();
    	     collectSound.open(AudioSystem.getAudioInputStream(new File("src/146723__fins__coin-object.wav")));
		} catch (Exception e) {}
		}
		
		if(refillSound.getMicrosecondPosition()>3340000 || fuel>800) {
	    	 refillSound.stop();
	    	 try {
	    	     refillSound=AudioSystem.getClip();
	    	     refillSound.open(AudioSystem.getAudioInputStream(new File("src/Health-Potion-and-Magic-Drink-Sound-Effects-High-Quality-Free-Download.wav")));
	    	     refillSound.setMicrosecondPosition(2200000);
			} catch (Exception e) {}
		}
		
		if(hurtSound.getMicrosecondPosition()>=1000000) {
			hurtSound.stop();
			try {
	    	     hurtSound=AudioSystem.getClip();
	    	     hurtSound.open(AudioSystem.getAudioInputStream(new File("src/oww-ow-Sound-Effect.wav")));
	    	     hurtSound.setMicrosecondPosition(500000);
			}catch(Exception e) {};
		}
		
		
		for(int i=0;i<apList.size();i++) {
			apList.get(i).paint(g2d);
			
			if(apList.get(i).hitBox.intersects(playerBox) && !apList.get(i).type.equals("norm")) {
				String type=apList.get(i).type;
				collectSound.start();
				apList.remove(i);
				if(type.equals("fuel")) {
					refilling=true;
					refillSound.start();
				}
				else if(type.equals("rocket fuel"))
					rFuelCount++;
				else if(type.equals("metal"))
					metalCount++;
			}
		}
		for(int i=0;i<asList.size();i++) {
			if(asList.get(i).hitBox.intersects(playerBox) && !playerHit) {
				condition--;
				hurtSound.start();
				safeCount=0;
				playerHit=true;
			}
			asList.get(i).paint(g2d);
		}
		safeCount++;
		
		if (condition<=0)
			death=true;
		
		if(rFuelCount>=rFuelGoal && metalCount>=metalGoal) {
			win=true;
		}
		
		if(refilling) {
			fuel+=10;
		}
		if(fuel>=800) {
			fuel=800;
			refilling=false;
		}
		if(safeCount>=110)
			playerHit=false;
		
		AffineTransform backup=g2d.getTransform();
		AffineTransform trans=new AffineTransform();
		trans.translate(0, -guys[imgCount].getHeight(null));
		trans.rotate(imgAngle,x+40,y+40);
		g2d.transform(trans);
		g2d.drawImage(guys[imgCount], (int)x, (int)y, null);			
		//g2d.setColor(Color.RED);
		//g2d.fillOval((int)x+40, (int)y+40, 10, 10);
		g2d.setTransform(backup);
		
		if (!death && !win) {
			int qw=(int)(Math.random()*100)+1;
			if(qw<=2) {
				int rand=(int)(Math.random()*3);
				makeNewAnchor(rand);
			}
				else if(qw<=4 && asList.size()<10) {
					int rand=(int)(Math.random()*3);
					makeNewRoid(rand);
				}
			
			if(fuel>0) {
			if(w) {
				g2d.drawImage(airs[wCount],(int)(x+guys[imgCount].getWidth()/2-airs[wCount].getWidth()/2),(int)(y-guys[imgCount].getHeight()/2),null);
				if(wCount+1>39)
					wCount=0;
				else wCount++;
				
			}
			if(s) {
				g2d.drawImage(airs[sCount],(int)(x+guys[imgCount].getWidth()/2-airs[sCount].getWidth()/2),(int)(y-guys[imgCount].getHeight()-airs[sCount].getHeight()),null);
				if(sCount+1>39)
					sCount=0;
				else sCount++;
			}
			if(a) {
				g2d.drawImage(airs[aCount],(int)(x+guys[imgCount].getWidth()/2),(int)(y-guys[imgCount].getHeight()-airs[aCount].getHeight()/2),null);
				if(aCount+1>39)
					aCount=0;
				else aCount++;
			}
			if(d) {
				g2d.drawImage(airs[dCount],(int)(x-guys[imgCount].getWidth()),(int)(y-guys[imgCount].getHeight()-airs[dCount].getHeight()/2),null);
				if(dCount+1>39)
					dCount=0;
				else dCount++;
			}
			}
			
			
		}
		
		if((shooting) && floating) {
		trans=new AffineTransform();

		if(shootSlope<0)
			trans.rotate(shootAngle,tempX+25,(int)(shootSlope*(tempX-(x+40))+(y+25)));
		else 
			trans.rotate(shootAngle,tempX+25,(int)(shootSlope*(tempX-(x+40))+(y-25)));
		g2d.transform(trans);
		//if(right && shootSlope>0)
			  //NO SPRITE SHEET > g2d.drawImage(openHook, tempX, (int)(shootSlope*(tempX-(x-40))+(y+25)), null);
			/*g2d.drawImage(hooks[0], tempX, (int)(shootSlope*(tempX-(x-40))+(y+25)), null);		
		else if(right && shootSlope<0)
			g2d.drawImage(hooks[0], tempX, (int)(shootSlope*(tempX-(x-40))+(y-25)), null);		
		else if(!right && shootSlope<0)		
			g2d.drawImage(hooks[0], tempX, (int)(shootSlope*(tempX-(x-40))+(y-25)), null);		
		else 	
			g2d.drawImage(hooks[0], tempX, (int)(shootSlope*(tempX-(x-40))+(y+25)), null);	*/	

		g2d.setTransform(backup);
		}
		
		else if(ropeDrawn) {
		trans=new AffineTransform();
		if(shootSlope<0)
			trans.rotate(shootAngle,finalAimX+25,finalAimY+25);
		else 
			trans.rotate(shootAngle,finalAimX+25,finalAimY+25);
		g2d.transform(trans);
		//g2d.drawImage(closeHook, (int)finalAimX,(int)finalAimY, null);		
		g2d.setTransform(backup);
		}
		
		Stroke s=g2d.getStroke();
		g2d.setStroke(new BasicStroke(10));
		//g2d.setColor(new Color(153,102,51));
		g2d.setColor(Color.LIGHT_GRAY);

		
		
		if (y<-81 || x>frame.getWidth()+81 || y>frame.getHeight()+81 || x<-81) {
			death=true;
		}
		
		if(!death && !win) {
		if(shooting && !ropeDrawn) {
			g2d.drawLine((int)x+40, (int)y-40, tempX, (int)(shootSlope*(tempX-(x+40))+(y+40)));
			if (right && tempX<finalAimX) {
				if(finalAimX-tempX<25)
					tempX+=25;
				else tempX+=50;
			}
			
			else if (!right && tempX>finalAimX) {
				if(tempX-finalAimX<25)
					tempX-=25;
				else tempX-=50;
			}
			
			else {
				tempX=(int) Math.round(x);
				if(goodAim) {
					ropeDrawn=true;
				}
				else {
					ropeDrawn=false;
					floating=false;
					tempX=(int) Math.round(finalAimX);
					tempY=(int) Math.round(finalAimY);
				}
				shooting=false;
				time=0;}
		}	
		
		else if((ropeDrawn || floating)) {
			//if(right) {
				r+=Math.min(time,8);
				x=r*Math.cos(shootAngle)+tempX;
				y=r*Math.sin(shootAngle)+tempY;
				
				if(r<shootDistance && goodAim) {
						time+=.1;
				g2d.drawLine((int)x+40, (int)y-40, (int)finalAimX, (int)finalAimY);
				}
				
				else {
					ropeDrawn=false;
					goodAim=false;
					shootDistance=0;
					for(int i=0;i<apList.size();i++)
						apList.get(i).mover=true;
				}
				
				/*y=(int)(shootSlope*(x-(tempX))+(tempY));
				if(finalAimX-x<50)
					x+=Math.min(1*time,3);
				else x+=Math.min(1*time,8);
				if(x<finalAimX && goodAim) {time+=.2;
					g2d.drawLine((int)x+40, (int)y-40, (int)finalAimX, (int)finalAimY);
				}
				else {
					ropeDrawn=false;
					goodAim=false;
					for(int i=0;i<apList.size();i++)
						apList.get(i).mover=true;
				}
			}
			else if (!right){
				y=(int)(shootSlope*(x-(tempX))+(tempY));
				if(x-finalAimX<50)
					x-=Math.min(1*time,3);
				else x-=Math.min(1*time,8);
				if(x>finalAimX && goodAim) {time+=.2;
					g2d.drawLine((int)x+40, (int)y-40, (int)finalAimX, (int)finalAimY);}
				else {
					ropeDrawn=false;
					goodAim=false;
					for(int i=0;i<apList.size();i++)
						apList.get(i).mover=true;
				}
			}*/
		}
		
		for(int i=apList.size()-1;i>=0;i--) {
			if(!apList.get(i).inFrame())
				apList.remove(i);
		}
		
		for(int i=asList.size()-1;i>=0;i--) {
			if(!asList.get(i).inFrame())
				asList.remove(i);
		}
		
		if (fuel>350)
			g2d.setColor(Color.GREEN);
		else if(fuel>200)
			g2d.setColor(Color.yellow);
		else if(fuel>75)
			g2d.setColor(Color.ORANGE);
		else 
			g2d.setColor(Color.RED);
		g2d.drawLine(frame.getWidth()/2-250,50,frame.getWidth()/2-250+fuel,50);
	
		playerBox=new Rectangle((int)x+20,(int)y-guys[imgCount].getHeight()+10,guys[imgCount].getWidth()-20,guys[imgCount].getHeight()-20);
		g2d.setColor(Color.CYAN);
		for(int i=0;i<condition;i++) {
			g2d.fillRect(frame.getWidth()/2-250+(i*50), 20, 40, 10);
		}
		//if(!playerHit)
		//	g2d.draw(playerBox);
		g2d.setStroke(s);
		g2d.setColor(Color.RED);
		g2d.fillOval((int)MouseInfo.getPointerInfo().getLocation().getX(), (int)MouseInfo.getPointerInfo().getLocation().getY()-25, 5, 5);

		}
		
	//LOSE ANIMATION
		else if(death){
			wTime=0;
			sTime=0;
			aTime=0;
			dTime=0;
			time=0;
			
			g2d.setColor(Color.BLACK);
			
			if (rectX>=0) {
			imgX+=2;
			for(int i=apList.size()-1;i>=0;i--)
				apList.remove(i);
			for(int i=asList.size()-1;i>=0;i--)
				asList.remove(i);
			if(imgX>=0)
				imgX=-back.getWidth()/2;
			x=frame.getWidth()/2-guys[imgCount].getWidth()/2;	
			y=frame.getHeight()/2-guys[imgCount].getWidth()/2+100;
			g2d.drawImage(gameOver,frame.getWidth()/2-gameOver.getWidth()/2,200,null);
			g2d.drawImage(restartText, frame.getWidth()/2-continueText.getWidth()/2, frame.getHeight()-400, null);
			}	
			
			if(rectX<frame.getWidth()) {
				g2d.fillRect(rectX, 0, frame.getWidth(), frame.getHeight());
				rectX+=10;
			}

		}
		
		//WIN ANIMATION
		else if(win) {
			wTime=0;
			sTime=0;
			aTime=0;
			dTime=0;
			time=0;
			g2d.setColor(Color.BLACK);

			if(rectX>=0) {
				g2d.drawImage(winText, frame.getWidth()/2-winText.getWidth()/2, 200, null);
				g2d.drawImage(continueText, frame.getWidth()/2-continueText.getWidth()/2, frame.getHeight()-400, null);
			for(int i=apList.size()-1;i>=0;i--)
				apList.remove(i);
			for(int i=asList.size()-1;i>=0;i--)
				asList.remove(i);
			if(imgX<=-back.getWidth()/2)
				imgX=-0;
			imgX-=2;
			x=frame.getWidth()/2-guys[imgCount].getWidth()/2;	
			y=frame.getHeight()/2-guys[imgCount].getWidth()/2;
			
			g2d.drawImage(surviveShip, (int)x-surviveShip.getWidth()/2, (int)y-surviveShip.getHeight()/2-50+(int)vertAnimation, null);
			if (animateUp) 
				vertAnimation-=0.5;
			else vertAnimation+=0.5;
			
			if(vertAnimation<=-20)
				animateUp=false;
			if(vertAnimation>=20)
				animateUp=true;
			}	
			
			if(rectX<frame.getWidth()) {
				g2d.fillRect(rectX, 0, frame.getWidth(), frame.getHeight());
				rectX+=10;
			}
		}
	}
		
	}


	private void makeNewAnchor(int dir) {
		switch(dir) {
		case 0: apList.add(new AnchorPoint(-100,(int)(Math.random()*frame.getHeight()),this,dir));
				break;
		case 1:	apList.add(new AnchorPoint(frame.getWidth(),(int)(Math.random()*frame.getHeight()),this,dir));
				break;	
		case 2:	apList.add(new AnchorPoint((int)(Math.random()*frame.getWidth()),-100,this,dir));
				break;		
		case 3:	apList.add(new AnchorPoint((int)(Math.random()*frame.getWidth()),frame.getHeight()+100,this,dir));
				break;	
				
		}
	}
	
	private void makeNewRoid(int dir) {
		switch(dir) {
		case 0: asList.add(new Asteroid(-100,(int)(Math.random()*frame.getHeight()),dir,this, roid));
				break;
		case 1:	asList.add(new Asteroid(frame.getWidth(),(int)(Math.random()*frame.getHeight()),dir,this, roid));
				break;	
		case 2:	asList.add(new Asteroid((int)(Math.random()*frame.getWidth()+100),-100,dir,this,roid));
				break;		
		case 3:	asList.add(new Asteroid((int)(Math.random()*frame.getWidth()+100),frame.getHeight()+100,dir,this,roid));
				break;	
				
		}
	}

	public static void main(String args[])
	{
		MainGame app=new MainGame();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

			
			if(win || death) {
				restart=true;
			}
			else if(!startAnimation && gameOn && !shooting && !ropeDrawn){

				finalAimX=e.getX();
				finalAimY=e.getY()-25;	
				
				for(int i=0;i<apList.size();i++) {
					if (apList.get(i).hitBox.contains(finalAimX,finalAimY)) {
						goodAim=true;
						apList.get(i).mover=false;
						break;
					}
				}
				
				if(goodAim) {		
					tempX=(int) Math.round(x+40);
					tempY=(int)Math.round(y);
					time=0;
					r=0;
					wTime=0;
					sTime=0;
					aTime=0;
					dTime=0;
					shooting=true;
					floating=true;	
					shootAngle=imgAngle;
					shootDistance=(int) Math.sqrt((finalAimY-y)*(finalAimY-y)+(finalAimX-x)*(finalAimX-x));
					
					shootSlope=((y+40)-finalAimY)/((x+40)-finalAimX);
					//if (shootSlope>0)
						//shootAngle=imgAngle-Math.PI/4;
					//else
						//shootAngle=imgAngle-Math.PI/4;
					
					if(finalAimX>x)
						right=true;
					if(finalAimX<x) 
						right=false;
					//System.out.println(Math.toDegrees(shootAngle));
				}
				else{
					shooting=false;
					//floating=false;
				}
			}
			
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("start game")) {
			gameOn=true;
			tempX=0;
			tempY=frame.getHeight()/2;
			asteroidAnimation=true;
			startAnimation=true;
		}
		
		else if(e.getActionCommand().equals("show instructions")) {
			if(!showInstructions) {
				showInstructions=true;
				this.remove(p2);
			}
			else { 
				showInstructions=false;
				this.add(p2);
			}
		}
		
	}
}