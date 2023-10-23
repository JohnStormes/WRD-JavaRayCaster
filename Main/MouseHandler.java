package Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.Robot;
import java.awt.AWTException;

public class MouseHandler implements MouseMotionListener, MouseListener{
	//accessible position of mouse which updates all the time
	public int x; 
	public int y;
	public int pauseHoldX;
	public int pauseHoldY;
	public int prevX = 0;
	public boolean leftPressed, leftPrevPressed;
	
	private MainPanel mp;
	public Robot robot;
	
	public MouseHandler(MainPanel aMp){
		mp = aMp;
		try {
			robot = new Robot();
		} catch (AWTException e){
			System.out.println ("error");
		}
	}
	
	public void setStartMouse(){
		robot.mouseMove(mp.screenWidth / 3, mp.screenHeight / 2);
	}
	
	public void mouseDragged(MouseEvent e){
		if (mp.gameState == mp.PLAY_STATE){
			if (x <= Ray.startPosX){
				robot.mouseMove(mp.screenWidth / 2, mp.screenHeight / 2);
			} else if (x >= Ray.startPosX + Player.FOV * Ray.rectWidth){
				robot.mouseMove(mp.screenWidth / 2, mp.screenHeight / 2);
			}
			if (y < Ray.startPosY - Ray.rectHeight){
				robot.mouseMove(x, mp.screenHeight / 2);
			} else if (y > Ray.startPosY + Ray.rectHeight){
				robot.mouseMove(x, mp.screenHeight / 2);
			}
		}
		x = e.getX();
		y = e.getY();
	}
	
	public void mouseMoved(MouseEvent e){
		if (mp.gameState == mp.PLAY_STATE){
			if (x <= Ray.startPosX){
				robot.mouseMove(mp.screenWidth / 2, mp.screenHeight / 2);
			} else if (x >= Ray.startPosX + Player.FOV * Ray.rectWidth){
				robot.mouseMove(mp.screenWidth / 2, mp.screenHeight / 2);
			}
			if (y < Ray.startPosY - Ray.rectHeight){
				robot.mouseMove(x, mp.screenHeight / 2);
			} else if (y > Ray.startPosY + Ray.rectHeight){
				robot.mouseMove(x, mp.screenHeight / 2);
			}
		}
		x = e.getX();
		y = e.getY();
	}
	public void mouseClicked(MouseEvent e){
		mp.ui.clicked();
	}
	public void mousePressed(MouseEvent e){
		leftPressed = true;
	}
	public void mouseReleased(MouseEvent e){
		leftPressed = false;
		leftPrevPressed = false;
		mp.invM.inventory.get(mp.invM.curGun).clickedForFlash();
		if (mp.invM.inventory.get(mp.invM.curGun).shootBufferCounter == mp.invM.inventory.get(mp.invM.curGun).singleShotBuffer){
			mp.invM.inventory.get(mp.invM.curGun).shootBufferCounter = 0;
		}
	}
	public void mouseEntered(MouseEvent e){
		
	}
	public void mouseExited(MouseEvent e){
		
	}
}