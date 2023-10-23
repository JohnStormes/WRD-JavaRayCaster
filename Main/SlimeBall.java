package Main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class SlimeBall extends Visual{
	MainPanel mp;
	public BufferedImage image;
	public double lookAngle;
	public boolean isInFOV;
	public double curHeight;
	public int xPos;
	public boolean isMoving = true;
	public int speed;
	
	//private temps
	private double angle;
	private boolean inRangeOutsideFOV;
	
	//target when shot
	public double targetX;
	public double targetY;
	
	public SlimeBall(MainPanel aMp){
		mp = aMp;
		x = mp.grid.increment * 38 - (mp.grid.increment / 2);
		y = mp.grid.increment * 7 - (mp.grid.increment / 2);
		speed = 6;
		targetX = mp.player.playerX;
		targetY = mp.player.playerY;
		innitialDirection();
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/src/slime_ball.png"));
		} catch (IOException e){
			System.out.println ("error loading slime ball");
		}
	}
	
	public double distanceToPlayer(){
		double dist = Math.sqrt(Math.pow(mp.player.playerX - x, 2) + Math.pow(mp.player.playerY - y, 2));
		return dist;
	}
	
	public void innitialDirection(){
		//angle to the player for rendering (where on the screen should the images appear)
		hypotenuse = distanceToPlayer();
		int horDis = (int)targetX - (int)x;
		int vertDis = (int)targetY - (int)y;
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
		
		mp.cDet.checkCollisions(this);
		if (isMoving == false){
			mp.visualM.sortedList.remove(arrayListNum);
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
			
			g2.drawImage(image, xPos - (int)(curHeight * 3 / 2)
				, Ray.startPosY - (int)(curHeight * 3 / 2), (int)curHeight * 3, (int)curHeight * 3, null);
		} else if ((isInFOV == true || inRangeOutsideFOV == true) && mp.gameState == mp.PAUSE_STATE){
			g2.drawImage(image, xPos - ((int)curHeight * 3 / 2)
				, Ray.startPosY - (int)(curHeight * 3 / 2), (int)curHeight * 3, (int)curHeight * 3, null);
		}
	}
}