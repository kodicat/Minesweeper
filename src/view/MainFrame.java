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

import model.BoardModel;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 42L;
	private BoardComponent field;
	private SmileComponent smile;
	
	public MainFrame(BoardModel board)
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
		BoardComponent bc = new BoardComponent(board);
		field = bc;
		// create JPanel with FlowLayout to put there smile, timer and bombs
		SmileComponent sc = new SmileComponent(board);
		this.smile = sc;
		JPanel topPanel = new JPanel();
		topPanel.add(sc);
		Container contentPane = this.getContentPane();
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(bc);
		this.pack();
	}
	
	public void addFieldMouseListener(MouseListener listener) {
		field.addMouseListener(listener);
	}
	
	public void removeFieldMouseListener(MouseListener listener) {
		field.removeMouseListener(listener);
	}
	
	public void addSmileMouseListener(MouseListener listener) {
		smile.addMouseListener(listener);
	}
	
	public void reset()
	{
		field.reset();
		smile.reset();
	}
	
	public int getFieldBoxSize() {
		return field.getBoxSize();
	}
}
