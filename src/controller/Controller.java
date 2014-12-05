package controller;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

import view.BoardView;
import view.MainFrame;
import view.SmileView;
import model.Game;

public class Controller {

	private Game model;
	private MainFrame view;
	
	// button variables
	private static final int LEFT_BUTTON = 1;
	private static final int BOTH_BUTTON = 2;
	private static final int RIGHT_BUTTON = 3;
	
	
	public Controller(Game model, MainFrame view) {
		this.model = model;
		this.view = view;
		
		view.addBoardMouseListener(new BoardMouseListener());
		view.addSmileMouseListener(new SmileMouseListener());
	}
	
	public void renewTimer() {
		view.repaintTimer();
	}
	
	private class BoardMouseListener extends MouseInputAdapter {
		
		int leftButtonDown = MouseEvent.BUTTON1_DOWN_MASK;
		int rightButtonDown = MouseEvent.BUTTON3_DOWN_MASK;
		int bothDown = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
		int pressedButton = 0;
		
		public void mousePressed(MouseEvent e) {
			int column = e.getX() / BoardView.BOX_SIZE;
			int row = e.getY() / BoardView.BOX_SIZE;
			int button = e.getButton();

			if ((e.getModifiersEx() & (bothDown)) == leftButtonDown) {
				pressedButton = LEFT_BUTTON;
				model.openPress(row, column);
			}
			
			else if ((e.getModifiersEx() & (bothDown)) == rightButtonDown) {
				pressedButton = RIGHT_BUTTON;
				model.flagPress(row, column);
			}
			
			else if ((e.getModifiersEx() & (bothDown)) == bothDown
					|| button == BOTH_BUTTON) {
				pressedButton = BOTH_BUTTON;
				model.openNeighborsPress(row, column);
			}
			
			view.repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			int column = e.getX() / BoardView.BOX_SIZE;
			int row = e.getY() / BoardView.BOX_SIZE;

			if (pressedButton == LEFT_BUTTON) {
				model.openRelease(row, column);
			}
			
			else if (pressedButton == RIGHT_BUTTON) {
				model.flagRelease(row, column);
			}
			
			else if (pressedButton == BOTH_BUTTON) {
				model.openNeighborsRelease(row, column);
			}
			pressedButton = 0;
			
			if (model.isFinishedGame()) {
				view.removeBoardMouseListener(this);
			}
			
			view.repaint();
		}
	}
	
	private class SmileMouseListener extends MouseInputAdapter {
		
		public void mousePressed(MouseEvent e) {
			int button = e.getButton();
			int X = e.getX();
			// add constraints for X axis because of SpringLayout enlargements
			boolean inSmileBounds = X > 0 && X < SmileView.SMILE_SIZE;
			
			if (button == LEFT_BUTTON && inSmileBounds) {
				model.pressSmile();
			}
			view.repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			int button = e.getButton();
			int X = e.getX();
			// add constraints for X axis because of SpringLayout enlargements
			boolean inSmileBounds = X > 0 && X < SmileView.SMILE_SIZE;
			
			if (button == LEFT_BUTTON && inSmileBounds) {
				model.releaseSmile();
			}
			if (view.getNumberOfFieldMouseListeners() == 0 && inSmileBounds) {
				view.addBoardMouseListener(new BoardMouseListener());
			}
			view.repaint();
		}
	}
}
