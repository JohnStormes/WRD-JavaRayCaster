package Main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Point;

public class MainPanel extends JPanel implements Runnable{
	public int FPS = 30;
	public Color ranColor = new Color(0,0,0);
	public boolean changable = true;
	BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), "blank");
	public int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public double startTime;
	public double totalTime;
	
	KeyHandler keyH = new KeyHandler(this);
	Player player;
	Grid2D grid = new Grid2D(this);
	MouseHandler mouseH = new MouseHandler(this);
	UI ui = new UI(this);
	CollisionDetection cDet = new CollisionDetection(this);
	InventoryManager invM = new InventoryManager(this);
	Sound soundDriver = new Sound();
	Sound finalMusicDriver = new Sound();
	Sound titleScreenDriver = new Sound();
	Sound backgroundDriver = new Sound();
	TextureManager textM = new TextureManager(this);
	VisualManager visualM = new VisualManager(this);
	RoundManager roundM = new RoundManager(this);
	
	public final int PLAY_STATE = 0;
	public final int PAUSE_STATE = 1;
	public final int DEATH_STATE = 2;
	public final int WIN_STATE = 3;
	public final int TITLE_STATE = 4;
	public int gameState;
	
	Thread mainThread;
	
	public MainPanel(){
		//875 175 to start at boss
		//200 50 for normal game
		player = new Player(this, 200, 50);
		//panel setup
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.addMouseMotionListener(mouseH);
		this.addMouseListener(mouseH);
		this.setFocusable(true);
		//grid.randomizeGrid();
		mouseH.setStartMouse();
		gameState = PLAY_STATE;
		setCursor(blankCursor);
		
		//initialize the rays and sprites/entities
		visualM.setup();
		playSoundEffect(8);
		startTime = System.nanoTime();
		gameState = TITLE_STATE;
		playTitleMusic();
	}
	
	public void startMainThread(){
		mainThread = new Thread(this);
		mainThread.start();
	}
	
	@Override
	public void run(){
		
		//GAME LOOP, DO NOT ALTER, USE UPDATE() AND DRAW()
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while (mainThread != null){
			
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if (delta >= 1){
				//update
				update();
				//draw
				repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >= 1000000000){
				System.out.println ("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}
	
	public void update(){
		if (gameState == PLAY_STATE){
			player.update();
			grid.update();
			visualM.update();
			roundM.update();
			totalTime += (System.nanoTime() - startTime);
			startTime = System.nanoTime();
		} else if (gameState == DEATH_STATE || gameState == WIN_STATE || gameState == TITLE_STATE){
			ui.update();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		if (gameState != DEATH_STATE && gameState != WIN_STATE && gameState != TITLE_STATE){
			grid.draw(g2);
			player.draw(g2);
			visualM.draw(g2);
			invM.draw(g2);
			changable = true;
		}
		ui.draw(g2);
		g2.dispose();
	}
	
	//MUSIC AND SOUNDS
	public void playMusic(int x){
		soundDriver.setFile(x);
		soundDriver.play();
		soundDriver.loop();
	}
	
	public void stopMusic(){
		soundDriver.stop();
	}
	
	public void playSoundEffect(int x){
		soundDriver.setFile(x);
		soundDriver.play();
	}
	
	//music for final boss
	public void playFinalMusic(){
		finalMusicDriver.setFile(13);
		finalMusicDriver.play();
		finalMusicDriver.loop();
	}
	
	public void stopFinalMusic(){
		finalMusicDriver.stop();
	}
	
	//music for title screen
	public void playTitleMusic(){
		titleScreenDriver.setFile(18);
		titleScreenDriver.play();
		titleScreenDriver.loop();
	}
	
	public void stopTitleMusic(){
		titleScreenDriver.stop();
	}
	
	//music for title screen
	public void playBackgroundMusic(){
		backgroundDriver.setFile(19);
		backgroundDriver.play();
		backgroundDriver.loop();
	}
	
	public void stopBackgroundMusic(){
		backgroundDriver.stop();
	}
}