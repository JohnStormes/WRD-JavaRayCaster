package Main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.function.DoubleToIntFunction;
import java.awt.Toolkit;

public class Ray extends Visual{
	MainPanel mp;
	public double lookAngle;
	private int nextX;
	private int nextY;
	private double angleInc;
	public int numOfRect;
	private int count = 0;
	public static int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	//rendering variables
	public static int rectHeight = (int)(screenHeight / 2.5);
	public static int graphicalRectHeight = rectHeight * 2;
	public static int dof = 20;
	public static int renderDistance;
	public static int startPosX = 0;
	public static int startPosY = screenHeight / 2;
	public static int rectWidth;
	public String hitDir = "";
	
	//temp
	public int newX;
	public int newY;
	private int[][] array;
	private int runCount;
	public String dir = "";
	public int finalX = 0, finalY = 0;
	
	public Ray(MainPanel aMp, double aAngleInc, int aNumOfRect){
		mp = aMp;
		lookAngle = mp.player.lookAngle;
		angleInc = aAngleInc;
		array = new int[100][2];
		numOfRect = aNumOfRect;
		rectWidth = (int) (screenWidth / Player.FOV);
		renderDistance = dof * mp.grid.increment;
	}
	
	public double recursion(){
		double h = 0;
		lookAngle = mp.player.lookAngle + angleInc;
		int r = 0, mx = 0, my = 0, mp1 = 0, dofT = 0;
		int px = (int)(mp.player.playerX / mp.grid.increment * mp.grid.increment);
		int py = (int)(mp.player.playerY / mp.grid.increment * mp.grid.increment);
		int mapX = mp.grid.grid[0].length;
		int mapY = mp.grid.grid.length;
		double rx = 0, ry = 0, ra = 0, xo = 0, yo = 0;
		double disH = Integer.MAX_VALUE;
		ra = lookAngle;
		double vx, vy;
		int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		
		//checking vertical lines
		double Tan = Math.tan(ra);
		double disV = Integer.MAX_VALUE;
		dofT = 0;
		//looking right on map
		if (Math.cos(ra) > 0.001){
			int inc = (px / mp.grid.increment * mp.grid.increment + mp.grid.increment) - px;
			rx = ((px * mp.grid.increment) / mp.grid.increment) + inc;
			ry = (px - rx) * Tan + py;
			xo = mp.grid.increment;
			yo = -xo * Tan;
			dir = "right";
		}
		//looking left on map
		else if (Math.cos(ra) < -0.001){
			int inc = px - (px / mp.grid.increment * mp.grid.increment);
			rx = ((px * mp.grid.increment) / mp.grid.increment) - 0.0001 - inc;
			ry = (px - rx) * Tan + py;
			xo = -mp.grid.increment;
			yo = -xo * Tan;
			dir = "left";
		}
		//looking straight up or down on map
		else {
			rx = px;
			ry = py;
			dofT = dof;
		}
		while (dofT < dof){
			mx = (int)rx / mp.grid.increment;
			my = (int)ry / mp.grid.increment;

			//hits wall
			if (mx >= 0 && my >= 0 && mx < mapX && my < mapY && mp.grid.grid[my][mx] != 0){
				x1 = mx;
				y1 = my;
				dofT = dof;
				disV = Math.cos(ra) * (rx - px) - Math.sin(ra) * (ry - py);
			}
			//next line
			else {
				rx += xo;
				ry += yo;
				dofT += 1;
			}
		}
		vx = rx;
		vy = ry;

		dofT = 0;
		disH = Integer.MAX_VALUE;
		Tan = 1.0 / Tan;
		//checking horizontal lines
		//looking up on map
		if (Math.sin(ra) > 0.001){
			int inc = py - (py / mp.grid.increment * mp.grid.increment);
			ry = ((py * mp.grid.increment) / mp.grid.increment) - 0.0001 - inc;
			rx = (py - ry) * Tan + px;
			yo = -mp.grid.increment;
			xo = -yo * Tan;
			dir = "up";
		}
		//looking down on map
		else if (Math.sin(ra) < -0.001){
			int inc = (py / mp.grid.increment * mp.grid.increment + mp.grid.increment) - py;
			ry = ((py * mp.grid.increment) / mp.grid.increment) + inc;
			rx = (py - ry) * Tan + px;
			yo = mp.grid.increment;
			xo = -yo * Tan;
			dir = "down";
		}
		//looking straight left or right on map
		else {
			rx = px;
			ry = py;
			dofT = dof;
		}
		while (dofT < dof){
			mx = (int)rx / mp.grid.increment;
			my = (int)ry / mp.grid.increment;
			//System.out.println (numOfRect + ", " + rx + ", " + ry + ", " + my + ", " + mx);
			//hits wall
			if (mx >= 0 && my >= 0 && mx < mapX && my < mapY && mp.grid.grid[my][mx] != 0){
				x2 = mx;
				y2 = my;
				dofT = dof;
				disH = Math.cos(ra) * (rx - px) - Math.sin(ra) * (ry - py);
			}
			//next line
			else {
				rx += xo;
				ry += yo;
				dofT += 1;
			}
		}
		if (disV < disH) {
			rx = vx;
			ry = vy;
			disH = disV;
			finalX = x1;
			finalY = y1;
			hitDir = "vertical";
			if (yo < 0){
				dir = "up";
			} else {
				dir = "down";
			}
		} else {
			finalX = x2;
			finalY = y2;
			hitDir = "horizontal";
			if (xo < 0){
				dir = "left";
			} else {
				dir = "right";
			}
		}
		x = rx;
		y = ry;
		return disH;
	}
	
