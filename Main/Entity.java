package Main;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.BasicStroke;

public class Entity extends Visual{
	private MainPanel mp;
	//major geneeral variables
	public double disToPlayer;
	public boolean isInFOV;
	public boolean beenDrawn;
	public boolean isMoving;
	public double speed;
	public double lookAngle;
	public double canMove;
	public int entityNum;
	public int maxHealth;
	public int curHealth;
	public int damage;
	public int hitSpeed;
	public int killValue;
	
	//for death
	public boolean alive = true;
	
	//private temps
	private double angle;
	private boolean inRangeOutsideFOV;
	
	//animation
	public BufferedImage[] images;
	public BufferedImage[] deathImages;
	public BufferedImage[] punchImages;
	public int animationTime;
	public int animationCounter;
	public int curImage;
	public int curPunchImage;
	public int punchCounter;
	public boolean punching;
	
	//collisionBox
	public Rectangle collisionBox;
	public double collisionBoxWidthRatio; //this variable is the width of the collisionBox on the visual screen as a ratio to the width of the 
	//sprite image. for instance, if the sprite takes up half of the actual image, make this value 0.5
	public double curHeight;
	public int xPos;
	
	//standard death animation
	private int deathCounter;
	private int curDeathImage;
	private int deathFinalImageTime = 100;
	
	//ultimate 1 death animation
	public boolean ultimate1;
	private int ult1Counter1 = 0;
	private int ult1MaxTime1 = 5;
	private int ult1Counter2 = 0;
	private int ult1MaxTime2 = 1;
	private int ult1CurImage = 0;
	private int ult1LightningCounter = 0;
	private int ult1LightningMax = 100;
	public boolean ultimateExtraDeathTime = false;
	
	//been remove (bug fix)
	private boolean beenRemoved;
	
	//constructor
	public Entity(MainPanel aMp, int aX, int aY, double aSpeed, String[] imagePaths, String[] deathImagePaths
		, String[] punchImagePaths, int aEntityNum, double aCollisionBoxWidthRatio, int aMaxHealth, int aDamage, int aHitSpeed
		, int aKillValue){
		mp = aMp;
		x = aX;
		y = aY;
		speed = aSpeed;
		images = new BufferedImage[imagePaths.length];
		deathImages = new BufferedImage[deathImagePaths.length];
		punchImages = new BufferedImage[punchImagePaths.length];
		
		isMoving = true;
		animationTime = (int)(speed * 4);
		curImage = 0;
		animationCounter = 0;
		entityNum = aEntityNum;
		collisionBoxWidthRatio = aCollisionBoxWidthRatio;
		curDeathImage = 0;
		maxHealth = aMaxHealth;
		curHealth = maxHealth;
		damage = aDamage;
		hitSpeed = aHitSpeed;
		killValue = aKillValue;
		
		//collisionBox size
		collisionBox = new Rectangle((int)x - 10, (int)y - 10, 20, 20);
			
		//images try catch
		try {
			for (int x = 0; x < images.length; x++){
				images[x] = ImageIO.read(getClass().getResourceAsStream(imagePaths[x]));
			}
			for (int x = 0; x < deathImages.length; x++){
				deathImages[x] = ImageIO.read(getClass().getResourceAsStream(deathImagePaths[x]));
			}
			for (int x = 0; x < punchImages.length; x++){
				punchImages[x] = ImageIO.read(getClass().getResourceAsStream(punchImagePaths[x]));
			}
		} catch (IOException e){
			System.out.println ("error loading sprite image");
		}
	}
	
	//gets called any time the entity is hit, handles health, player points, sounds, and animations
	public void getHit(Gun gun){
		curHealth -= gun.damage;
		if (curHealth <= 0){
			alive = false;
			mp.playSoundEffect(6);
			mp.player.points += killValue;
			if (mp.roundM.curRound != 5){
				mp.roundM.curEnemies--;
				mp.roundM.enemiesKilled++;
			}
			if (mp.roundM.curRound != 6 && mp.roundM.curEnemies == 0 && mp.roundM.enemiesSpawned == 5 + ((mp.roundM.curRound - 1) * 3)){
				mp.roundM.increaseRound();
			}
		}
	}
	
	//returns the current distance to the player, needed for many functions
	public double distanceToPlayer(){
		double dist = Math.sqrt(Math.pow(mp.player.playerX - x, 2) + Math.pow(mp.player.playerY - y, 2));
		return dist;
	}
	
