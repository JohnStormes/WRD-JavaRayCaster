package Main;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class UI{
	public MainPanel mp;
	public Font font1;
	private Color shade;
	private ArrayList<Rectangle> stringRects;
	private int pausedScreenSelection = 0;
	public int crossHairSize; //SETTINGS
	Settings settings = null;
	
	//death image
	private BufferedImage deathImage;
	private BufferedImage winImage;
	private BufferedImage titleImage;
	private int counter;
	private int maxCounter = 1000000000;
	private boolean playedDeath;
	private boolean playedWin;
	private boolean playedTitle;
	
	//temp
	private int titleCounter;
	private int titleInc;
	
	public UI(MainPanel aMp){
		mp = aMp;
		shade = new Color((float)0, (float)0, (float)0, (float)0.7);
		font1 = new Font("Ink Free", Font.BOLD, 40);
		stringRects = new ArrayList<Rectangle>();
		stringRects.add(new Rectangle(50, 200, 440, 80));
		stringRects.add(new Rectangle(50, 350, 250, 80));
		stringRects.add(new Rectangle(50, 500, 310, 80));
		crossHairSize = 10;
		try {
			deathImage = ImageIO.read(getClass().getResourceAsStream("/src/textureImages/death_image.png"));
			winImage = ImageIO.read(getClass().getResourceAsStream("/src/textureImages/giga_chad.jpg"));
			titleImage = ImageIO.read(getClass().getResourceAsStream("/src/textureImages/title_screen.png"));
		} catch (IOException e) {
			System.out.println ("death image error");
		}
	}
	
	//returns the index of the rectangle being hovered over in the paused screen for UI effects
	private int checkPausedScreen(){
		for (int x = 0; x < stringRects.size(); x++){
			if (mp.mouseH.x >= stringRects.get(x).x && mp.mouseH.x <= stringRects.get(x).x + stringRects.get(x).width
			  && mp.mouseH.y >= stringRects.get(x).y && mp.mouseH.y <= stringRects.get(x).y + stringRects.get(x).height){
				return x;	
			}
		}
		return -1;
	}
	
	//called from mouse handler
	public void clicked(){
		if (mp.gameState == mp.PAUSE_STATE){
			if (checkPausedScreen() == 0){
				//resume
				mp.setCursor(mp.blankCursor);
				mp.gameState = mp.PLAY_STATE;
				mp.mouseH.robot.mouseMove(mp.mouseH.pauseHoldX + 5, mp.mouseH.pauseHoldY);
			} else if (checkPausedScreen() == 1){
				//settings
				settings = new Settings(mp);
			} else if (checkPausedScreen() == 2){
				//exit game
				System.exit(0);
			}
		}
	}
	
	public void update(){
		if (playedDeath == false && mp.gameState == mp.DEATH_STATE){
			mp.setCursor(Cursor.getDefaultCursor());
			mp.stopFinalMusic();
			mp.playMusic(5);
			playedDeath = true;
		} else if (playedWin == false && mp.gameState == mp.WIN_STATE){
			mp.setCursor(Cursor.getDefaultCursor());
			mp.stopFinalMusic(); 
			mp.playMusic(17);
			playedWin = true;
		} else if (playedTitle == false && mp.gameState == mp.TITLE_STATE){
			mp.setCursor(Cursor.getDefaultCursor());
			playedTitle = true;
		}
	}
	
	public void draw(Graphics2D g2){
		if (mp.gameState != mp.DEATH_STATE && mp.gameState != mp.WIN_STATE && mp.gameState != mp.TITLE_STATE){
			//crosshair
			/* g2.setColor(Color.RED);
			g2.fillOval(Ray.startPosX + (Ray.rectWidth * Player.FOV / 2) - (crossHairSize / 2)
				, Ray.startPosY - (crossHairSize / 2), crossHairSize, crossHairSize); */
			g2.setColor(Color.MAGENTA);
			g2.setStroke(new BasicStroke(2));
			g2.drawLine(Ray.startPosX + (Ray.rectWidth * Player.FOV / 2), Ray.startPosY - (crossHairSize / 2)
				,Ray.startPosX + (Ray.rectWidth * Player.FOV / 2), Ray.startPosY + (crossHairSize / 2));
			g2.drawLine(Ray.startPosX + (Ray.rectWidth * Player.FOV / 2) - (crossHairSize / 2), Ray.startPosY
				,Ray.startPosX + (Ray.rectWidth * Player.FOV / 2) + (crossHairSize / 2), Ray.startPosY);
			
			//black tiles so nothing is outside of in game window
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, mp.screenWidth, (Ray.startPosY - Ray.rectHeight));
			g2.fillRect(0, Ray.startPosY + Ray.rectHeight, mp.screenWidth, mp.screenHeight - (Ray.startPosY + Ray.rectHeight));
			g2.fillRect(0, 0, Ray.startPosX + Ray.rectWidth, mp.screenHeight);
			g2.fillRect(Ray.startPosX + ((Player.FOV + 1) * Ray.rectWidth), 0, 100, mp.screenHeight);
				
			//player health bar
			g2.setStroke(new BasicStroke(40));
			g2.setColor(Color.GREEN);
			double length = ((double)mp.player.curHealth / mp.player.maxHealth) * 200;
			if (mp.player.curHealth > 0){
				g2.drawLine(Ray.startPosX + 100, Ray.startPosY - Ray.rectHeight + 100, 100 + (int)length - 40, Ray.startPosY - Ray.rectHeight + 100);
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(5));
				g2.drawRect(Ray.startPosX + 80, Ray.startPosY - Ray.rectHeight + 80, 200, 40);
			}
			
			//player points
			g2.setColor(Color.YELLOW);
			g2.setFont(mp.invM.ammoFont);
			g2.drawString("score: " + mp.player.points, (Ray.startPosX + (Player.FOV * Ray.rectWidth)) - 500
			, Ray.startPosY - Ray.rectHeight + 100);
			
			//round and enemies
			g2.setFont(new Font("Courier", Font.BOLD, 25));
			if (mp.roundM.curRound != 5){
				g2.drawString("enemies left: " + (mp.roundM.enemiesInThisRound - mp.roundM.enemiesKilled)
				, (Ray.startPosX + (Player.FOV * Ray.rectWidth)) - 500, Ray.startPosY - Ray.rectHeight + 200);
			} else {
				g2.drawString("enemies left: ???", (Ray.startPosX + (Player.FOV * Ray.rectWidth)) - 500, Ray.startPosY - Ray.rectHeight + 200);
			}
			
			if (mp.roundM.buffer == true){
				if (mp.roundM.bufferCount >= 30){
					mp.roundM.bufferInc = -1;
				}
				if (mp.roundM.bufferCount < 0){
					mp.roundM.bufferInc = 1;
				}
				mp.roundM.bufferCount += mp.roundM.bufferInc;
				double scale = 1.0 / 60;
				Color opaque = new Color((float)1.0, (float)0.0, (float)0.0, (float)(mp.roundM.bufferCount * scale + scale * 30));
				g2.setColor(opaque);
			}
			g2.drawString("round: " + mp.roundM.curRound, (Ray.startPosX + (Player.FOV * Ray.rectWidth)) - 500
			, Ray.startPosY - Ray.rectHeight + 150);
			
			//ultimate cooldown
			if (mp.player.ultimate1Cooldown == true){
				g2.setColor(Color.CYAN);
				g2.fillRect(Ray.startPosX + 30 * Ray.rectWidth, Ray.startPosY + Ray.rectHeight - 100, mp.player.ultimate1CooldownCounter * 3, 10);
			}
			
			//current gun number
			g2.setFont(mp.invM.newAmmoFont);
			if (mp.invM.curGun == 0){
				g2.setColor(Color.YELLOW);
				g2.drawString("1", mp.screenWidth / 32, Ray.startPosY - 100);
				g2.setColor(Color.GRAY);
				g2.drawString("2", mp.screenWidth / 32, Ray.startPosY);
				g2.drawString("3", mp.screenWidth / 32, Ray.startPosY + 100);
			} else if (mp.invM.curGun == 1){
				g2.setColor(Color.GRAY);
				g2.drawString("1", mp.screenWidth / 32, Ray.startPosY - 100);
				g2.setColor(Color.YELLOW);
				g2.drawString("2", mp.screenWidth / 32, Ray.startPosY);
				g2.setColor(Color.GRAY);
				g2.drawString("3", mp.screenWidth / 32, Ray.startPosY + 100);
			} else if (mp.invM.curGun == 2){
				g2.setColor(Color.GRAY);
				g2.drawString("1", mp.screenWidth / 32, Ray.startPosY - 100);
				g2.drawString("2", mp.screenWidth / 32, Ray.startPosY);
				g2.setColor(Color.YELLOW);
				g2.drawString("3", mp.screenWidth / 32, Ray.startPosY + 100);
			}
		
		} else if (mp.gameState == mp.DEATH_STATE){
			g2.drawImage(deathImage, 0, 0, mp.screenWidth, mp.screenHeight, null);
			counter++;
			if (counter > maxCounter){
				System.exit(0);
			}
		} else if (mp.gameState == mp.WIN_STATE){
			g2.drawImage(winImage, 0, 0, mp.screenWidth, mp.screenHeight, null);
			g2.setColor(Color.YELLOW);
			g2.setFont(mp.invM.newAmmoFont);
			g2.drawString("enemies killed: " + mp.player.points / 100, 100, 100);
			g2.drawString("final score: " + mp.player.points, 100, 200);
			double seconds = (mp.totalTime / 1000000000);
			g2.drawString("time: " + (int)(seconds / 60) + ":" + (int)(seconds % 60), 100, 300);
		} else if (mp.gameState == mp.TITLE_STATE){
			g2.drawImage(titleImage, 0, 0, mp.screenWidth, mp.screenHeight, null);
			g2.setColor(Color.GREEN);
			g2.setFont(mp.invM.newAmmoFont);
			g2.drawString("William Robinson's", Ray.startPosX + ((Player.FOV / 2) * Ray.rectWidth) - 520
			, Ray.startPosY - Ray.rectHeight + 75);
			g2.drawString("Dungeon Escape", Ray.startPosX + ((Player.FOV / 2) * Ray.rectWidth) - 420
			, Ray.startPosY - Ray.rectHeight + 200);
			g2.setColor(Color.YELLOW);
			g2.setFont(mp.invM.ammoFont);
			
			g2.drawString("press (p) to play", Ray.startPosX + ((Player.FOV / 2) * Ray.rectWidth) - 280
			, Ray.startPosY - Ray.rectHeight + 300);
		}
		
		//pause menu
		if (mp.gameState == mp.PAUSE_STATE){
			g2.setFont(mp.invM.ammoFont);
			pausedScreenSelection = checkPausedScreen();
			g2.setColor(shade);
			g2.fillRect(0, 0, mp.screenWidth, mp.screenHeight);
			g2.setColor(Color.white);
			g2.drawString("GAME PAUSED", 50, 100);
			if (pausedScreenSelection == -1){
				g2.drawString("resume game", 50, 260);
				g2.drawString("settings", 50, 410);
				g2.drawString("exit game", 50, 560);
			} else if (pausedScreenSelection == 0){
				g2.setColor(Color.yellow);
				g2.drawString("resume game", 50, 260);
				g2.setColor(Color.white);
				g2.drawString("settings", 50, 410);
				g2.drawString("exit game", 50, 560);
			} else if (pausedScreenSelection == 1){
				g2.drawString("resume game", 50, 260);
				g2.setColor(Color.yellow);
				g2.drawString("settings", 50, 410);
				g2.setColor(Color.white);
				g2.drawString("exit game", 50, 560);
			} else if (pausedScreenSelection == 2){
				g2.drawString("resume game", 50, 260);
				g2.drawString("settings", 50, 410);
				g2.setColor(Color.yellow);
				g2.drawString("exit game", 50, 560);
			}
		}
	}
}