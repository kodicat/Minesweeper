package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import model.GameModel;

public class BombsComponent extends JComponent {

	private static final long serialVersionUID = 42L;
	
	private final int PICTURE_WIDTH = 11;
	private final int PICTURE_HEIGHT = 21;
	private final int COUNTER_WIDTH = 39;
	private final int COUNTER_HEIGHT = 23;
	private final String PICTURES_FOLDER = "pictures/timer/";
	private final HashMap<Integer,String> PICTURES_MAP;
	private String[] values = {"-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	private GameModel model;
	
	public BombsComponent(GameModel model) {
		
		PICTURES_MAP = getPicturesMap();
		this.model = model;
		setSize(COUNTER_WIDTH, COUNTER_HEIGHT);
		setOpaque(true);
		setBackground(Color.BLACK);
	}
	
	private HashMap<Integer, String> getPicturesMap() {
		int[] keys = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		String[] values = {"-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		for (int i = 0; i < keys.length; i++) {
			result.put(keys[i], PICTURES_FOLDER + values[i] + ".gif");
		}
		return result;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		// TODO
		super.paintComponent(g);
//		System.out.println(isOpaque());
		int numberToShow = model.bombsToShow();
		String numberString = "" + numberToShow;
		if (numberToShow >= 0) {
			if (numberToShow / 10 == 0) {
				numberString = "00" + numberString;
			}
			else if (numberToShow / 100 == 0) {
				numberString = "0" + numberString;
			}
		}
		else {
			if (numberToShow / 10 == 0) {
				numberString = "-0" + numberString.substring(1);
			}
			else {
				numberString = "-" + numberString.substring(1);
			}
		}
		String path1 = numberString.substring(0, 1);
		String path2 = numberString.substring(1, 2);
		String path3 = numberString.substring(2, 3);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, COUNTER_WIDTH, COUNTER_HEIGHT);
		Image img1 = new ImageIcon(PICTURES_FOLDER + "/" + path1 + ".gif").getImage();
		g.drawImage(img1, 1, 1, PICTURE_WIDTH, PICTURE_HEIGHT, this);
		Image img2 = new ImageIcon(PICTURES_FOLDER + "/" + path2 + ".gif").getImage();
		g.drawImage(img2, 14, 1, PICTURE_WIDTH, PICTURE_HEIGHT, this);
		Image img3 = new ImageIcon(PICTURES_FOLDER + "/" + path3 + ".gif").getImage();
		g.drawImage(img3, 27, 1, PICTURE_WIDTH, PICTURE_HEIGHT, this);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}
	
	public void reset()	{
		this.repaint();
	}
}
