package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import model.GameModel;

public class BoardView extends JComponent{

	private static final long serialVersionUID = 42L;
	private final int BOX_SIZE = 16;
	private final String PICTURES_FOLDER = "pictures/board/";
	private GameModel model;
	private final HashMap<Integer,String> PICTURES_MAP;
	
	// show state final variables
	private final int CLOSED = 0;
	private final int OPEN = 1;
	private final int FLAG = 2;
	private final int QUESTION = 3;
	private final int PRESSED = 4;
	
	// floor final helper board variables
	private final int ZERO_FLOOR = 0;
	private final int FIRST_FLOOR = 1;
	
	public BoardView(GameModel model)
	{
		this.model = model;
		int boardWidth = model.getWidth();
		int boardHeight = model.getHeight();
		PICTURES_MAP = getPicturesMap();
		
		setSize(boardWidth * BOX_SIZE, boardHeight * BOX_SIZE);
	}
	
	private HashMap<Integer, String> getPicturesMap() {
		int[] keys = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		String[] values = {"empty", "one", "two", "three", "four", "five", "six",
				"seven", "eight", "mine", "red_mine", "wrong"};
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		for (int i = 0; i < keys.length; i++) {
			result.put(keys[i], PICTURES_FOLDER + values[i] + ".png");
		}
		return result;
	}
	
	public void reset()	{
		this.repaint();
	}
	
	public int getBoxSize() {
		return BOX_SIZE;
	}
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		int[][][] values = model.getBoardValues();
		for (int i = 0; i < model.getHeight(); i++) {
			for (int j = 0; j < model.getWidth(); j++) {
				String path = "";
				if (values[i][j][FIRST_FLOOR] == CLOSED) {
					path = PICTURES_FOLDER + "closed_box.png";
				}
				if (values[i][j][FIRST_FLOOR] == OPEN) {
					// get the path to the file with picture
					path = PICTURES_MAP.get(values[i][j][ZERO_FLOOR]);
				}
				if (values[i][j][FIRST_FLOOR] == FLAG) {
					path = PICTURES_FOLDER + "flag.png";
				}
				if (values[i][j][FIRST_FLOOR] == QUESTION) {
					path = PICTURES_FOLDER + "question.png";
				}
				if (values[i][j][FIRST_FLOOR] == PRESSED) {
					path = PICTURES_FOLDER + "empty.png";
				}
				Image img = new ImageIcon(path).getImage();
				g.drawImage(img, j * BOX_SIZE, i * BOX_SIZE,
							BOX_SIZE, BOX_SIZE, this);
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}
}