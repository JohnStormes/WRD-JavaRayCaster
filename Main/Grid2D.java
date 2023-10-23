package Main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Grid2D{
	private MainPanel mp;
	public int[][] grid;
	public int increment;
	public Color backColor = new Color(0,0,0);
	public int backColorNum = 200;
	
	public Grid2D(MainPanel aMp){
		mp = aMp;
		increment = 32;
		innitializeGrid();
	}
	
	public void innitializeGrid(){
		grid = new int[22][51];
		try{
			InputStream is = getClass().getResourceAsStream("/src/Map.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			for (int y = 0; y < grid.length; y++){
				String line = br.readLine();
				for (int x = 0; x < grid[y].length; x++){
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[x]);
					grid[y][x] = num;
				}
			}
			br.close();
		} catch(Exception e){
			System.out.println ("error loading map");
		}
	}
	
	public void update(){
		/*
		if  (mp.keyH.nPressed == true && mp.keyH.nPrevPressed == false){
			randomizeGrid();
			mp.keyH.nPrevPressed = true;
		}
		*/
		
		if (mp.keyH.qPressed == true && mp.keyH.qPrevPressed == false){
			openDoor();
			mp.keyH.qPrevPressed = true;
		}
		
		//checks for item pickups
		pickUpItem();
	}
	
	//not being used but the code is here
	public void randomizeGrid(){
		for(int x = 1; x <= 38; x++){
			for(int i = 1; i <= 8; i++){
					grid[i][x] = 0;
			}
		}
		for(int x = 1; x <= 38; x++){
			for(int i = 1; i <= 8; i++){
				int num = (int)(Math.random() * 4);
				int curX = (int)(mp.player.playerX / increment);
				int curY = (int)(mp.player.playerY / increment);
				if(num == 2 && (i != curY || x != curX))
					grid[i][x] = num;
			}
		}
	}
	
	public void openDoor(){
        int curX = (int)(mp.player.playerX / increment);
        int curY = (int)(mp.player.playerY / increment);
        for(int i = curX - 1; i < curX + 2; i++){
        	for(int z = curY - 1; z < curY + 2; z++){
        		if(((z == curY && i == curX - 1) || (z == curY - 1 && i == curX)
        		 || (z == curY + 1 && i == curX) || (z == curY && i == curX + 1))){
        			if (grid[z][i] == 4 && mp.player.points >= 500){
        				grid[z][i] = 0;
        				mp.playSoundEffect(10);
        			} else if (grid[z][i] == 5 && mp.player.points >= 2400){
        				grid[z][i] = 0;
        				mp.playSoundEffect(10);
        			} else if (grid[z][i] == 6 && mp.player.points >= 3800){
        				grid[z][i] = 0;
        				mp.playSoundEffect(10);
        				mp.stopBackgroundMusic();
        			} else if (grid[z][i] == 7){
        				mp.player.playerX = (increment * 35);
        				mp.player.playerY = (increment * 7);
        				mp.roundM.buffer = false;
        				mp.roundM.endBuffer = false;
        				mp.roundM.curRound = 5;
        				mp.roundM.roomNum = 4;
        				mp.visualM.spawnWilly();
        				mp.playSoundEffect(11);
        				mp.playFinalMusic();
        			} else if (grid[z][i] == 4 || grid[z][i] == 5 || grid[z][i] == 6 || grid[z][i] == 7){
        				mp.playSoundEffect(14);
        			}
        		}
        	}
        }
    }
    
    public void pickUpItem(){
    	int curX = (int)(mp.player.playerX / increment);
        int curY = (int)(mp.player.playerY / increment);
        for (FloorEntity entity : mp.visualM.floorEntities){
        	if (entity.exists == true && curX == (int)(entity.x / increment) && curY == (int)(entity.y / increment)){
        		if (entity.objectNum == 0){
        			mp.playSoundEffect(12);
        			mp.invM.inventory.get(1).available = true;
        			mp.invM.setGun2();
        			mp.visualM.removeFloorEntity(entity.objectNum);
        		} else if (entity.objectNum == 1){
        			mp.playSoundEffect(12);
        			mp.player.curHealth = 100;
        			mp.visualM.removeFloorEntity(entity.objectNum);
        		} else if (entity.objectNum == 2){
        			mp.playSoundEffect(12);
        			mp.invM.inventory.get(2).available = true;
        			mp.invM.setGun3();
        			mp.visualM.removeFloorEntity(entity.objectNum);
        		} else if (entity.objectNum == 3){
        			mp.playSoundEffect(12);
        			mp.player.curHealth = 100;
        			mp.visualM.removeFloorEntity(entity.objectNum);
        		} else if (entity.objectNum == 4){
        			mp.playSoundEffect(12);
        			mp.player.ultimate1Available = true;
        			mp.visualM.removeFloorEntity(entity.objectNum);
        		} else if (entity.objectNum == 5){
        			mp.playSoundEffect(12);
        			mp.player.curHealth = 100;
        			mp.visualM.removeFloorEntity(entity.objectNum);
        		} else if (entity.objectNum == 6){
        			mp.playSoundEffect(12);
        			mp.player.curHealth = 100;
        			mp.visualM.removeFloorEntity(entity.objectNum);
        		} else if (entity.objectNum == 7){
        			mp.playSoundEffect(12);
        			mp.player.curHealth = 100;
        			mp.visualM.removeFloorEntity(entity.objectNum);
        		} else if (entity.objectNum == 8){
        			if (mp.visualM.willy.firstBarHealth == 0){
        				mp.playSoundEffect(12);
        				mp.visualM.willy.secondBar = true;
        				mp.visualM.removeFloorEntity(entity.objectNum);
        			}
        		} else if (entity.objectNum == 9){
        			if (mp.visualM.willy.secondBarHealth == 0){
        				mp.playSoundEffect(12);
        				mp.visualM.willy.thirdBar = true;
        				mp.visualM.removeFloorEntity(entity.objectNum);
        			}
        		} else if (entity.objectNum == 10){
        			if (mp.visualM.willy.thirdBarHealth == 0){
        				mp.playSoundEffect(12);
        				mp.visualM.willy.fourthBar = true;
        				mp.visualM.removeFloorEntity(entity.objectNum);
        			}
        		}
        	}
        }
    }
	
	public void shoot(Gun gun){
		//shooting down walls code if ever desired
		/*
		Ray middleRay = new Ray(mp, Math.PI / 4, -1);
		middleRay.update();
		if (middleRay.finalY != 0 && middleRay.finalY != grid.length - 1 
			&& middleRay.finalX != 0 && middleRay.finalX != grid[0].length - 1){
			grid[middleRay.finalY][middleRay.finalX] = 0;
		}
		*/
		
		//shooting enemies cause that's cooler yesyes
		for (Entity entity : mp.visualM.entities){
			if ((Ray.startPosX + (Ray.rectWidth * Player.FOV / 2)) > 
			(entity.xPos - ((int)(entity.curHeight * entity.collisionBoxWidthRatio) / 2))
			&& (Ray.startPosX + (Ray.rectWidth * Player.FOV / 2)) < 
			(entity.xPos + ((int)(entity.curHeight * entity.collisionBoxWidthRatio) / 2))){
				if (entity.alive == true){
					entity.getHit(gun);	
				}
			}
		}
		
		//shooting boss
		if (mp.roundM.curRound == 5){
			if (mp.visualM.willy.alive == true && (Ray.startPosX + (Ray.rectWidth * Player.FOV / 2)) > 
			(mp.visualM.willy.xPos - ((int)(mp.visualM.willy.curHeight * mp.visualM.willy.hitBoxRatio) / 2))
			&& (Ray.startPosX + (Ray.rectWidth * Player.FOV / 2)) < 
			(mp.visualM.willy.xPos + ((int)(mp.visualM.willy.curHeight * mp.visualM.willy.hitBoxRatio) / 2))){
				mp.visualM.willy.getHit(gun.damage);
			}
		}
	}
	
	//shoots the lightning ultimate
	public void ultimate1(){
		boolean hit = false;
		for (Entity entity : mp.visualM.entities){
			if (entity.distanceToPlayer() < increment * 2 && mp.player.ultimate1Cooldown == false){
				if (entity.alive == true){
					mp.playSoundEffect(7);
					entity.ultimate1 = true;
					hit = true;
					mp.player.points += entity.killValue;	
				}
				entity.alive = false;
			}
		}
		if (hit == true){
			mp.player.ultimate1Cooldown = true;
		}
	}
	
	public void draw(Graphics2D g2){
		//Background
		g2.setColor(Color.WHITE);
		g2.fillRect(Ray.startPosX + Ray.rectWidth, Ray.startPosY - Ray.rectHeight
			, mp.player.FOV * Ray.rectWidth, Ray.rectHeight);
		for(int x=0; x<Ray.rectHeight; x++) {
			double scale = (double)1 / Ray.rectHeight;
			backColor = new Color((float)0.0, (float)0.0, (float)0.0, (float)scale * x);
			g2.setColor(backColor);
			g2.fillRect(Ray.startPosX, Ray.startPosY - Ray.rectHeight + x, (mp.player.FOV + 1) * Ray.rectWidth
				, 1);
		}	
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(Ray.startPosX + Ray.rectWidth, Ray.startPosY, Player.FOV * Ray.rectWidth
			, Ray.rectHeight);
		/*for (int x = 0; x <= Ray.rectHeight; x++){
			double scale = (double)1 / Ray.rectHeight;
			backColor = new Color((float)0.0, (float)0.0, (float)0.0, (float)scale * x);
			g2.setColor(backColor);
			g2.fillRect(Ray.startPosX, Ray.startPosY + Ray.rectHeight - x, (mp.player.FOV + 1) * Ray.rectWidth
			, 1);
		}*/
		double curHeight = ((double)Ray.rectHeight / 2) / (Ray.dof * mp.grid.increment) * 30;
		double trueScale = (double)Ray.graphicalRectHeight / Ray.rectHeight;
		curHeight *= trueScale;
		g2.setColor(Color.BLACK);
		g2.fillRect(Ray.startPosX + 10, Ray.startPosY, mp.player.FOV * Ray.rectWidth, (int)curHeight);
		
		//draw basic grid
		/*
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(2));
		for (int y = 0; y < grid.length + 1; y++){
			g2.drawLine(0,  (y * increment),  ((grid[0].length) * increment),  (y * increment));
		}
		for (int x = 0; x < grid[0].length + 1; x++){
			g2.drawLine( (x * increment), 0,  (x * increment),  ((grid.length) * increment));
		}
		g2.setColor(Color.red);
		for (int i = 0; i < grid.length; i++){
			for (int w = 0; w < grid[i].length; w++){
				if (grid[i][w] == 1){
					g2.fillRect(w * increment, i * increment, increment, increment);
				}
			}
		}
		
		//fill in current square
		g2.setColor(new Color((float)0.0, (float)1.0, (float)1.0, (float)0.4));
		int startX = (((int)mp.player.playerX) / increment) * increment;
		int startY = (((int)mp.player.playerY) / increment) * increment;
		g2.fillRect( startX,  startY, increment, increment);
		*/
	}
}