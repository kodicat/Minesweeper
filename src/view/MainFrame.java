package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.GameModel;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 42L;
	private final String PICTURES_FOLDER = "pictures/canvas/";
	private BoardComponent board;
	private DisplayView bombs;
	private SmileView smile;
	private DisplayView timer;
	private GameModel model;
	
	public MainFrame(GameModel model) throws IOException
	{
		this.model = model;
		// set the icon of the frame
		Image img = new ImageIcon(PICTURES_FOLDER + "ico.gif").getImage();
		setIconImage(img);
		
		setTitle("Minesweeper");
		
		// user can't resize the window of the game
//		setResizable(false);
		
		// close this frame and exit the program
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// pack this frame with mineComponent
		BoardComponent board = new BoardComponent(model);
		this.board = board;
		
		// create JPanel with SpringLayout to put there bombs, smile and timer
		DisplayView bombs = new DisplayView(model, DisplayView.BOMBS);
		SmileView smile = new SmileView(model);
		DisplayView timer = new DisplayView(model, DisplayView.TIMER);
		this.bombs = bombs;
		this.smile = smile;
		this.timer = timer;
		
		ScorePanel scorePanel = new ScorePanel(bombs, smile, timer);
		
		// create upper bound panel
		JPanel upperPanel = createHorizontalBorder("left-upper", "upper",
													"right-upper");
		// create middle bound panel
		JPanel middlePanel = createHorizontalBorder("left-middle", "middle",
													"right-middle");
		// create left bound panel
		JPanel leftTopPanel = createSideBorder(2);
		
		// create right bound panel
		JPanel rightTopPanel = createSideBorder(2);
		
		// pack all top components to the topPanel
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(upperPanel, BorderLayout.NORTH);
		topPanel.add(leftTopPanel, BorderLayout.WEST);
		topPanel.add(rightTopPanel, BorderLayout.EAST);
		topPanel.add(scorePanel, BorderLayout.CENTER);
		topPanel.add(middlePanel, BorderLayout.SOUTH);
		
		// left bottom panel
		JPanel leftBottomPanel = createSideBorder(model.getHeight());
		
		// right bottom panel
		JPanel rightBottomPanel = createSideBorder(model.getHeight());
		
		// fill in board panel
		JPanel boardPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		boardPanel.add(leftBottomPanel);
		boardPanel.add(board);
		boardPanel.add(rightBottomPanel);
		
		// fill in bottom panel
		JPanel bottomPanel = createHorizontalBorder("left-bottom", "bottom",
													"right-bottom");
		
		// fill in content pane
		Container contentPane = getContentPane();
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(boardPanel, BorderLayout.CENTER);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		this.pack();
		
		// set location of this window in the middle
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		setLocation(screenWidth / 2 - (getWidth() / 2),
				screenHeight / 2 - (getHeight() / 2));
	}
	
	public void addBoardMouseListener(MouseListener listener) {
		board.addMouseListener(listener);
	}
	
	public void removeFieldMouseListener(MouseListener listener) {
		board.removeMouseListener(listener);
	}
	
	public int getNumberOfFieldMouseListeners()
	{
		return board.getMouseListeners().length;
	}
	
	public void addSmileMouseListener(MouseListener listener) {
		smile.addMouseListener(listener);
	}
	
	public void reset() {
		board.reset();
		smile.reset();
		bombs.reset();
	}
	
	public void resetTimer() {
		timer.reset();
	}
	
	public int getFieldBoxSize() {
		return board.getBoxSize();
	}
	
	public int getSmileSize() {
		return smile.getSmileSize();
	}
	
	private JPanel createHorizontalBorder(String leftPic,
			String middlePic, String rightPic) throws IOException {
		
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		BufferedImage pic;
		pic = ImageIO.read(new File(PICTURES_FOLDER + leftPic + ".gif"));
		JLabel picLabel = new JLabel(new ImageIcon(pic));
		result.add(picLabel);
		pic = ImageIO.read(new File(PICTURES_FOLDER + middlePic + ".gif"));
		for (int i = 0; i < model.getWidth(); i++) {
			picLabel = new JLabel(new ImageIcon(pic));
			result.add(picLabel);
		}
		pic = ImageIO.read(new File(PICTURES_FOLDER + rightPic + ".gif"));
		picLabel = new JLabel(new ImageIcon(pic));
		result.add(picLabel);
		
		return result;
	}
	
	private JPanel createSideBorder(int height) throws IOException {
		
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		BufferedImage pic = ImageIO.read(new File(PICTURES_FOLDER + "side.gif"));
		for (int i = 0; i < height; i++) {
			JLabel picLabel = new JLabel(new ImageIcon(pic));
			result.add(picLabel);
		}
		return result;
	}
}