	//updates the isInFOV boolean
	public void update(){
		//collisionBox
		collisionBox.x = (int)x - 10;
		collisionBox.y = (int)y - 10;
		
		//angle to the player for rendering (where on the screen should the images appear)
		hypotenuse = distanceToPlayer();
		int horDis = (int)mp.player.playerX - (int)x;
		int vertDis = (int)mp.player.playerY - (int)y;
		if (horDis != 0){
			angle = (Math.atan((double)vertDis / horDis));
		}
		if (horDis > 0 && vertDis < 0){
			angle = -angle;
		} else if (horDis < 0 && vertDis < 0){
			angle = Math.PI - angle;
		} else if (horDis < 0 && vertDis > 0){
			angle = -angle;
			angle = Math.PI + angle;
		} else if (horDis > 0 && vertDis > 0){
			angle = Math.PI * 2 - angle;
		} else if (horDis == 0 && vertDis < 0){
			angle = angle + Math.PI;
		} else if (horDis == 0 && vertDis > 0){
			angle = angle - Math.PI;
		} else if (horDis < 0 && vertDis == 0){
			angle = angle - Math.PI;
		}
		
		lookAngle = angle - Math.PI / 4;
		
		angle = Math.PI + angle;
		if (angle > Math.PI * 2){
			angle = angle - Math.PI * 2;
		}
		if (angle > Math.PI / 4 * 7 && (mp.player.lookAngle + Math.PI / 4) < Math.PI / 4){
			angle = angle - Math.PI * 2;
		} else if (angle < Math.PI / 4 && (mp.player.lookAngle + Math.PI / 4) > Math.PI / 4 * 7){
			angle = angle + Math.PI * 2;
		}
		if (Math.abs(angle - (mp.player.lookAngle + Math.PI / 4)) <= Math.PI / 4){
			inRangeOutsideFOV = false;
			isInFOV = true;
		} else if (Math.abs(angle - (mp.player.lookAngle + Math.PI / 4)) > Math.PI / 4
			&& Math.abs(angle - (mp.player.lookAngle + Math.PI / 4)) <= Math.PI / 2){
			isInFOV = false;
			inRangeOutsideFOV = true;
		} else {
			isInFOV = false;
		}
			
		//if it's alive, check its collisions
		if (alive == true){				
			mp.cDet.checkCollisions(this);
		}
	}

	//draw function, handles all animations
	public void draw(Graphics2D g2){
		//if its on the screen and the game is not paused
		if ((isInFOV == true || inRangeOutsideFOV == true) && mp.gameState == mp.PLAY_STATE){
			double ca = 0;
			double scaledHypotenuse = 0;
			double trueScale = 0;
			
			//sprite position horizontally
			int width = Ray.startPosX + (mp.player.FOV * Ray.rectWidth);
			double xInc = (Math.PI / 4) / (double)(width / 2);
			double diff = (mp.player.lookAngle + Math.PI / 4) - angle;
			xPos = ((Ray.startPosX + mp.player.FOV * Ray.rectWidth) / 2) + (int)(diff / xInc);
			
			//sprite size
			int halfRectHeight = Ray.rectHeight / 2;
			
			//if it's on the screen with exception to the outer most parts
			if (isInFOV == true){
				ca = (mp.player.lookAngle + Math.PI / 4) - angle;
				if (ca < 0){
					ca += 2 * Math.PI;
				} else if (ca > 2 * Math.PI){
					ca -= 2 * Math.PI;
				}
				
				scaledHypotenuse = distanceToPlayer() * Math.cos(ca);
				curHeight = halfRectHeight / scaledHypotenuse * 30;
				trueScale = (double)Ray.graphicalRectHeight / Ray.rectHeight;
				curHeight *= trueScale;
				curHeight *= 2;
				
			//takes into account the edges of the screen, essentially fixes a visual bug
			} else if (inRangeOutsideFOV == true){
				if ((mp.player.lookAngle + Math.PI / 4) - angle > 0){
					ca = Math.PI / 4;
				} else {
					ca = -Math.PI / 4;
				}
				if (ca < 0){
					ca += 2 * Math.PI;
				} else if (ca > 2 * Math.PI){
					ca -= 2 * Math.PI;
				}
				
				scaledHypotenuse = distanceToPlayer() * Math.cos(ca);
				curHeight = halfRectHeight / scaledHypotenuse * 30;
				trueScale = (double)Ray.graphicalRectHeight / Ray.rectHeight;
				curHeight *= trueScale;
				curHeight *= 2;
			}
			
			//handles standard animation if the entity is not punching
			if (alive == true && punching == false){
				g2.drawImage(images[curImage], xPos - ((int)curHeight / 2)
					, Ray.startPosY - (int)(curHeight / 2), (int)curHeight, (int)curHeight, null);
				if (isMoving == true){
					animationCounter++;
					if (animationCounter > animationTime){
						animationCounter = 0;
						if (curImage + 1 < images.length){
							curImage++;
							if (curImage == images.length - 1){
								curImage = 0;
							}
						}
					}
				} else {
					curImage = 0;
				}
			
			//handles punching animation if the entity is punching
			} else if (alive == true && punching == true){
				g2.drawImage(punchImages[curPunchImage], xPos - ((int)curHeight / 2)
					, Ray.startPosY - (int)(curHeight / 2), (int)curHeight, (int)curHeight, null);
				punchCounter++;
				if (punchCounter > animationTime){
					punchCounter = 0;
					curPunchImage++;
					if (curPunchImage == (int)((double)punchImages.length / 2)){
						mp.player.getHit(this);
					}
					if (curPunchImage == punchImages.length - 1){
						curPunchImage = 0;
						punching = false;
					}
				}
			}
			
			//health bar
			double barWidth = curHeight * ((double)curHealth / maxHealth);
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.RED);
			if (curHealth > 0 && ultimate1 != true){
				g2.drawLine(xPos - ((int)curHeight / 2), Ray.startPosY - (int)(curHeight / 2)
				, xPos - ((int)curHeight / 2) + (int)barWidth - 5, Ray.startPosY - (int)(curHeight / 2));
			}
			
		//if it's on the screen but the game is paused, we still want to draw it but it isn't being updated
		} else if ((isInFOV == true || inRangeOutsideFOV == true) && mp.gameState == mp.PAUSE_STATE && alive == true){
			g2.drawImage(images[curImage], xPos - ((int)curHeight / 2)
				, Ray.startPosY - (int)(curHeight / 2), (int)curHeight, (int)curHeight, null);
		}
		
