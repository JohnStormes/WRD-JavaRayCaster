package Main;

public class RoundManager{
	public MainPanel mp;
	
	public int curRound = 1;
	public int spawnCounter;
	public int enemiesSpawned;
	public int curEnemies;
	public int enemiesKilled;
	public int enemiesInThisRound;
	public boolean buffer;
	public boolean endBuffer;
	
	public int roomNum;
	
	//round buffer
	private int bufferCounter = 0;
	public int bufferInc = 1;
	public int bufferCount = 0;
	
	//final boss variables
	private int bossSpawnCounter;
	private int bossSlimeShootCounter;
	
	public RoundManager(MainPanel aMp){
		mp = aMp;
		curRound = 1;
		enemiesInThisRound = 5;
		roomNum = 1;
	}
	
	public void update(){
		if (curRound != 5){
			if (buffer != true){
				spawnCounter++;
				if (spawnCounter > mp.FPS * 2 && enemiesSpawned < enemiesInThisRound){
					spawnCounter = 0;
					mp.visualM.spawnEntity(0.75, 50 + ((curRound - 1) * 50), 25);
					enemiesSpawned++;
					curEnemies++;
				}
			} else {
				bufferCounter++;
				if (bufferCounter > 150 && endBuffer != true){
					bufferCounter = 0;
					bufferCount = 0;
					buffer = false;
				}
			}
		} else if (mp.visualM.willy.alive == true){
			bossSpawnCounter++;
			bossSlimeShootCounter++;
			//spawn entities
			if (bossSpawnCounter > 450){
				bossSpawnCounter = 0;
				for (int x = 0; x < 5; x++){
					mp.visualM.spawnEntity(0.75, 200, 25);
				}
			}
			//spawn globs
			if (bossSlimeShootCounter > 150){
				bossSlimeShootCounter = 0;
				mp.visualM.willy.isShooting = true;
				mp.visualM.spawnGlob();
				mp.playSoundEffect(15);
			}
		}
	}
	
	public void increaseRound(){
		if (curRound != 4){
			curRound++;
			curEnemies = 0;
			enemiesSpawned = 0;
			enemiesKilled = 0;
			enemiesInThisRound = 5 + ((curRound - 1) * 3);
			buffer = true;
			if (curRound == 2){
				roomNum = 2;
			} else if (curRound == 4){
				roomNum = 3;
			}
		} else {
			buffer = true;
			endBuffer = true;
			curEnemies = 0;
			enemiesSpawned = 0;
			enemiesKilled = 0;
			enemiesInThisRound = 0;	
		}
	}
}