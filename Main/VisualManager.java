package Main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Rectangle;

public class VisualManager{
	private MainPanel mp;
	public Ray[] rays;
	public Entity[] entities;
	public FloorEntity[] floorEntities;
	public WilliamRobinson willy;
	ArrayList<Visual> sortedList;
	Rectangle[] rooms;
	
	public int midRayNum;
	
	private int curEntity;
	
	public VisualManager(MainPanel aMp){
		mp = aMp;
	}
	
	//make rays, initialize sprites
	public void setup(){
		double rayDiff = Math.PI / 180;
		rays = new Ray[mp.player.FOV];
		for (int x = 0; x < rays.length; x++){
			rays[x] = new Ray(mp, rayDiff * x, x);
		}
		entities = new Entity[0];
		
		//floor entities
		int inc = mp.grid.increment;
		floorEntities = new FloorEntity[11];
		floorEntities[0] = new FloorEntity(mp, "/src/question_mark.png", inc * 6 - (inc / 2), inc * 17 - (inc / 2), 0);
		floorEntities[1] = new FloorEntity(mp, "/src/health.png", inc * 14 - (inc / 2), inc * 17 - (inc / 2), 1);
		floorEntities[2] = new FloorEntity(mp, "/src/question_mark.png", inc * 25 - (inc / 2), inc * 20 - (inc / 2), 2);
		floorEntities[3] = new FloorEntity(mp, "/src/health.png", inc * 27 - (inc / 2), inc * 8 - (inc / 2), 3);
		//final room
		floorEntities[4] = new FloorEntity(mp, "/src/question_mark.png", inc * 35 - (inc / 2), inc * 3 - (inc / 2), 4);
		floorEntities[5] = new FloorEntity(mp, "/src/health.png", inc * 35 - (inc / 2), inc * 11 - (inc / 2), 5);
		floorEntities[6] = new FloorEntity(mp, "/src/health.png", inc * 41 - (inc / 2), inc * 3 - (inc / 2), 6);
		floorEntities[7] = new FloorEntity(mp, "/src/health.png", inc * 41 - (inc / 2), inc * 11 - (inc / 2), 7);
		//orbs
		floorEntities[8] = new FloorEntity(mp, "/src/orange_orb.png", inc * 38 - (inc / 2), inc * 11 - (inc / 2), 8);
		floorEntities[9] = new FloorEntity(mp, "/src/red_orb.png", inc * 38 - (inc / 2), inc * 3 - (inc / 2), 9);
		floorEntities[10] = new FloorEntity(mp, "/src/magenta_orb.png", inc * 42 - (inc / 2), inc * 7 - (inc / 2), 10);
		
		//sorted list
		sortedList = new ArrayList<Visual>(rays.length + entities.length);
		
		//add everything to sorted list
		for (int x = 0; x < rays.length; x++){
			sortedList.add(rays[x]);
		}
		for (int x = 0; x < floorEntities.length; x++){
			sortedList.add(floorEntities[x]);
		}
		
		//set spawn rectangles
		rooms = new Rectangle[4];
		rooms[0] = new Rectangle(0, 0, 18, 7);
		rooms[1] = new Rectangle(8, 9, 9, 13);
		rooms[2] = new Rectangle(18, 9, 11, 13);
		rooms[3] = new Rectangle(34, 3, 8, 8);
	}
	
	public void removeFloorEntity(int num){
		sortedList.remove(floorEntities[num]);
		floorEntities[num].exists = false;
	}
	
