package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import model.GameModel;

public class DisplayView extends JComponent {

	private static final long serialVersionUID = 42L;
	
	private final int PICTURE_WIDTH = 11;
	private final int PICTURE_HEIGHT = 21;
	private final int COUNTER_WIDTH = 39;
	private final int COUNTER_HEIGHT = 23;
	private final String PICTURES_FOLDER = "pictures/timer/";
	private GameModel model;
	private String type;
	
	public static String BOMBS = "BOMBS";
	public static String TIMER = "TIMER";
	
	public DisplayView(GameModel model, String type) {
		
		this.model = model;
		this.type = type; 
		setSize(COUNTER_WIDTH, COUNTER_HEIGHT);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// set background color
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, COUNTER_WIDTH, COUNTER_HEIGHT);
		
		// 
		String numberToShow = "";
		if (type.equals(BOMBS)) {
			numberToShow = model.bombsToShow();
		}
		else if (type.equals(TIMER)) {
			numberToShow = model.timerToShow();
		}
		
		Image img = null;
		for (int i = 0; i < 3; i++) {
			int margin = 2 * i;
			String path = numberToShow.substring(i, i + 1);
			img = new ImageIcon(PICTURES_FOLDER + path + ".gif").getImage();
			g.drawImage(img, 1 + margin + PICTURE_WIDTH * i, 1,
						PICTURE_WIDTH, PICTURE_HEIGHT, this);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}
}