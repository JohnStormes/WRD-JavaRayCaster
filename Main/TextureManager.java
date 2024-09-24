package Main;

public class TextureManager{
	MainPanel mp;
	Texture[] textures;
	
	public TextureManager(MainPanel aMp){
		mp = aMp;
		textures = new Texture[30];
		loadTextures();
	}
	
	public void loadTextures(){
		textures[0] = new Texture(mp, "/src/textureImages/WALL-2.png");
		textures[1] = new Texture(mp, "/src/textureImages/WALL_3.png");
		textures[2] = new Texture(mp, "/src/textureImages/WALL_4.png");
		textures[3] = new Texture(mp, "/src/textureImages/door1.png");
		textures[4] = new Texture(mp, "/src/textureImages/door2.png");
		textures[5] = new Texture(mp, "/src/textureImages/door3.png");
		textures[6] = new Texture(mp, "/src/textureImages/portal.png");
	}
}
