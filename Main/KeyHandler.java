package Main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class KeyHandler implements KeyListener{
	//booleans which can be accessed to use for events
	public boolean upPressed, downPressed, leftPressed, rightPressed, lookLeftPressed,
	 lookRightPressed, nPressed, nPrevPressed, qPressed, qPrevPressed, ePressed, ePrevPressed; 
	
	private MainPanel mp;
	
	public KeyHandler(MainPanel aMp){
		mp = aMp;
	}
	
	public void keyTyped(KeyEvent e){
		if (e.getKeyChar() == '1'){
			mp.invM.setGun1();
		} else if (e.getKeyChar() == '2'){
			mp.invM.setGun2();
		} else if (e.getKeyChar() == 'r'){
			mp.invM.reloadCurGun();
		} else if (e.getKeyChar() == '3'){
			mp.invM.setGun3();
		}
	}
	
	public void keyPressed(KeyEvent e){
		if (e.getKeyChar() == 'w'){
			upPressed = true;
		} else if (e.getKeyChar() == 'a'){
			leftPressed = true;
		} else if (e.getKeyChar() == 's'){
			downPressed = true;
		} else if (e.getKeyChar() == 'd'){
			rightPressed = true;
		} else if (e.getKeyCode() == 37){
			lookLeftPressed = true;
		} else if (e.getKeyCode() == 39){
			lookRightPressed = true;
		} else if (e.getKeyChar() == 'n'){
			nPressed = true;
		} else if (e.getKeyChar() == 'q'){
			qPressed = true;
			//mp.invM.switchGuns();
		} else if (e.getKeyCode() == 27){
			if (mp.gameState == mp.PLAY_STATE){
				mp.gameState = mp.PAUSE_STATE;
				mp.setCursor(Cursor.getDefaultCursor());
				mp.mouseH.pauseHoldX = mp.mouseH.x;
				mp.mouseH.pauseHoldY = mp.mouseH.y;
			}
		} else if (e.getKeyChar() == 'e'){
			ePressed = true;
			if (ePrevPressed == false && mp.player.ultimate1Available == true){
				mp.grid.ultimate1();
				ePrevPressed = true;
			}
		} else if (e.getKeyChar() == 'p' && mp.gameState == mp.TITLE_STATE){
			mp.stopTitleMusic();
			mp.playBackgroundMusic();
			mp.setCursor(mp.blankCursor);
			mp.gameState = mp.PLAY_STATE;
			mp.mouseH.robot.mouseMove(mp.mouseH.pauseHoldX + 5, mp.mouseH.pauseHoldY);
		}
	}
	
	public void keyReleased(KeyEvent e){
		if (e.getKeyChar() == 'w'){
			upPressed = false;
		} else if (e.getKeyChar() == 'a'){
			leftPressed = false;
		} else if (e.getKeyChar() == 's'){
			downPressed = false;
		} else if (e.getKeyChar() == 'd'){
			rightPressed = false;
		} else if (e.getKeyCode() == 37){
			lookLeftPressed = false;
		} else if (e.getKeyCode() == 39){
			lookRightPressed = false;
		} else if (e.getKeyChar() == 'n'){
			nPressed = false;
			nPrevPressed = false;
		} else if (e.getKeyChar() == 'q'){
			qPressed = false;
			qPrevPressed = false;
		} else if (e.getKeyChar() == 'e'){
			ePressed = false;
			ePrevPressed = false;
		}
	}
}