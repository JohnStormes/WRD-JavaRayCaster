package Main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class WilliamRobinson extends Visual{
	MainPanel mp;
	public BufferedImage[] idleImages;
	public BufferedImage[] deathImages;
	public BufferedImage shootImage;
	public double lookAngle;
	public boolean isInFOV;
	public double curHeight;
	public int xPos;
	public double hitBoxRatio = 0.3;
	
	//private temps
	private double angle;
	private boolean inRangeOutsideFOV;
	
	//idle timer
	public int idleTimer;
	
	//shooting slime animation
	public boolean isShooting;
	public int shootCounter;
	
	//health
	public boolean secondBar;
	public boolean thirdBar;
	public boolean fourthBar;
	public int maxHealthPerBar = 2000;
	public int firstBarHealth = maxHealthPerBar;
	public int secondBarHealth = maxHealthPerBar;
	public int thirdBarHealth = maxHealthPerBar;
	public int fourthBarHealth = maxHealthPerBar;
	
	//for death
	public boolean alive = true;
	
	//standard death animation
	public int animationTime = 4;
	private int deathCounter;
	private int curDeathImage;
	private int deathFinalImageTime = 100;
	
	public WilliamRobinson(MainPanel aMp){
		mp = aMp;
		x = mp.grid.increment * 38 - (mp.grid.increment / 2);
		y = mp.grid.increment * 7 - (mp.grid.increment / 2);
		
		//idle image paths
		String[] idleImagePaths = {"/src/willy/wr1.png", "/src/willy/wr2.png", "/src/willy/wr3.png", "/src/willy/wr4.png", "/src/willy/wr5.png", "/src/willy/wr6.png"
		, "/src/willy/wr7.png", "/src/willy/wr8.png", "/src/willy/wr9.png", "/src/willy/wr10.png", "/src/willy/wr11.png", "/src/willy/wr12.png", "/src/willy/wr13.png"
		, "/src/willy/wr14.png", "/src/willy/wr15.png", "/src/willy/wr16.png", "/src/willy/wr17.png", "/src/willy/wr18.png", "/src/willy/wr19.png", "/src/willy/wr20.png"
		, "/src/willy/wr21.png", "/src/willy/wr22.png", "/src/willy/wr23.png", "/src/willy/wr24.png", "/src/willy/wr25.png", "/src/willy/wr26.png"
		, "/src/willy/wr27.png"};
		
		String[] deathImagePaths = {"/src/willy/wr_death1.png", "/src/willy/wr_death2.png", "/src/willy/wr_death3.png", "/src/willy/wr_death4.png", 
			"/src/willy/wr_death5.png", "/src/willy/wr_death6.png", "/src/willy/wr_death7.png", "/src/willy/wr_death8.png", "/src/willy/wr_death9.png"};
		idleImages = new BufferedImage[27];
		deathImages = new BufferedImage[9];
		
		try {
			for (int x = 0; x < idleImages.length; x++){
				idleImages[x] = ImageIO.read(getClass().getResourceAsStream(idleImagePaths[x]));
			}
			for (int x = 0; x < deathImagePaths.length; x++){
				deathImages[x] = ImageIO.read(getClass().getResourceAsStream(deathImagePaths[x]));
			}
			shootImage = ImageIO.read(getClass().getResourceAsStream("/src/willy/wr_angry.png"));
		} catch (IOException e){
			System.out.println ("error loading William Robinson");
		}
	}
	
	public void getHit(int damage){
		if (firstBarHealth != 0){
			firstBarHealth -= damage;
			if (firstBarHealth < 0){
				firstBarHealth = 0;
			}
		} else if (secondBar == true && secondBarHealth != 0){
			secondBarHealth -= damage;
			if (secondBarHealth < 0){
				secondBarHealth = 0;
			}
		} else if (thirdBar == true && thirdBarHealth != 0){
			thirdBarHealth -= damage;
			if (thirdBarHealth < 0){
				thirdBarHealth = 0;
			}
		} else if (fourthBar == true && fourthBarHealth != 0){
			fourthBarHealth -= damage;
			if (fourthBarHealth < 0){
				fourthBarHealth = 0;
			}
		}
		if (firstBarHealth == 0 && secondBarHealth == 0 && thirdBarHealth == 0 && fourthBarHealth == 0){
			mp.stopFinalMusic();
			mp.playSoundEffect(16);
			for (Entity entity : mp.visualM.entities){
				entity.alive = false;
			}
			alive = false;
		}
	}
	
	public double distanceToPlayer(){
		double dist = Math.sqrt(Math.pow(mp.player.playerX - x, 2) + Math.pow(mp.player.playerY - y, 2));
		return dist;
	}
	
	public void update(){
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
	}
	
	public void draw(Graphics2D g2){
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
			idleTimer++;
			if (idleTimer > 52){
				idleTimer = 0;
			}
			
			if (isShooting != true && alive == true){
				g2.drawImage(idleImages[idleTimer / 2], xPos - (int)(curHeight * 3 / 2)
				, Ray.startPosY - (int)(curHeight * 3 / 2), (int)curHeight * 3, (int)curHeight * 3, null);
			} else if (alive == true){
				shootCounter++;
				if (shootCounter > 10){
					isShooting = false;
					shootCounter = 0;
				}
				g2.drawImage(shootImage, xPos - (int)(curHeight * 3 / 2)
				, Ray.startPosY - (int)(curHeight * 3 / 2), (int)curHeight * 3, (int)curHeight * 3, null);
			}
		} else if ((isInFOV == true || inRangeOutsideFOV == true) && mp.gameState == mp.PAUSE_STATE){
			if (isShooting != true && alive == true){
				g2.drawImage(idleImages[idleTimer / 2], xPos - (int)(curHeight * 3 / 2)
				, Ray.startPosY - (int)(curHeight * 3 / 2), (int)curHeight * 3, (int)curHeight * 3, null);
			} else if (alive == true){
				shootCounter++;
				if (shootCounter > 10){
					isShooting = false;
					shootCounter = 0;
				}
				g2.drawImage(shootImage, xPos - (int)(curHeight * 3 / 2)
				, Ray.startPosY - (int)(curHeight * 3 / 2), (int)curHeight * 3, (int)curHeight * 3, null);
			} else {
				g2.drawImage(deathImages[curDeathImage], xPos - ((int)curHeight * 3 / 2)
				, Ray.startPosY - (int)(curHeight * 3 / 2), (int)curHeight * 3, (int)curHeight * 3, null);
			}
		}
		
		if (alive == false && (isInFOV == true || inRangeOutsideFOV == true)){
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
				g2.drawImage(deathImages[curDeathImage], xPos - ((int)curHeight * 3 / 2)
				, Ray.startPosY - (int)(curHeight * 3 / 2), (int)curHeight * 3, (int)curHeight * 3, null);
			}
			
			if (curDeathImage == -1){
				mp.visualM.sortedList.remove(arrayListNum);
				mp.visualM.update();
				mp.visualM.sortAllDistances();
				mp.gameState = mp.WIN_STATE;
			}
		}
		
		//health bar
		//health bar width is 600
		double inc = (double)150 / maxHealthPerBar;
		g2.setStroke(new BasicStroke(5));
		g2.setColor(Color.BLACK);
		g2.drawRect((Ray.startPosX + Ray.rectWidth * Player.FOV) / 2 - 300, (Ray.startPosY - Ray.rectHeight) + 50, 600, 50);
		//first bar health
		g2.setColor(Color.YELLOW);
		double length = inc * firstBarHealth;
		g2.fillRect((Ray.startPosX + Ray.rectWidth * Player.FOV) / 2 + 150, (Ray.startPosY - Ray.rectHeight) + 50, (int)length, 50);
		
		//second bar health
		length = inc * secondBarHealth;
		g2.setColor(Color.ORANGE);
		g2.fillRect((Ray.startPosX + Ray.rectWidth * Player.FOV) / 2, (Ray.startPosY - Ray.rectHeight) + 50, (int)length, 50);
		
		//third bar health
		length = inc * thirdBarHealth;
		g2.setColor(Color.RED);
		g2.fillRect((Ray.startPosX + Ray.rectWidth * Player.FOV) / 2 - 150, (Ray.startPosY - Ray.rectHeight) + 50, (int)length, 50);
		
		//fourth bar health
		length = inc * fourthBarHealth;
		g2.setColor(Color.MAGENTA);
		g2.fillRect((Ray.startPosX + Ray.rectWidth * Player.FOV) / 2 - 300, (Ray.startPosY - Ray.rectHeight) + 50, (int)length, 50);
	}
}