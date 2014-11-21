package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import model.BoardModel;

public class BoardComponent extends JComponent{

	private static final long serialVersionUID = 42L;
	private final int BOX_SIZE = 16;
	private final String PICTURES_FOLDER = "pictures";
	private BoardModel board;
	private final HashMap<Integer,String> PICTURES_MAP;
	
	// show state final variables
	private final int CLOSED = 0;
	private final int OPEN = 1;
	private final int FLAG = 2;
	private final int QUESTION = 3;
	
	public BoardComponent(BoardModel board)
	{
		this.board = board;
		int boardWidth = board.getWidth();
		int boardHeight = board.getHeight();
		PICTURES_MAP = getPicturesMap();
		
		setSize(boardWidth * BOX_SIZE, boardHeight * BOX_SIZE);
	}
	
	private HashMap<Integer, String> getPicturesMap()
	{
		int[] keys = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		String[] values = {"empty", "one", "two", "three", "four", "five", "six",
				"seven", "eight", "mine", "red_mine", "wrong"};
		
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		for (int i = 0; i < keys.length; i++)
		{
			result.put(keys[i], PICTURES_FOLDER + "/" + values[i] + ".png");
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
		int[][] values = board.getBoardBombInnerValues();
		int[][] show = board.getBoardShowOuterValues();
		for (int i = 0; i < board.getHeight(); i++)
		{
			for (int j = 0; j < board.getWidth(); j++)
			{
				String path = "";
				if (show[i][j] == CLOSED) {
					path = "pictures/closed_tile.png";
				}
				if (show[i][j] == OPEN) {
					// get the path to the file with picture
					path = PICTURES_MAP.get(values[i][j]);
				}
				if (show[i][j] == FLAG) {
					path = "pictures/flag.png";
				}
				if (show[i][j] == QUESTION) {
					path = "pictures/question.png";
				}
				Image img = new ImageIcon(path).getImage();
				g.drawImage(img, j * BOX_SIZE, i * BOX_SIZE,
						BOX_SIZE, BOX_SIZE, this);
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return getSize();
	}
}
