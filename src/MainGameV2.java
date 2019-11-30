import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;

import java.awt.geom.AffineTransform;

public class MainGameV2 extends JPanel implements Runnable, MouseListener
{
	private float angle;
	private int x;
	private int y;
	int fuel=500;
	private JFrame frame;
	Thread t;
	private boolean gameOn;
	BufferedImage guy;
	BufferedImage[] guys=new BufferedImage[11];
	boolean restart=false;
	int imgCount=0;

	BufferedImage back;

	boolean w;
	boolean s;
	boolean a;
	boolean d;

	double imgAngle;

	int playerX;
	int playerY;
	int imgCenterX;
	int imgCenterY;

	boolean shooting=false;
	boolean ropeDrawn=false;
	double finalAimX;
	double finalAimY;
	double shootSlope;
	int tempX;
	int tempY;


	InputMap iMap=getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	ActionMap aMap=getActionMap();

	public MainGameV2()
	{
		frame=new JFrame();
		gameOn=true;

		try {
			guy = ImageIO.read(new File("C:\\Users\\Alex\\eclipse\\workspace\\GameProj\\src\\char.png"));
			//guy=ImageIO.read(new File("char.png"));
			for(int x=0;x<1;x++)
				guys[x]=guy.getSubimage(x*81,0,81,81);
		}
		catch (IOException e) {
		}

		try{
			back=ImageIO.read(new File("C:\\Users\\Alex\\eclipse\\workspace\\GameProj\\src\\xChy58V.png"));
			//back=ImageIO.read(new File("back.jpg"));
		}
		catch(IOException e){}

		frame.add(this);

		iMap.put(KeyStroke.getKeyStroke("W"),"up");
		aMap.put("up",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				w=true;
			}
		});
		iMap.put(KeyStroke.getKeyStroke("S"),"down");
		aMap.put("down",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				s=true;
			}
		});
		iMap.put(KeyStroke.getKeyStroke("A"),"left");
		aMap.put("left",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				a=true;
			}
		});
		iMap.put(KeyStroke.getKeyStroke("D"),"right");
		aMap.put("right",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
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


		frame.setSize(1000,800);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		x=0;
		y=0;
		playerX=frame.getWidth()/2-40;
		playerY=frame.getHeight()/2-40;
		imgCenterX=playerX+40;
		imgCenterY=playerY+40;
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addMouseListener(this);
		setVisible(true);
		t=new Thread(this);
		t.start();
	}

	public double findAngle(Point mouseLoc) {
		return Math.atan2(mouseLoc.y-(playerY+40),mouseLoc.x-(playerX+40));
	}

	public void run()
	{
		while(true)
		{
			imgAngle=findAngle(MouseInfo.getPointerInfo().getLocation());
			frame.setCursor(Cursor.CROSSHAIR_CURSOR);
			if(gameOn){
				//x++;
				//imgCenterX++;
				if(w && y<=0){
					y+=5;
					fuel-=1;
				}
				if(s && y+back.getHeight()>frame.getHeight()){
					y-=5;
					fuel-=1;
				}
				if (a){
					x+=5;
					fuel-=1;
				}
				if (d){
					x-=5;
					fuel-=1;
				}
				if (fuel<=0)
					fuel=0;

				repaint();
			}
			if(restart)
			{
				restart=false;
				gameOn=true;
			}
			try
			{
				t.sleep(10);
			}catch(InterruptedException e)
			{
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		AffineTransform backup=g2d.getTransform();
		AffineTransform trans=new AffineTransform();
		//trans.translate(0, -guys[imgCount].getHeight(null));
		g2d.drawImage(back,x,y,null);
		trans.rotate(imgAngle,playerX+40,playerY+40);
		g2d.transform(trans);
		g2d.drawImage(guys[imgCount], playerX, playerY, null);
		g2d.setColor(Color.RED);
		g2d.fillOval(imgCenterX, imgCenterY, 10, 10);
		g2d.setTransform(backup);
		Stroke s=g2d.getStroke();
		g2d.setStroke(new BasicStroke(10));
		g2d.setColor(new Color(153,102,51));

		if(shooting) {
		g2d.drawLine(playerX+40, playerY+40, tempX, (int)(shootSlope*(tempX-(playerX+40))+(playerY+40)));
		if (tempX<finalAimX) {
			tempX+=15;
		}
		else {tempX=playerX;ropeDrawn=true;shooting=false;}
		}

		else if(ropeDrawn) {
			if (imgCenterX<=finalAimX) {
				if(finalAimX-imgCenterX<10) {
					x-=2;
					imgCenterX+=2;
				}
				else{x-=10;
				imgCenterX+=10;}
				y=(int)(shootSlope*(x-(tempX))+(tempY));
				imgCenterY-=y;
				g2d.drawLine(imgCenterX, imgCenterY, (int)finalAimX, (int)finalAimY);
			}
			else{
				imgCenterX=playerX+40;
				imgCenterY=playerY+40;
				shooting=false;
				ropeDrawn=false;
			}
		}
		
		g2d.setColor(Color.RED);
		g2d.drawLine(frame.getWidth()/2-fuel/2,50,frame.getWidth()/2+fuel/2,50);

		g2d.setStroke(s);
	}


	public static void main(String args[])
	{
		MainGameV2 app=new MainGameV2();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//shooting=false;
		if (!shooting) {
			tempX=playerX+40;
			tempY=playerY;
			finalAimX=e.getX();
			finalAimY=e.getY();
			shootSlope=((playerY+40)-finalAimY)/((playerX+40)-finalAimX);
			shooting=true;
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
}