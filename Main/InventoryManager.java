package Main;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class InventoryManager{
	private MainPanel mp;
	public ArrayList<Gun> inventory;
	public ArrayList<Gun> guns;
	public int curGun = 0;
	public Font ammoFont;
	public Font newAmmoFont; //because ammo font is used in some other stuff now and im too lazy to make it all work
	
	//reload images
	public BufferedImage[] reloadImages;
	
	public InventoryManager(MainPanel aMp){
		mp = aMp;
		inventory = new ArrayList<Gun>(2);
		guns = new ArrayList<Gun>();
		ammoFont = new Font("Courier", Font.BOLD, 50);
		newAmmoFont = new Font("Courier", Font.BOLD, 100);
		
		//innitialize reload images
		reloadImages = new BufferedImage[9];
		try {
			reloadImages[0] = ImageIO.read(getClass().getResourceAsStream("/src/reload1.png"));
			reloadImages[1] = ImageIO.read(getClass().getResourceAsStream("/src/reload2.png"));
			reloadImages[2] = ImageIO.read(getClass().getResourceAsStream("/src/reload3.png"));
			reloadImages[3] = ImageIO.read(getClass().getResourceAsStream("/src/reload4.png"));
			reloadImages[4] = ImageIO.read(getClass().getResourceAsStream("/src/reload5.png"));
			reloadImages[5] = ImageIO.read(getClass().getResourceAsStream("/src/reload6.png"));
			reloadImages[6] = ImageIO.read(getClass().getResourceAsStream("/src/reload7.png"));
			reloadImages[7] = ImageIO.read(getClass().getResourceAsStream("/src/reload8.png"));
			reloadImages[8] = ImageIO.read(getClass().getResourceAsStream("/src/reload9.png"));
		} catch (IOException e){
			System.out.println ("error loading reload images");
		}
		
		innitializeGuns();
	}
	
	//initialize assets
	public void innitializeGuns(){
		//create guns
		Gun rayGun1 = new Gun(mp, "/src/ray_gun_1.png", "/src/ray_gun_flash_1.png", 200, 2, 8, 12, 10, 25, 18);
		rayGun1.bulletLength = 60;
		rayGun1.bulletColor = Color.GREEN;
		rayGun1.available = true;
		Gun rayGun2 = new Gun(mp,"/src/ray_gun_2.png", "/src/ray_gun_flash_2.png", 200, 3, 10, 30, 3, 30, 18);
		rayGun2.bulletLength = 60;
		rayGun2.bulletColor = Color.GREEN;
		rayGun2.fullAuto = true;
		rayGun2.fireRate = 3;
		rayGun2.bulletYAdjustment = rayGun2.imageSize / 2;
		Gun machineGun1 = new Gun(mp, "/src/gun_no_flash.png", "/src/gun_flash.png", 300, 1, 8, 100, 1, 25, 36);
		machineGun1.bulletLength = 20;
		machineGun1.bulletColor = Color.ORANGE;
		machineGun1.fullAuto = true;
		machineGun1.fireRate = 1;
		Gun sniper = new Gun(mp, "/src/sniper.png", "/src/sniper_flash.png", 200, 4, 3, 5, 30, 200, 36);
		sniper.bulletYAdjustment = sniper.imageSize / 2;
		sniper.bulletLength = 30;
		sniper.bulletColor = Color.ORANGE;
		
		//TEMP
		/*
		machineGun1.available = true;
		sniper.available = true;
		*/
		
		//add guns to lists
		guns.add(rayGun1);
		guns.add(rayGun2);
		guns.add(machineGun1);
		guns.add(sniper);
		inventory.add(guns.get(0));
		inventory.add(guns.get(2));
		inventory.add(guns.get(3));
	}
	
	public void setGun1(){
		if (inventory.get(0).available == true){
			curGun = 0;
		}
	}
	
	public void setGun2(){
		if (inventory.get(1).available == true){
			curGun = 1;
		}
	}
	
	public void setGun3(){
		if (inventory.get(2).available == true){
			curGun = 2;
		}
	}
	
	public void reloadCurGun(){
		inventory.get(curGun).reload();
	}
	
	public void draw(Graphics2D g2){
		//draw gun from slot in inventory of curGun;
		inventory.get(curGun).update(g2);
		inventory.get(curGun).draw(g2);
	}
}