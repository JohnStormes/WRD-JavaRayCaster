package Main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Player{
	MainPanel mp;
	public double playerX;
	public double playerY;
	public int curX;
	public int curY;
	public int speed;
	public double lookAngle;
	public double lookSensitivity; //for settings
	public boolean canMove = true;
	
	public static final int FOV = 90;
	
	//points
	public int points = 0;
	
	//hitbox
	public Rectangle hitBox;
	
	//ultimate images
	public BufferedImage[] ultimate1Images;
	public BufferedImage[] ultimate1DeathImages;
	
	//health
	public int curHealth;
	public int maxHealth = 100;
	
	//ultimate 1 availability and cooldown
	public boolean ultimate1Available = false;
	public boolean ultimate1Cooldown;
	public int ultimate1CooldownCounter = 200;
	
	public Player(MainPanel aMp, int aX, int aY){
		mp = aMp;
		playerX = aX;
		playerY = aY;
		speed = 2;
		lookAngle = Math.PI / 4;
		lookSensitivity = 0.005;
		hitBox = new Rectangle((int)playerX - 5, (int)playerY - 5, 10, 10);
		curHealth = maxHealth;
		setUltimateImages();
	}
	
	public void setUltimateImages(){
		ultimate1Images = new BufferedImage[7];
		ultimate1DeathImages = new BufferedImage[8];
		try {
			ultimate1Images[0] = ImageIO.read(getClass().getResourceAsStream("/src/ultimate1_1.png"));
			ultimate1Images[1] = ImageIO.read(getClass().getResourceAsStream("/src/ultimate1_2.png"));
			ultimate1Images[2] = ImageIO.read(getClass().getResourceAsStream("/src/ultimate1_3.png"));
			ultimate1Images[3] = ImageIO.read(getClass().getResourceAsStream("/src/ultimate1_4.png"));
			ultimate1Images[4] = ImageIO.read(getClass().getResourceAsStream("/src/ultimate1_5.png"));
			ultimate1Images[5] = ImageIO.read(getClass().getResourceAsStream("/src/ultimate1_6.png"));
			ultimate1Images[6] = ImageIO.read(getClass().getResourceAsStream("/src/ultimate1_7.png"));
			
			ultimate1DeathImages[0] = ImageIO.read(getClass().getResourceAsStream("/src/enemy2_ult1_1.png"));
			ultimate1DeathImages[1] = ImageIO.read(getClass().getResourceAsStream("/src/enemy2_ult1_2.png"));
			ultimate1DeathImages[2] = ImageIO.read(getClass().getResourceAsStream("/src/enemy2_ult1_3.png"));
			ultimate1DeathImages[3] = ImageIO.read(getClass().getResourceAsStream("/src/enemy2_ult1_4.png"));
			ultimate1DeathImages[4] = ImageIO.read(getClass().getResourceAsStream("/src/enemy2_ult1_5.png"));
			ultimate1DeathImages[5] = ImageIO.read(getClass().getResourceAsStream("/src/enemy2_ult1_6.png"));
			ultimate1DeathImages[6] = ImageIO.read(getClass().getResourceAsStream("/src/enemy2_ult1_7.png"));
			ultimate1DeathImages[7] = ImageIO.read(getClass().getResourceAsStream("/src/enemy2_ult1_8.png"));
		} catch (IOException e){
			System.out.println ("ultimate images error");
		}
	}
	
	public void getHit(Entity entity){
		curHealth -= entity.damage;
		mp.playSoundEffect(9);
		if (curHealth <= 0){
			mp.gameState = mp.DEATH_STATE;
		}
	}
	
	public void getHit(int curDamage){
		curHealth -= curDamage;
		mp.playSoundEffect(9);
		if (curHealth <= 0){
			mp.gameState = mp.DEATH_STATE;
		}	
	}
	
	public void update(){
		mp.cDet.checkCollisions(this);
		
		//ultimate cooldown
		if (ultimate1Cooldown == true){
			ultimate1CooldownCounter--;
			if (ultimate1CooldownCounter < 0){
				ultimate1CooldownCounter = 200;
				ultimate1Cooldown = false;
			}
		}
		
		double xDiff = (mp.mouseH.x - mp.mouseH.prevX) * lookSensitivity;
		double max = (mp.screenWidth / 2 - Ray.startPosX) * lookSensitivity;
		if (xDiff < max - (max / 5) && xDiff > -(max - (max / 5))){
			lookAngle -= xDiff;
			if (lookAngle + Math.PI / 4 < 0){
				lookAngle = Math.PI * 2 + lookAngle;
			} else if (lookAngle + Math.PI / 4 > Math.PI * 2){
				lookAngle = lookAngle - Math.PI * 2;
			}
			//System.out.println (xDiff);
			mp.mouseH.prevX = mp.mouseH.x;
		} else {
			mp.mouseH.prevX = mp.screenWidth / 2;
		}
		hitBox.x = (int)playerX - 5;
		hitBox.y = (int)playerY - 5;
	}
	
	public void draw(Graphics2D g2){
		/*
		g2.setColor(Color.yellow);
		g2.fillOval((int)playerX - 5, (int)playerY - 5, 10, 10);
		*/
	}
}