	public void spawnEntity(double speed, int health, int damage){
		String[] entity1 = {"/src/enemy1_1.png", "/src/enemy1_2.png", "/src/enemy1_3.png", "/src/enemy1_4.png", "/src/enemy1_5.png"
			, "/src/enemy1_6.png", "/src/enemy1_7.png"};
		String[] entity2 = {"/src/enemy2_1.png", "/src/enemy2_2.png", "/src/enemy2_3.png", "/src/enemy2_4.png", "/src/enemy2_5.png"
			, "/src/enemy2_6.png", "/src/enemy2_7.png"};
			
		String[] deathImages = {"/src/enemy2_death1.png", "/src/enemy2_death2.png", "/src/enemy2_death3.png", "/src/enemy2_death4.png"
			, "/src/enemy2_death5.png", "/src/enemy2_death6.png", "/src/enemy2_death7.png", "/src/enemy2_death8.png",};
			
		String[] punchImages1 = {"/src/enemy2_punch1.png", "/src/enemy2_punch2.png", "/src/enemy2_punch3.png", "/src/enemy2_punch4.png"
			, "/src/enemy2_punch5.png", "/src/enemy2_punch6.png", "/src/enemy2_punch7.png"};
		
		int ran = (int)(Math.random() * 2);
		
		Entity newEntity = new Entity(mp, 0, 0, speed, entity1, deathImages, punchImages1, entities.length, 0.3, health, damage, 30, 100);
		if (ran == 0){
			newEntity = new Entity(mp, 0, 0, speed, entity2, deathImages, punchImages1, entities.length, 0.3, health, damage, 30, 100);
		}
		int realXPos, realYPos;
		int xPos = (int)(Math.random() * (mp.grid.grid[0].length * mp.grid.increment));
		int yPos = (int)(Math.random() * (mp.grid.grid.length * mp.grid.increment));
		realXPos = xPos / mp.grid.increment;
		realYPos = yPos / mp.grid.increment;
		int room = mp.roundM.roomNum;
		
		if (mp.roundM.curRound != 5){
			while (mp.grid.grid[realYPos][realXPos] != 0
			|| Math.sqrt(Math.pow(xPos - mp.player.playerX, 2) + Math.pow(yPos - mp.player.playerY, 2)) < 5 * mp.grid.increment
			|| (realXPos > rooms[room - 1].x + rooms[room - 1].width) || (realXPos < rooms[room - 1].x)
			|| (realYPos > rooms[room - 1].y + rooms[room - 1].height) || (realYPos < rooms[room - 1].y)){
				xPos = (int)(Math.random() * (mp.grid.grid[0].length * mp.grid.increment));
				yPos = (int)(Math.random() * (mp.grid.grid.length * mp.grid.increment));
				realXPos = xPos / mp.grid.increment;
				realYPos = yPos / mp.grid.increment;
			}
		} else {
			while (mp.grid.grid[realYPos][realXPos] != 0
			|| Math.sqrt(Math.pow(xPos - mp.visualM.willy.x, 2) + Math.pow(yPos - mp.visualM.willy.y, 2)) > 2 * mp.grid.increment
			|| (realXPos > rooms[room - 1].x + rooms[room - 1].width) || (realXPos < rooms[room - 1].x)
			|| (realYPos > rooms[room - 1].y + rooms[room - 1].height) || (realYPos < rooms[room - 1].y)){
				xPos = (int)(Math.random() * (mp.grid.grid[0].length * mp.grid.increment));
				yPos = (int)(Math.random() * (mp.grid.grid.length * mp.grid.increment));
				realXPos = xPos / mp.grid.increment;
				realYPos = yPos / mp.grid.increment;
			}
		}
		newEntity.x = xPos;
		newEntity.y = yPos;
		Entity[] newEntities = new Entity[entities.length + 1];
		for (int x = 0; x < entities.length; x++){
			newEntities[x] = entities[x];
		}
		newEntities[newEntities.length - 1] = newEntity;
		entities = newEntities;
		sortedList.add(newEntity);
	}
	
	//spawns slime globs in boss fight
	public void spawnGlob(){
		String[] glob = {"/src/slime_ball.png"};
		String[] globBreak = {"/src/slime_ball.png"};
		
		SlimeBall slimeBall = new SlimeBall(mp);
		sortedList.add(slimeBall);
	}
	
	public void spawnWilly(){
		//willy :0
		willy = new WilliamRobinson(mp);
		sortedList.add(willy);
	}
	
	//orders the distances of sprites and rays together, such that when everything is drawn, things that should appear behind walls
	//are rendered before the wall it's behind to give that illusion
	public void sortAllDistances(){
		for (int i = 0; i < sortedList.size(); i++){
			double key = sortedList.get(i).hypotenuse;
			Visual temp = sortedList.get(i);
			int pos = i;
			while (pos > 0 && sortedList.get(pos - 1).hypotenuse < key){
				sortedList.set(pos, sortedList.get(pos - 1));
				pos--;
			}
			sortedList.set(pos, temp);
		}
	}
	
	//updates all the rays and stores a middle ray value for the look direction ray, updates sprites, and sorts
	public void update(){
		sortAllDistances();
		for (int x = 0; x < sortedList.size(); x++) {
			sortedList.get(x).arrayListNum = x;
		}
		for (int x = 0; x < sortedList.size(); x++){
			sortedList.get(x).update();
		}
	}
	
	public void draw(Graphics2D g2){
		for (int x = 0; x < sortedList.size(); x++){
			sortedList.get(x).draw(g2);
		}
	}
}