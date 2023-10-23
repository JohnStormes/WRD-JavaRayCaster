package Main;

import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.JSlider;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;

public class Settings implements ActionListener, ChangeListener{
	JFrame settingsFrame = new JFrame();
	JPanel visualPanel, audioPanel, backPanel;
	JButton btVisual, btAudio, btQuit;
	JSlider sensitivity, renderDistance;
	JLabel lblSensitivity, lblRenderDistance;
	Color c1, c2;
	Font exitFont;
	
	MainPanel mp;
	
	private double inc = (0.01) / 100;
	
	public Settings(MainPanel aMp){
		mp = aMp;
		
		//font
		exitFont = new Font("Courier", Font.BOLD, 30);
		
		//COLORS
		c1 = new Color(201, 109, 109);
		c2 = new Color(204, 82, 82);
		
		//SLIDER LABELS
		lblSensitivity = new JLabel();
		lblSensitivity.setFont(mp.invM.ammoFont);
		lblSensitivity.setBounds(mp.screenWidth / 16, 50, mp.screenWidth / 2, 50);
		lblSensitivity.setText("sensitivity: " + (int)(mp.player.lookSensitivity / inc));
		lblSensitivity.setHorizontalAlignment(JLabel.CENTER);
		
		lblRenderDistance = new JLabel();
		lblRenderDistance.setFont(mp.invM.ammoFont);
		lblRenderDistance.setBounds(mp.screenWidth / 16, 200, mp.screenWidth / 2, 50);
		lblRenderDistance.setText("render distance: " + 10);
		lblRenderDistance.setHorizontalAlignment(JLabel.CENTER);
		
		//SLIDERS
		sensitivity = new JSlider(10, 100, (int)(mp.player.lookSensitivity / inc));
		sensitivity.setPaintTrack(true);
		sensitivity.setMajorTickSpacing(10);
		sensitivity.setPaintLabels(true);
		sensitivity.setBackground(c2);
		sensitivity.setBounds(mp.screenWidth / 16, 100, mp.screenWidth / 2, 100);
		sensitivity.addChangeListener(this);
		
		renderDistance = new JSlider(4, 40, Ray.dof);
		renderDistance.setPaintTrack(true);
		renderDistance.setMajorTickSpacing(4);
		renderDistance.setPaintLabels(true);
		renderDistance.setBackground(c2);
		renderDistance.setBounds(mp.screenWidth / 16, 250, mp.screenWidth / 2, 100);
		renderDistance.addChangeListener(this);
		
		//BUTTONS
		btVisual = new JButton("visual");
		btVisual.setFocusable(false);
		btVisual.addActionListener(this);
		btVisual.setBounds(mp.screenWidth / 16, 50, mp.screenWidth / 4, 100);
		btVisual.setForeground(Color.MAGENTA);
		btVisual.setBackground(Color.YELLOW);
		btVisual.setFont(mp.invM.ammoFont);
		
		btAudio = new JButton("audio");
		btAudio.setFocusable(false);
		btAudio.addActionListener(this);
		btAudio.setBounds(mp.screenWidth / 16, 200, mp.screenWidth / 4, 100);
		btAudio.setForeground(Color.MAGENTA);
		btAudio.setBackground(Color.LIGHT_GRAY);
		btAudio.setFont(mp.invM.ammoFont);
		
		btQuit = new JButton("return to game");
		btQuit.setFocusable(false);
		btQuit.addActionListener(this);
		btQuit.setBounds(mp.screenWidth / 16, mp.screenHeight - 150, mp.screenWidth / 4, 100);
		btQuit.setForeground(Color.MAGENTA);
		btQuit.setBackground(Color.LIGHT_GRAY);
		btQuit.setFont(exitFont);
		
		//PANELS
		visualPanel = new JPanel();
		visualPanel.setBounds(mp.screenWidth / 8 * 3, 0, mp.screenWidth / 8 * 5, mp.screenHeight);
		visualPanel.setBackground(c2);
		visualPanel.setVisible(true);
		visualPanel.add(sensitivity);
		visualPanel.add(lblSensitivity);
		visualPanel.add(renderDistance);
		visualPanel.add(lblRenderDistance);
		visualPanel.setLayout(null);
		
		audioPanel = new JPanel();
		audioPanel.setBounds(mp.screenWidth / 8 * 3, 0, mp.screenWidth / 8 * 5, mp.screenHeight);
		audioPanel.setBackground(c2);
		audioPanel.setVisible(false);
		audioPanel.setLayout(null);
		
		backPanel = new JPanel();
		backPanel.setBounds(0, 0, mp.screenWidth, mp.screenHeight);
		backPanel.setBackground(c1);
		
		//JFRAME
		settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		settingsFrame.setResizable(false);
		settingsFrame.setTitle("Settings");
		settingsFrame.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 10), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()) + 10);
		settingsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		settingsFrame.setResizable(false);
		settingsFrame.setLocationRelativeTo(null);
		settingsFrame.setLayout(null);
		
		settingsFrame.add(btVisual);
		settingsFrame.add(btAudio);
		settingsFrame.add(btQuit);
		settingsFrame.add(visualPanel);
		settingsFrame.add(audioPanel);
		settingsFrame.add(backPanel);
		settingsFrame.pack();
		settingsFrame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == btVisual){
			audioPanel.setVisible(false);
			btAudio.setBackground(Color.LIGHT_GRAY);
			visualPanel.setVisible(true);
			btVisual.setBackground(Color.YELLOW);
		} else if (e.getSource() == btAudio){
			visualPanel.setVisible(false);
			btVisual.setBackground(Color.LIGHT_GRAY);
			audioPanel.setVisible(true);
			btAudio.setBackground(Color.YELLOW);
		} else if (e.getSource() == btQuit){
			settingsFrame.dispose();
		}
	}
	
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sensitivity){
			lblSensitivity.setText("sensitivity: " + sensitivity.getValue());
			mp.player.lookSensitivity = inc * sensitivity.getValue();
		} else if (e.getSource() == renderDistance){
			lblRenderDistance.setText("render distance: " + renderDistance.getValue());
			Ray.dof = renderDistance.getValue();
			Ray.renderDistance = Ray.dof * mp.grid.increment;
		}
	}
	
	public void update(){
		
	}
}