		//ALL death animations under this if statement
		if (alive == false && (isInFOV == true || inRangeOutsideFOV == true)){
			//standard death animation, if the ultimate is happening then it uses the charred death animation
			if (ultimate1 == true && ultimateExtraDeathTime == false){
				deathCounter = -15;
				ultimateExtraDeathTime = true;
			}
			if (curDeathImage != -1){
				deathCounter++;
			}
			if (deathCounter > animationTime && curDeathImage < deathImages.length - 1){
				curDeathImage++;
				deathCounter = 0;
			} else if (deathCounter > deathFinalImageTime){
				curDeathImage = -1;
				deathCounter = 0;
			}
			if (curDeathImage != -1){
				if (ultimate1 == true){
					g2.drawImage(mp.player.ultimate1DeathImages[curDeathImage], xPos - ((int)curHeight / 2)
					, Ray.startPosY - (int)(curHeight / 2), (int)curHeight, (int)curHeight, null);
				} else {
					g2.drawImage(deathImages[curDeathImage], xPos - ((int)curHeight / 2)
					, Ray.startPosY - (int)(curHeight / 2), (int)curHeight, (int)curHeight, null);
				}
			}
			
			if (curDeathImage == -1){
				mp.visualM.sortedList.remove(arrayListNum);
				mp.visualM.update();
				mp.visualM.sortAllDistances();
			}

			//ultimate 1 lightning animation on top of the charred death animation
			if (ultimate1 == true){
				if (ult1Counter1 != -1){
					ult1Counter1++;
					if (ult1Counter1 > ult1MaxTime1){
						ult1CurImage++;
						ult1Counter1 = 0;
						if (ult1CurImage == 3){
							ult1Counter1 = -1;
						}
					}
				} else if (ult1Counter1 == -1){
					ult1Counter2++;
					ult1LightningCounter++;
					if (ult1Counter2 > ult1MaxTime2){
						ult1CurImage++;
						ult1Counter2 = 0;
						if (ult1CurImage == 6){
							ult1CurImage = 3;
						}
					}
					if (ult1LightningCounter > ult1LightningMax){
						mp.visualM.sortedList.remove(arrayListNum);
						mp.visualM.update();
						mp.visualM.sortAllDistances();
					}
				}
				g2.drawImage(mp.player.ultimate1Images[ult1CurImage], xPos - ((int)curHeight / 2)
					, Ray.startPosY - (int)(curHeight / 2), (int)curHeight, (int)curHeight, null);
			}
		}
	}
}