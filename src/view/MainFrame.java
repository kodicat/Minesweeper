package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.GameModel;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 42L;
	private BoardComponent board;
	private SmileComponent smile;
	
	public MainFrame(GameModel model)
	{
		// set the icon of the frame
		Image img = new ImageIcon("pictures/mine.png").getImage();
		setIconImage(img);
		
		// set location of this window in the middle
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		setLocation(screenWidth / 2 - (getWidth() / 2),
				screenHeight / 2 - (getHeight() / 2));
		
		// set title
		setTitle("Minesweeper");
		
		// user can't resize the window of the game
		setResizable(false);
		
		// close this frame and exit the program
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// pack this frame with mineComponent
		BoardComponent bc = new BoardComponent(model);
		board = bc;
		// create JPanel with FlowLayout to put there smile, timer and bombs
		SmileComponent sc = new SmileComponent(model);
		this.smile = sc;
		JPanel topPanel = new JPanel();
		topPanel.add(sc);
		Container contentPane = this.getContentPane();
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(bc);
		this.pack();
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
	
	public void reset()
	{
		board.reset();
		smile.reset();
	}
	
	public int getFieldBoxSize() {
		return board.getBoxSize();
	}
	
	public int getSmileSize() {
		return smile.getSmileSize();
	}
}
