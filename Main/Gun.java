package Main;

import java.io.IOException;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Gun{
	private MainPanel mp;
	public BufferedImage image;
	public BufferedImage defaultImage;
	public BufferedImage flashImage;
	public BufferedImage[] recoilAnimationImages;
	public boolean recoiling;
	public boolean fullAuto;
	//MINIMUM FIRE RATE IS 2
	public int fireRate; //1 = 2 frames, higher the number the slower it is
	public int singleShotBuffer;
	public int imageSize;
	public int soundEffectNum;
	
	//damage
	public int damage;
	
	//AMMO
	public int maxMags;
	public int maxAmmoPerMag;
	public int extraAmmo;
	public int curAmmo;
	
	//bullet graphics
	public int bulletYAdjustment;
	public int bulletLength;
	public Color bulletColor;
	
	//FOR THIS CLASS ONLY
	private int count;
	private int noAmmoBlinkCount;
	private int noAmmoBlinkInc;
	public int shootBufferCounter;
	
	//reload
	public boolean reloading;
	public int reloadTime;
	public int reloadCounter;
	public int curReloadImage;
	
	//for picking up
	public boolean available;
	
	public Gun(MainPanel aMp, String defaultImagePath, String flashImagePath, int aImageSize, int aSoundEffectNum
	, int aMaxMags, int aMaxAmmoPerMag, int aSingleShotBuffer, int aDamage, int aReloadTime){
		mp = aMp;
		recoiling = false;
		soundEffectNum = aSoundEffectNum;
		maxMags = aMaxMags;
		maxAmmoPerMag = aMaxAmmoPerMag;
		curAmmo = maxAmmoPerMag;
		extraAmmo = maxMags * maxAmmoPerMag;
		damage = aDamage;
		reloadTime = aReloadTime;
		try {
			defaultImage = ImageIO.read(getClass().getResourceAsStream(defaultImagePath));
			flashImage = ImageIO.read(getClass().getResourceAsStream(flashImagePath));
		} catch (IOException e){
			System.out.println ("error");
		}
		imageSize = aImageSize * (mp.screenHeight / 400);
		count = 0;
		noAmmoBlinkCount = 0;
		noAmmoBlinkInc = 1;
		singleShotBuffer = aSingleShotBuffer;
		shootBufferCounter = singleShotBuffer + 1;
	}
	
	//called to restart flash animation
	public void clickedForFlash(){
		count = 0;
	}
	
	//reload function
	public void reload(){
		if (curAmmo != maxAmmoPerMag && extraAmmo != 0){
			reloading = true;
		}
	}
	
	public void update(Graphics2D g2){
		if (fullAuto != true && reloading != true){
			if (mp.mouseH.leftPressed == true && mp.mouseH.leftPrevPressed == false
			 && fullAuto == false && mp.gameState == mp.PLAY_STATE && curAmmo != 0 && shootBufferCounter > singleShotBuffer){
			 	mp.playSoundEffect(soundEffectNum);
				mp.grid.shoot(this);
				bulletAnimation(g2);
				mp.mouseH.leftPrevPressed = true;
				curAmmo--;
				image = flashImage;
				shootBufferCounter = 0;
			} else {
				if (shootBufferCounter <= singleShotBuffer){
					shootBufferCounter++;
				}
				image = defaultImage;
			}
		} else if (fullAuto == true && reloading != true){
			if (mp.mouseH.leftPressed == true && mp.gameState == mp.PLAY_STATE && shootBufferCounter > singleShotBuffer){
				if (count == 0 && curAmmo != 0){
					mp.playSoundEffect(soundEffectNum);
					curAmmo--;
					mp.grid.shoot(this);
					bulletAnimation(g2);
					image = flashImage;
					count++;
				} else if (count >= fireRate * 2){
					count = 0;
				} else {
					image = defaultImage;
					count++;
				}
			} else {
				image = defaultImage;
				count = 0;
				if (shootBufferCounter <= singleShotBuffer){
					shootBufferCounter++;
				}
			}
		} else if (reloading == true){
			image = defaultImage;
			reloadCounter++;
			if (reloadCounter > reloadTime / 9){
				curReloadImage++;
				reloadCounter = 0;
				if (curReloadImage == 9){
					reloading = false;
					curReloadImage = 0;
					int dif = 0;
					if (extraAmmo >= maxAmmoPerMag){
						dif = maxAmmoPerMag - curAmmo;
					} else {
						dif = maxAmmoPerMag - curAmmo;
						if (dif > extraAmmo){
							dif = extraAmmo;
						}
					}
					curAmmo += dif;
					extraAmmo -= dif;
				}
			}
		}
	}
	
	public void draw(Graphics2D g2){
		//image
		int imageX = mp.screenWidth-(int)(mp.screenWidth/2.4);
		int imageY = Ray.startPosY + Ray.rectHeight - imageSize;
		g2.drawImage(image, imageX, imageY, imageSize, imageSize, null); //drawImage only works with null at the end
		
		//reloading
		if (reloading == true){
			g2.drawImage(mp.invM.reloadImages[curReloadImage], (Player.FOV * Ray.rectWidth - Ray.startPosX - 250)
			, (Ray.startPosY + Ray.rectHeight - 200), 150, 150, null);
		}
		
		//ammo
		String curAmmoString = "";
		String extraAmmoString = String.valueOf(extraAmmo);
		if (maxAmmoPerMag >= 10 && curAmmo < 10){
			curAmmoString += "0";
		}
		if (maxAmmoPerMag >= 100 && curAmmo < 100){
			curAmmoString += "0";
		}
		curAmmoString += curAmmo;
		g2.setFont(mp.invM.newAmmoFont);
		g2.setColor(Color.BLACK);
		if (curAmmo == 0){
			if (noAmmoBlinkCount >= 30){
				noAmmoBlinkInc = -1;
			}
			if (noAmmoBlinkCount < 0){
				noAmmoBlinkInc = 1;
			}
			noAmmoBlinkCount += noAmmoBlinkInc;
			double scale = 1.0 / 60;
			Color opaque = new Color((float)1.0, (float)0.0, (float)0.0, (float)(noAmmoBlinkCount * scale + scale * 30));
			g2.setColor(opaque);
		}
		g2.drawString("" + curAmmoString + ":" + extraAmmoString, mp.screenWidth / 32, mp.screenHeight / 20 * 17);
	}
	
	public void bulletAnimation(Graphics2D g2){
		if (reloading != true){
			int imageX = mp.screenWidth-(int)(mp.screenWidth/2.4);
			int imageY = Ray.startPosY + Ray.rectHeight - imageSize;
			//bullets drawing
			int gunX = imageX + (imageSize / 2);
			int gunY = Ray.startPosY + Ray.rectHeight - bulletYAdjustment;
			int crossHairX = Ray.startPosX + (Ray.rectWidth * mp.player.FOV / 2);
			int crossHairY = Ray.startPosY;
			int ranBulletX = (int)((Math.random() * (gunX - crossHairX - bulletLength - imageSize / 2)));
			int ranBulletX2 = ranBulletX + bulletLength;
			double angle = Math.atan((double)(gunY - crossHairY) / (gunX - crossHairX));
			int linePosY = (int)(ranBulletX * Math.tan(angle));
			int linePosY2 = (int)(ranBulletX2 * Math.tan(angle));
			g2.setColor(bulletColor);
			g2.setStroke(new BasicStroke(2));
			g2.drawLine(ranBulletX + crossHairX, linePosY + Ray.startPosY, ranBulletX2 + crossHairX, linePosY2 + Ray.startPosY);
		}
	}
}