	public void update(){
		runCount = 0;
		newX = (int)mp.player.playerX;
		newY = (int)mp.player.playerY;
		for(int x = 0; x < array.length; x++){
			array[x][0] = 0;
			array[x][1] = 0;
		}
		hypotenuse = recursion();
	}
	
	public void draw(Graphics2D g2){
		//draw look direction
		
		/*
		if (hypotenuse <= renderDistance){
			g2.setColor(Color.YELLOW);
			int lineX = (int)mp.player.playerX;
			int lineY = (int)mp.player.playerY;
			lineX = (int)(mp.player.playerX + hypotenuse * Math.cos(lookAngle));
			lineY = (int)(mp.player.playerY - hypotenuse * Math.sin(lookAngle));
			g2.drawLine((int)mp.player.playerX, (int)mp.player.playerY, lineX, lineY);
		}
		
		//marks all collision points
		g2.setColor(Color.BLUE);
		for (int x = 0; x < array.length; x++){
			if (array[x][0] != 0 && array[x][1] != 0){
				g2.fillOval(array[x][0] - 3, array[x][1] - 3, 6, 6);
			}
		}
		*/
		
		//draw rect
		/*
		if (count == 0){
			if (mp.changable == true){
				mp.ranColor = new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256));
				mp.changable = false;
			}
		}
		g2.setColor(mp.ranColor);
		count++;
		if (count >= 50){
			count = 0;
		}
		*/
		if (hypotenuse >= 0 && hypotenuse <= renderDistance && numOfRect != -1){
			//int colPar1 = 200;
			//int colPar2 = 200;
			//int colPar3 = 200;
			
			int halfRectHeight = rectHeight / 2;
			double ca = (mp.player.lookAngle + Math.PI / 4) - lookAngle;
			if (ca < 0){
				ca += 2 * Math.PI;
			} else if (ca > 2 * Math.PI){
				ca -= 2 * Math.PI;
			}
			double scaledHypotenuse = hypotenuse * Math.cos(ca);
			double curHeight = halfRectHeight / scaledHypotenuse * 30;
			double trueScale = (double)graphicalRectHeight / rectHeight;
			curHeight *= trueScale;
			/*
			double colorScale = (double)255 / renderDistance;
			colPar1 = 255 - (int)(colorScale * hypotenuse);
			colPar2 = 255 - (int)(colorScale * hypotenuse);
			colPar3 = 255 - (int)(colorScale * hypotenuse);
			Color testCol = new Color(colPar1,colPar2,colPar3);
			g2.setColor(testCol);
			g2.fillRect(startPosX + ((mp.player.FOV - numOfRect) * rectWidth), (int)(startPosY - curHeight),
			 rectWidth, (int)(curHeight * 2));
			*/
			
			//TEXTURING
			double inc = (double)mp.grid.increment / 64;
			int rayPos = 0;
			int curTexture = mp.grid.grid[finalY][finalX];
			if (hitDir.equals("vertical")){
				rayPos = (int)((y % mp.grid.increment) / inc);
			} else if (hitDir.equals("horizontal")){
				rayPos = (int)((x % mp.grid.increment) / inc);
			}
			if (dir.equals("up") || dir.equals("down")){
				rayPos = 63 - rayPos;
			}
			if (curTexture != 0){
				g2.drawImage(mp.textM.textures[curTexture - 1].textureLines[rayPos],
				startPosX + ((mp.player.FOV - numOfRect) * rectWidth),
				(int)(startPosY - curHeight), rectWidth, (int)(curHeight * 2), null);
			}
			
			//depth perception
			double colorScale = 1.0 / renderDistance;
			double curShade = colorScale * hypotenuse;
			Color shading = new Color((float)0.0, (float)0.0, (float)0.0, (float)curShade);
			g2.setColor(shading);
			g2.fillRect(startPosX + ((mp.player.FOV - numOfRect) * rectWidth), (int)(startPosY - curHeight),
			 rectWidth, (int)(curHeight * 2));
			
			
			//overlaying black areas
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, mp.screenWidth, startPosY - rectHeight);
			g2.fillRect(0, startPosY + rectHeight, mp.screenWidth, mp.screenHeight - (startPosY + rectHeight));
		}
	}
}