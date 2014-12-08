package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import javax.swing.JComponent;

import utils.MyImageCache;

import model.Cell;
import model.Game;

public class BoardView extends JComponent{

	private static final long serialVersionUID = 42L;
	
	public static final int CELL_SIZE = 16;
	private final HashMap<Integer,Image> BOARD_VALUES_IMAGES = new HashMap<Integer, Image>();
	private final HashMap<Integer,Image> BOARD_STATE_IMAGES = new HashMap<Integer, Image>();
	
	private Game model;
	
	public BoardView(Game model) {
		this.model = model;
		int boardWidth = model.getBoardWidth();
		int boardHeight = model.getBoardHeight();
		initImages();
		setSize(boardWidth * CELL_SIZE, boardHeight * CELL_SIZE);
	}
	
	private void initImages() {
		MyImageCache instance = MyImageCache.getInstance();
		BOARD_VALUES_IMAGES.put(Cell.EMPTY, instance.getImage(MyImageCache.BOARD_EMPTY));
		BOARD_VALUES_IMAGES.put(Cell.ONE, instance.getImage(MyImageCache.BOARD_ONE));
		BOARD_VALUES_IMAGES.put(Cell.TWO, instance.getImage(MyImageCache.BOARD_TWO));
		BOARD_VALUES_IMAGES.put(Cell.THREE, instance.getImage(MyImageCache.BOARD_THREE));
		BOARD_VALUES_IMAGES.put(Cell.FOUR, instance.getImage(MyImageCache.BOARD_FOUR));
		BOARD_VALUES_IMAGES.put(Cell.FIVE, instance.getImage(MyImageCache.BOARD_FIVE));
		BOARD_VALUES_IMAGES.put(Cell.SIX, instance.getImage(MyImageCache.BOARD_SIX));
		BOARD_VALUES_IMAGES.put(Cell.SEVEN, instance.getImage(MyImageCache.BOARD_SEVEN));
		BOARD_VALUES_IMAGES.put(Cell.EIGHT, instance.getImage(MyImageCache.BOARD_EIGHT));
		BOARD_VALUES_IMAGES.put(Cell.BOMB, instance.getImage(MyImageCache.BOARD_MINE));
		BOARD_VALUES_IMAGES.put(Cell.RED_BOMB, instance.getImage(MyImageCache.BOARD_RED_MINE));
		BOARD_VALUES_IMAGES.put(Cell.WRONG, instance.getImage(MyImageCache.BOARD_WRONG));
		BOARD_STATE_IMAGES.put(Cell.CLOSED, instance.getImage(MyImageCache.BOARD_CLOSED));
		BOARD_STATE_IMAGES.put(Cell.FLAG, instance.getImage(MyImageCache.BOARD_FLAG));
		BOARD_STATE_IMAGES.put(Cell.QUESTION, instance.getImage(MyImageCache.BOARD_QUESTION));
		BOARD_STATE_IMAGES.put(Cell.PRESSED, instance.getImage(MyImageCache.BOARD_PRESSED));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Cell[][] values = model.getBoardValues();
		for (int i = 0; i < model.getBoardHeight(); i++) {
			for (int j = 0; j < model.getBoardWidth(); j++) {
				Cell cell = values[i][j];
				Image img = null;
				if (cell.isOpen()) {
					img = BOARD_VALUES_IMAGES.get(cell.getValue());
				}
				if (cell.isNotOpen()) {
					img = BOARD_STATE_IMAGES.get(cell.getState());
				}
				g.drawImage(img, j * CELL_SIZE, i * CELL_SIZE,
							CELL_SIZE, CELL_SIZE, this);
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}
}
