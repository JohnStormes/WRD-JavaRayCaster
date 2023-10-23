package Main;

import javax.swing.JFrame;
import java.awt.Toolkit;

public class Main{
	public static void main(String args[]){
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("William Robinson's dungeon escape");
		
		MainPanel panel = new MainPanel();
		window.add(panel);
		window.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 10), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()) + 10);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.pack();
		window.setVisible(true);
		
		panel.startMainThread();
	}
}