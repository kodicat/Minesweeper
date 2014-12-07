package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.JComponent;

import model.Game;
import utils.MyImageCache;

public class SmileView extends JComponent {

	private static final long serialVersionUID = 42L;
	public static final int SMILE_SIZE = 26;
	
	private final HashMap<Integer,Image> STATE_IMAGES = new HashMap<Integer, Image>();
	
	private Game model;
	
	public SmileView(Game model) {
		this.model = model;
		initImages();
		setSize(SMILE_SIZE, SMILE_SIZE);
	}
	
	private void  initImages() {
		STATE_IMAGES.put(Game.SMILE_NORMAL, MyImageCache.getInstance().getImage(MyImageCache.SMILE_NORMAL));
		STATE_IMAGES.put(Game.SMILE_PRESSED, MyImageCache.getInstance().getImage(MyImageCache.SMILE_PRESSED));
		STATE_IMAGES.put(Game.SMILE_LOSE, MyImageCache.getInstance().getImage(MyImageCache.SMILE_LOSE));
		STATE_IMAGES.put(Game.SMILE_WIN, MyImageCache.getInstance().getImage(MyImageCache.SMILE_WIN));
		STATE_IMAGES.put(Game.SMILE_SCARED, MyImageCache.getInstance().getImage(MyImageCache.SMILE_SCARED));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int key = model.getSmileState();
		g.drawImage(getImage(key), 0, 0, SMILE_SIZE, SMILE_SIZE, this);
	}
	private Image getImage(int smileState) {
		return STATE_IMAGES.get(smileState);
	}

	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}
}
