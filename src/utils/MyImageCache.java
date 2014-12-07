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
	//TODO
	//End board region
	
	
	public static final String GIF = ".gif";
	
	private static final MyImageCache instance = new MyImageCache();


	private final Map<String, Image> images = new HashMap<String, Image>();
	
	public static MyImageCache getInstance(){
		return instance;
	}
	private MyImageCache(){
		loadSmiles();
		loadCanvas();
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
	
	private String getGifImagePath(String imageName, String categoryPath){
		return getImagePath(imageName, categoryPath, GIF);
	}
	
	private String getImagePath(String imageName, String categoryPath, String imageType){
		return PICTURES_FOLDER + categoryPath + imageName + imageType;
	}
	
	public Image getImage(String name){
		return images.get(name);
	}
}
