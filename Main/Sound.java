package Main;

import javax.sound.sampled.Clip;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;

//sound class, isnt used much just use the methods to change songs and sound effects
public class Sound{
	
	private Clip clip;
	private FloatControl fc;
	private URL soundURL[] = new URL[20];
	public boolean isPlaying = false;
	
	public Sound(){
		soundURL[0] = getClass().getResource("/src/sound_machine_gun_1.wav");
		soundURL[1] = getClass().getResource("/src/gun-gunshot-02.wav");
		soundURL[2] = getClass().getResource("/src/sound_laser_1.wav");
		soundURL[3] = getClass().getResource("/src/sound_laser_2.wav");
		soundURL[4] = getClass().getResource("/src/sniper_shot.wav");
		soundURL[5] = getClass().getResource("/src/death_music.wav");
		soundURL[6] = getClass().getResource("/src/death_sound1.wav");
		soundURL[7] = getClass().getResource("/src/ultimate1_sound.wav");
		soundURL[9] = getClass().getResource("/src/punch1_sound.wav");
		soundURL[10] = getClass().getResource("/src/openDoor.wav");
		soundURL[11] = getClass().getResource("/src/teleport.wav");
		soundURL[12] = getClass().getResource("/src/pick_up_object.wav");
		soundURL[13] = getClass().getResource("/src/boss_music.wav");
		soundURL[14] = getClass().getResource("/src/denied.wav");
		soundURL[15] = getClass().getResource("/src/slime_splat.wav");
		soundURL[16] = getClass().getResource("/src/boss_death_sound.wav");
		soundURL[17] = getClass().getResource("/src/giga_chad_music.wav");
		soundURL[18] = getClass().getResource("/src/title_music.wav");
		soundURL[19] = getClass().getResource("/src/background_music.wav");
		
		//test WAV
		soundURL[8] = getClass().getResource("/src/opening_test_sound.wav");
	}
	
	public void setFile(int x){
		try{
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[x]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			if (x == 0){
				fc.setValue(-20);
			}
		} catch(Exception e){
			System.out.println ("sound error");
		}
	}
	public void play(){
		clip.start();
		isPlaying = true;
	}
	public void loop(){
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop(){
		clip.stop();
		isPlaying = false;
	}
}