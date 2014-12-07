package view;

import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;

import model.Game;
import utils.MyImageCache;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 42L;
	
	private static final Image ICON = MyImageCache.getInstance().getImage(MyImageCache.ICON);
	private static final Image LEFT_UPPER_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_LEFT_UPPER_BORDER);
	private static final Image UPPER_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_UPPER_BORDER);
	private static final Image RIGHT_UPPER_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_RIGHT_UPPER_BORDER);
	private static final Image LEFT_MIDDLE_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_LEFT_MIDDLE_BORDER);
	private static final Image MIDDLE_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_MIDDLE_BORDER);
	private static final Image RIGHT_MIDDLE_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_RIGHT_MIDDLE_BORDER);
	private static final Image LEFT_BOTTOM_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_LEFT_BOTTOM_BORDER);
	private static final Image BOTTOM_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_BOTTOM_BORDER);
	private static final Image RIGHT_BOTTOM_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_RIGHT_BOTTOM_BORDER);
	private static final Image SIDE_BORDER = MyImageCache.getInstance().getImage(MyImageCache.CANVAS_SIDE_BORDER);
	
	// view classes instances
	private DisplayView bombs;
	private SmileView smile;
	private DisplayView timer;
	private BoardView board;
	
	private Game model;
	
	public MainFrame(Game model) throws IOException {
		this.model = model;
		
		setIconImage(ICON);
		
		setTitle("Minesweeper");
		
		// user can't resize the window of the game
		setResizable(false);
		
		// close this frame and exit the program
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create necessary instances
		DisplayView bombs = new DisplayView(model, DisplayView.BOMBS);
		SmileView smile = new SmileView(model);
		DisplayView timer = new DisplayView(model, DisplayView.TIMER);
		BoardView board = new BoardView(model);
		this.bombs = bombs;
		this.smile = smile;
		this.timer = timer;
		this.board = board;
		
		// create JPanel with SpringLayout to put there bombs, smile and timer
		ScorePanel scorePanel = new ScorePanel(bombs, smile, timer);
		
		// create top upper bound panel
		JPanel upperPanel = createHorizontalBorder(LEFT_UPPER_BORDER, UPPER_BORDER,
													RIGHT_UPPER_BORDER);
		// create top middle bound panel
		JPanel middlePanel = createHorizontalBorder(LEFT_MIDDLE_BORDER, MIDDLE_BORDER,
				RIGHT_MIDDLE_BORDER);
		// create top left bound panel
		JPanel leftTopPanel = createSideBorder(2);
		
		// create top right bound panel
		JPanel rightTopPanel = createSideBorder(2);
		
		// pack all top components to the topPanel
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(upperPanel, BorderLayout.NORTH);
		topPanel.add(leftTopPanel, BorderLayout.WEST);
		topPanel.add(rightTopPanel, BorderLayout.EAST);
		topPanel.add(scorePanel, BorderLayout.CENTER);
		topPanel.add(middlePanel, BorderLayout.SOUTH);
		
		// left bottom panel
		JPanel leftBottomPanel = createSideBorder(model.getBoardHeight());
		
		// right bottom panel
		JPanel rightBottomPanel = createSideBorder(model.getBoardHeight());
		
		// fill in board panel
		JPanel boardPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		boardPanel.add(leftBottomPanel);
		boardPanel.add(board);
		boardPanel.add(rightBottomPanel);
		
		// fill in bottom border
		JPanel bottomPanel = createHorizontalBorder(LEFT_BOTTOM_BORDER, BOTTOM_BORDER,
				RIGHT_BOTTOM_BORDER);
		
		// fill in content pane
		Container contentPane = getContentPane();
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(boardPanel, BorderLayout.CENTER);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		// add menu
		setJMenuBar(new MenuView(this));
		
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
	
	public void removeBoardMouseListener(MouseListener listener) {
		board.removeMouseListener(listener);
	}
	
	public int getNumberOfFieldMouseListeners() {
		return board.getMouseListeners().length;
	}
	
	public void addSmileMouseListener(MouseListener listener) {
		smile.addMouseListener(listener);
	}
	
	public void repaint() {
		board.repaint();
		bombs.repaint();
		smile.repaint();
	}
	
	public void repaintTimer() {
		timer.repaint();
	}
	
	//==========================================================================
	// helper methods
	
	private JPanel createHorizontalBorder(Image left, Image center, Image right) throws IOException {
		
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		JLabel picLabel = new JLabel(new ImageIcon(left));
		result.add(picLabel);
		for (int i = 0; i < model.getBoardWidth(); i++) {
			picLabel = new JLabel(new ImageIcon(center));
			result.add(picLabel);
		}
		picLabel = new JLabel(new ImageIcon(right));
		result.add(picLabel);
		
		return result;
	}
	
	private JPanel createSideBorder(int height) throws IOException {
		
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < height; i++) {
			JLabel picLabel = new JLabel(new ImageIcon(SIDE_BORDER));
			result.add(picLabel);
		}
		return result;
	}
	// end of helper methods
	//==========================================================================
}