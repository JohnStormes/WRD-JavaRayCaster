package Main;

public class CollisionDetection{
	MainPanel mp;
	
	//how close entities (including player) can get to walls for collisions
	public static final int minDistToWall = 2;
	
	//constructor
	public CollisionDetection(MainPanel aMp){
		mp = aMp;
	}
	
	//collisions for player
	public void checkCollisions(Player player)
	{
		boolean moveY = false, moveX = false;
		int curX = (int)(player.playerX / mp.grid.increment);
		int curY = (int)(player.playerY / mp.grid.increment);
		double curAngle = (Math.PI / 4) + player.lookAngle;

		//check all collisions and move player accordingly
		if (player.canMove == true){
			if (mp.keyH.upPressed == true){
				double xInc = player.speed * Math.cos(curAngle);
				double yInc = player.speed * Math.sin(curAngle);
				curX = (int)(player.playerX + (xInc * minDistToWall)) / mp.grid.increment;
				curY = (int)((player.playerY) / mp.grid.increment); 
				if (mp.grid.grid[curY][curX] == 0){
					moveX = true;
				}
				curX = (int)((player.playerX) / mp.grid.increment);
				curY = (int)(player.playerY - (yInc * minDistToWall)) / mp.grid.increment; 
				if (mp.grid.grid[curY][curX] == 0){
					moveY = true;
				}
				
				if (moveX == true){
					player.playerX += xInc;
				}
				if (moveY == true){
					player.playerY -= yInc;
				}
				moveX = false;
				moveY = false;
			}
			if (mp.keyH.leftPressed == true){
				double xInc = player.speed * Math.cos(curAngle);
				double yInc = player.speed * Math.sin(curAngle);
				curX = (int)(player.playerX - (yInc * minDistToWall)) / mp.grid.increment;
				curY = (int)((player.playerY) / mp.grid.increment); 
				if (mp.grid.grid[curY][curX] == 0){
					moveX = true;
				}
				curX = (int)((player.playerX) / mp.grid.increment);
				curY = (int)(player.playerY - (xInc * minDistToWall)) / mp.grid.increment; 
				if (mp.grid.grid[curY][curX] == 0){
					moveY = true;
				}
				
				if (moveX == true){
					player.playerX -= yInc;
				}
				if (moveY == true){
					player.playerY -= xInc;
				}
				moveX = false;
				moveY = false;
			}
			if (mp.keyH.rightPressed == true){
				double xInc = player.speed * Math.cos(curAngle);
				double yInc = player.speed * Math.sin(curAngle);
				curX = (int)(player.playerX + (yInc * minDistToWall)) / mp.grid.increment;
				curY = (int)((player.playerY) / mp.grid.increment); 
				if (mp.grid.grid[curY][curX] == 0){
					moveX = true;
				}
				curX = (int)((player.playerX) / mp.grid.increment);
				curY = (int)(player.playerY + (xInc * minDistToWall)) / mp.grid.increment; 
				if (mp.grid.grid[curY][curX] == 0){
					moveY = true;
				}
				
				if (moveX == true){
					player.playerX += yInc;
				}
				if (moveY == true){
					player.playerY += xInc;
				}
				moveX = false;
				moveY = false;
			}
			if (mp.keyH.downPressed == true){
				double xInc = player.speed * Math.cos(curAngle);
				double yInc = player.speed * Math.sin(curAngle);
				curX = (int)(player.playerX - (xInc * minDistToWall)) / mp.grid.increment;
				curY = (int)((player.playerY) / mp.grid.increment); 
				if (mp.grid.grid[curY][curX] == 0){
					moveX = true;
				}
				curX = (int)((player.playerX) / mp.grid.increment);
				curY = (int)(player.playerY + (yInc * minDistToWall)) / mp.grid.increment; 
				if (mp.grid.grid[curY][curX] == 0){
					moveY = true;
				}
				
				if (moveX == true){
					player.playerX -= xInc;
				}
				if (moveY == true){
					player.playerY += yInc;
				}
				moveX = false;
				moveY = false;
			}
		}
		
		//handle player collisions with entities
		boolean changed;
		changed = false;
		for (int x = 0; x < mp.visualM.entities.length; x++){
			if (player.hitBox.intersects(mp.visualM.entities[x].collisionBox) == true && mp.visualM.entities[x].alive == true){
				mp.visualM.entities[x].isMoving = false;
				changed = true;
				mp.visualM.entities[x].punching = true;
			} else {
				mp.visualM.entities[x].isMoving = true;
			}
		}
		if (changed == false){
			player.canMove = true;
		}
	}
	
