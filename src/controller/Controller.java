package controller;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import view.MainFrame;
import model.GameModel;

public class Controller {

	private GameModel model;
	private MainFrame view;
	
	// final click variables
	private final int LEFT_CLICK = 1;
	private final int BOTH_CLICK = 2;
	private final int RIGHT_CLICK = 3;
	private final int SMILE_SIZE;
	
	
	public Controller(GameModel model, MainFrame view) {
		this.model = model;
		this.view = view;
		SMILE_SIZE = view.getSmileSize();
		
		view.addBoardMouseListener(new BoardMouseListener());
		view.addSmileMouseListener(new SmileMouseListener());
	}
	
	public void tick() {
		view.resetTimer();
	}
	
	public class BoardMouseListener extends MouseInputAdapter {
		
		public void mouseClicked(MouseEvent e) {
			
			int boxSize = view.getFieldBoxSize();
			int columnIndex = e.getX() / boxSize;
			int rowIndex = e.getY() / boxSize;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				model.leftClickAt(rowIndex, columnIndex);
				}
			if (button == RIGHT_CLICK) {
				model.rightClickAt(rowIndex, columnIndex);
			}
			if (button == BOTH_CLICK) {
				model.bothClickAt(rowIndex, columnIndex);
			}
			if (model.isGameOver() == true) {
				view.removeFieldMouseListener(this);
			}
			view.reset();
		}
		
		public void mousePressed(MouseEvent e) {
			
			int boxSize = view.getFieldBoxSize();
			int columnIndex = e.getX() / boxSize;
			int rowIndex = e.getY() / boxSize;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				model.leftPressAt(rowIndex, columnIndex);
				}
			if (button == BOTH_CLICK) {
				model.bothPressAt(rowIndex, columnIndex);
			}
			view.reset();
		}
		
		public void mouseReleased(MouseEvent e) {
			
			int boxSize = view.getFieldBoxSize();
			int column = e.getX() / boxSize;
			int row = e.getY() / boxSize;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				model.leftReleaseAt(row, column);
				}
			if (button == BOTH_CLICK) {
				model.bothReleaseAt(row, column);
			}
			view.reset();
		}
	}
	
	public class SmileMouseListener extends MouseInputAdapter {
		
		public void mouseClicked(MouseEvent e) {
			
			int button = e.getButton();
			int X = e.getX();
			// add constraints for X axis because of SpringLayout enlargements
			if (button == LEFT_CLICK && X > 0 && X < SMILE_SIZE) {
				model.leftClickAtSmile();
			}
			// add mouse listener to the frame (board field) when there is none. 
			if (view.getNumberOfFieldMouseListeners() == 0)
			{
				view.addBoardMouseListener(new BoardMouseListener());
			}
			view.reset();
		}
		
		public void mousePressed(MouseEvent e) {
			
			int button = e.getButton();
			int X = e.getX();
			// add constraints for X axis because of SpringLayout enlargements
			if (button == LEFT_CLICK && X > 0 && X < SMILE_SIZE) {
				model.pressSmile();
			}
			view.reset();
		}
		
		public void mouseReleased(MouseEvent e) {
			
			int button = e.getButton();
			int X = e.getX();
			// add constraints for X axis because of SpringLayout enlargements
			if (button == LEFT_CLICK && X > 0 && X < SMILE_SIZE) {
				model.releaseSmile(e, view.getSmileSize());
			}
			view.reset();
		}
	}
}
