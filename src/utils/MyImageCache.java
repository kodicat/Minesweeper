package utils;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class MyImageCache {
	private static final String PICTURES_FOLDER = "pictures" + File.separator;

	//Smiles region
	public static final String SMILE_PATH = "smile" + File.separator;
	public static final String SMILE_NORMAL = "smile";
	public static final String SMILE_WIN = "smile_cool";
	public static final String SMILE_LOSE = "smile_dead";
	public static final String SMILE_SCARED = "smile_scared";
	public static final String SMILE_PRESSED = "smile_pushed";
	//End smiles region
	
	//Canvas region
	public static final String CANVAS_PATH = "canvas" + File.separator;
	public static final String ICON = "ico";
	public static final String CANVAS_LEFT_UPPER_BORDER = "left-upper";
	public static final String CANVAS_UPPER_BORDER = "upper";
	public static final String CANVAS_RIGHT_UPPER_BORDER = "right-upper";
	public static final String CANVAS_LEFT_MIDDLE_BORDER = "left-middle";
	public static final String CANVAS_MIDDLE_BORDER = "middle";
	public static final String CANVAS_RIGHT_MIDDLE_BORDER = "right-middle";
	public static final String CANVAS_LEFT_BOTTOM_BORDER = "left-bottom";
	public static final String CANVAS_BOTTOM_BORDER = "bottom";
	public static final String CANVAS_RIGHT_BOTTOM_BORDER = "right-bottom";
	public static final String CANVAS_SIDE_BORDER = "side";
	//End canvas region
	
	//Board  region
	public static final String BOARD_PATH = "board" + File.separator;
	public static final String BOARD_EMPTY = "empty";
	public static final String BOARD_ONE = "one";
	public static final String BOARD_TWO = "two";
	public static final String BOARD_THREE = "three";
	public static final String BOARD_FOUR = "four";
	public static final String BOARD_FIVE = "five";
	public static final String BOARD_SIX = "six";
	public static final String BOARD_SEVEN = "seven";
	public static final String BOARD_EIGHT = "eight";
	public static final String BOARD_MINE = "mine";
	public static final String BOARD_RED_MINE = "red_mine";
	public static final String BOARD_WRONG = "wrong";
	public static final String BOARD_CLOSED = "closed";
	public static final String BOARD_FLAG = "flag";
	public static final String BOARD_QUESTION = "question";
	public static final String BOARD_PRESSED = "empty";
	//End board region
	
	
	private static final String GIF = ".gif";
	private static final String PNG = ".png";
	
	private static final MyImageCache instance = new MyImageCache();


	private final Map<String, Image> images = new HashMap<String, Image>();
	
	public static MyImageCache getInstance(){
		return instance;
	}
	private MyImageCache(){
		loadSmiles();
		loadCanvas();
		loadBoard();
	}

	private void loadSmiles(){
		images.put(SMILE_NORMAL, new ImageIcon(getGifImagePath(SMILE_NORMAL, SMILE_PATH)).getImage());
		images.put(SMILE_WIN, new ImageIcon(getGifImagePath(SMILE_WIN, SMILE_PATH)).getImage());
		images.put(SMILE_LOSE, new ImageIcon(getGifImagePath(SMILE_LOSE, SMILE_PATH)).getImage());
		images.put(SMILE_SCARED, new ImageIcon(getGifImagePath(SMILE_SCARED, SMILE_PATH)).getImage());
		images.put(SMILE_PRESSED, new ImageIcon(getGifImagePath(SMILE_PRESSED, SMILE_PATH)).getImage());
	}
	
	private void loadCanvas() {
		images.put(ICON, new ImageIcon(getGifImagePath(ICON, CANVAS_PATH)).getImage());
		images.put(CANVAS_LEFT_UPPER_BORDER, new ImageIcon(getGifImagePath(CANVAS_LEFT_UPPER_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_UPPER_BORDER, new ImageIcon(getGifImagePath(CANVAS_UPPER_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_RIGHT_UPPER_BORDER, new ImageIcon(getGifImagePath(CANVAS_RIGHT_UPPER_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_LEFT_MIDDLE_BORDER, new ImageIcon(getGifImagePath(CANVAS_LEFT_MIDDLE_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_MIDDLE_BORDER, new ImageIcon(getGifImagePath(CANVAS_MIDDLE_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_RIGHT_MIDDLE_BORDER, new ImageIcon(getGifImagePath(CANVAS_RIGHT_MIDDLE_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_LEFT_BOTTOM_BORDER, new ImageIcon(getGifImagePath(CANVAS_LEFT_BOTTOM_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_BOTTOM_BORDER, new ImageIcon(getGifImagePath(CANVAS_BOTTOM_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_RIGHT_BOTTOM_BORDER, new ImageIcon(getGifImagePath(CANVAS_RIGHT_BOTTOM_BORDER, CANVAS_PATH)).getImage());
		images.put(CANVAS_SIDE_BORDER, new ImageIcon(getGifImagePath(CANVAS_SIDE_BORDER, CANVAS_PATH)).getImage());
	}
	
	private void loadBoard() {
		images.put(BOARD_EMPTY, new ImageIcon(getPngImagePath(BOARD_EMPTY, BOARD_PATH)).getImage());
		images.put(BOARD_ONE, new ImageIcon(getPngImagePath(BOARD_ONE, BOARD_PATH)).getImage());
		images.put(BOARD_TWO, new ImageIcon(getPngImagePath(BOARD_TWO, BOARD_PATH)).getImage());
		images.put(BOARD_THREE, new ImageIcon(getPngImagePath(BOARD_THREE, BOARD_PATH)).getImage());
		images.put(BOARD_FOUR, new ImageIcon(getPngImagePath(BOARD_FOUR, BOARD_PATH)).getImage());
		images.put(BOARD_FIVE, new ImageIcon(getPngImagePath(BOARD_FIVE, BOARD_PATH)).getImage());
		images.put(BOARD_SIX, new ImageIcon(getPngImagePath(BOARD_SIX, BOARD_PATH)).getImage());
		images.put(BOARD_SEVEN, new ImageIcon(getPngImagePath(BOARD_SEVEN, BOARD_PATH)).getImage());
		images.put(BOARD_EIGHT, new ImageIcon(getPngImagePath(BOARD_EIGHT, BOARD_PATH)).getImage());
		images.put(BOARD_MINE, new ImageIcon(getPngImagePath(BOARD_MINE, BOARD_PATH)).getImage());
		images.put(BOARD_RED_MINE, new ImageIcon(getPngImagePath(BOARD_RED_MINE, BOARD_PATH)).getImage());
		images.put(BOARD_WRONG, new ImageIcon(getPngImagePath(BOARD_WRONG, BOARD_PATH)).getImage());
		images.put(BOARD_CLOSED, new ImageIcon(getPngImagePath(BOARD_CLOSED, BOARD_PATH)).getImage());
		images.put(BOARD_FLAG, new ImageIcon(getPngImagePath(BOARD_FLAG, BOARD_PATH)).getImage());
		images.put(BOARD_QUESTION, new ImageIcon(getPngImagePath(BOARD_QUESTION, BOARD_PATH)).getImage());
		images.put(BOARD_PRESSED, new ImageIcon(getPngImagePath(BOARD_PRESSED, BOARD_PATH)).getImage());
	}
	
	private String getGifImagePath(String imageName, String categoryPath) {
		return getImagePath(imageName, categoryPath, GIF);
	}
	
	private String getPngImagePath(String imageName, String categoryPath) {
		return getImagePath(imageName, categoryPath, PNG);
	}
	
	private String getImagePath(String imageName, String categoryPath, String imageType){
		return PICTURES_FOLDER + categoryPath + imageName + imageType;
	}
	
	public Image getImage(String name){
		return images.get(name);
	}
}