	//checks collisions and does movement for entities
	public void checkCollisions(Entity entity)
	{
		//check collisions with player and other entities
		boolean changed;
		changed = false;
		for (int x = 0; x < mp.visualM.entities.length; x++){
			if (x != entity.entityNum && entity.collisionBox.intersects(mp.visualM.entities[x].collisionBox) == true
				&& mp.visualM.entities[x].isMoving == true && mp.visualM.entities[x].alive == true){
				entity.isMoving = false;
				changed = true;
			}
		}
		if (changed == false && entity.isMoving == true){
			entity.isMoving = true;
		}
		
		boolean moveY = false, moveX = false;
		int curX = (int)(entity.x / mp.grid.increment);
		int curY = (int)(entity.y / mp.grid.increment);
		double curAngle = (Math.PI / 4) + entity.lookAngle;

		//movement and collisions with walls
		if (entity.isMoving == true){
			double xInc = entity.speed * Math.cos(curAngle);
			double yInc = entity.speed * Math.sin(curAngle);
			curX = (int)(entity.x + (xInc * minDistToWall)) / mp.grid.increment;
			curY = (int)((entity.y) / mp.grid.increment); 
			if (mp.grid.grid[curY][curX] == 0){
				moveX = true;
			}
			curX = (int)((entity.x) / mp.grid.increment);
			curY = (int)(entity.y - (yInc * minDistToWall)) / mp.grid.increment; 
			if (mp.grid.grid[curY][curX] == 0){
				moveY = true;
			}
			if (moveX == true){
				entity.x += xInc;
			}
			if (moveY == true){
				entity.y -= yInc;
			}
			moveX = false;
			moveY = false;
		}
	}
	
	public void checkCollisions(SlimeBall slime){
		boolean moveY = false, moveX = false;
		int curX = (int)(slime.x / mp.grid.increment);
		int curY = (int)(slime.y / mp.grid.increment);
		double curAngle = (Math.PI / 4) + slime.lookAngle;

		//movement and collisions with walls
		if (slime.isMoving == true){
			//movement
			double xInc = slime.speed * Math.cos(curAngle);
			double yInc = slime.speed * Math.sin(curAngle);
			curX = (int)(slime.x + (xInc * minDistToWall)) / mp.grid.increment;
			curY = (int)((slime.y) / mp.grid.increment); 
			if (mp.grid.grid[curY][curX] == 0){
				moveX = true;
			} else {
				slime.isMoving = false;
			}
			curX = (int)((slime.x) / mp.grid.increment);
			curY = (int)(slime.y - (yInc * minDistToWall)) / mp.grid.increment; 
			if (mp.grid.grid[curY][curX] == 0){
				moveY = true;
			} else {
				slime.isMoving = false;
			}
			if (moveX == true){
				slime.x += xInc;
			}
			if (moveY == true){
				slime.y -= yInc;
			}
			moveX = false;
			moveY = false;
			
			//collision with player
			if (Math.abs(mp.player.playerX - slime.x) < 10 && Math.abs(mp.player.playerY - slime.y) < 10){
				mp.player.getHit(50);
				slime.isMoving = false;
			}
		}
	}
}