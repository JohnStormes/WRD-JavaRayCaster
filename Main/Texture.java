package Main;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;

public class Texture{
	MainPanel mp;
	String path;
	BufferedImage image;
	BufferedImage[] textureLines;
	
	public Texture(MainPanel aMp, String aPath){
		mp = aMp;
		path = aPath;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e){
			System.out.println ("couldn't find texture file");
		}
		textureLines = new BufferedImage[64];
		loadTexture();
	}
	
	public void loadTexture(){
		
		for (int x = 0; x < 64; x++){
			textureLines[x] = new BufferedImage(1, 64, BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < 64; y++){
				int curRGB = image.getRGB(x, y);
				textureLines[x].setRGB(0, y, curRGB);
			}
		}
	}
}