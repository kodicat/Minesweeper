package controller;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import view.MainFrame;
import model.GameModel;

public class Controller {

	private GameModel board;
	private MainFrame frame;
	
	// final click variables
	private final int LEFT_CLICK = 1;
	private final int BOTH_CLICK = 2;
	private final int RIGHT_CLICK = 3;
	
	
	public Controller(GameModel board, MainFrame frame)
	{
		this.board = board;
		this.frame = frame;
		
		frame.addFieldMouseListener(new FieldMouseListener());
		frame.addSmileMouseListener(new SmileMouseListener());
	}
	
	public class FieldMouseListener extends MouseInputAdapter {
		
		public void mouseClicked(MouseEvent e) {
			
			int boxSize = frame.getFieldBoxSize();
			int columnIndex = e.getX() / boxSize;
			int rowIndex = e.getY() / boxSize;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				board.leftClickAt(rowIndex, columnIndex);
				}
			if (button == RIGHT_CLICK) {
				board.rightClickAt(rowIndex, columnIndex);
			}
			if (button == BOTH_CLICK) {
				board.bothClickAt(rowIndex, columnIndex);
			}
			if (board.isGameOver() == true) {
				frame.removeFieldMouseListener(this);
			}
			frame.reset();
		}
		
		public void mousePressed(MouseEvent e) {
			
			int boxSize = frame.getFieldBoxSize();
			int columnIndex = e.getX() / boxSize;
			int rowIndex = e.getY() / boxSize;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				board.leftPressAt(rowIndex, columnIndex);
				}
			if (button == BOTH_CLICK) {
				board.bothPressAt(rowIndex, columnIndex);
			}
			frame.reset();
		}
		
		public void mouseReleased(MouseEvent e) {
			
			int boxSize = frame.getFieldBoxSize();
			int columnIndex = e.getX() / boxSize;
			int rowIndex = e.getY() / boxSize;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				board.leftReleaseAt(rowIndex, columnIndex);
				}
			if (button == BOTH_CLICK) {
				board.bothReleaseAt(rowIndex, columnIndex);
			}
			frame.reset();
		}
	}
	
	public class SmileMouseListener extends MouseInputAdapter {
		
		public void mouseClicked(MouseEvent e) {
			
			int button = e.getButton();
			
			if (button == LEFT_CLICK)
			{
				board.leftClickAtSmile();
			}
			// add mouse listener to the frame (board field) when there is none. 
			if (frame.getNumberOfFieldMouseListeners() == 0)
			{
				frame.addFieldMouseListener(new FieldMouseListener());
			}
			frame.reset();
		}
		
		public void mousePressed(MouseEvent e) {
			
			int button = e.getButton();
			
			if (button == LEFT_CLICK)
			{
				board.pressSmile();
			}
			frame.reset();
		}
		
		public void mouseReleased(MouseEvent e) {
			int button = e.getButton();
			
			if (button == LEFT_CLICK) {
				board.releaseSmile(e, frame.getSmileSize());
			}
			frame.reset();
		}
	}
}
