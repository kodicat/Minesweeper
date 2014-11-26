package controller;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

import view.BoardView;
import view.MainFrame;
import view.SmileView;
import model.GameModel;

public class Controller {

	private GameModel model;
	private MainFrame view;
	
	// final click variables
	private final int LEFT_CLICK = 1;
	private final int BOTH_CLICK = 2;
	private final int RIGHT_CLICK = 3;
	
	
	public Controller(GameModel model, MainFrame view) {
		this.model = model;
		this.view = view;
		
		view.addBoardMouseListener(new BoardMouseListener());
		view.addSmileMouseListener(new SmileMouseListener());
	}
	
	public void tick() {
		view.repaintTimer();
	}
	
	private class BoardMouseListener extends MouseInputAdapter {
		
		public void mouseClicked(MouseEvent e) {
			int column = e.getX() / BoardView.BOX_SIZE;
			int row = e.getY() / BoardView.BOX_SIZE;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				model.leftClickAt(row, column);
			}
			if (button == BOTH_CLICK) {
				model.bothClickAt(row, column);
			}
			if (model.isFinishedGame()) {
				view.removeBoardMouseListener(this);
			}
			view.repaint();
		}
		
		public void mousePressed(MouseEvent e) {
			int column = e.getX() / BoardView.BOX_SIZE;
			int row = e.getY() / BoardView.BOX_SIZE;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				model.leftPressAt(row, column);
				}
			if (button == RIGHT_CLICK) {
				model.rightPressAt(row, column);
			}
			if (button == BOTH_CLICK) {
				model.bothPressAt(row, column);
			}
			view.repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			int column = e.getX() / BoardView.BOX_SIZE;
			int row = e.getY() / BoardView.BOX_SIZE;
			int button = e.getButton();

			if (button == LEFT_CLICK) {
				model.leftReleaseAt(row, column);
				}
			if (button == BOTH_CLICK) {
				model.bothReleaseAt(row, column);
			}
			view.repaint();
		}
	}
	
	private class SmileMouseListener extends MouseInputAdapter {
		
		public void mouseClicked(MouseEvent e) {
			int button = e.getButton();
			int X = e.getX();
			// add constraints for X axis because of SpringLayout enlargements
			if (button == LEFT_CLICK && X > 0 && X < SmileView.SMILE_SIZE) {
				System.out.println(X);
				model.leftClickAtSmile();
			}
			// add mouse listener to the frame (board field) when there is none. 
			if (view.getNumberOfFieldMouseListeners() == 0) {
				view.addBoardMouseListener(new BoardMouseListener());
			}
			view.repaint();
		}
		
		public void mousePressed(MouseEvent e) {
			int button = e.getButton();
			int X = e.getX();
			// add constraints for X axis because of SpringLayout enlargements
			if (button == LEFT_CLICK && X > 0 && X < SmileView.SMILE_SIZE) {
				model.pressSmile();
			}
			view.repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			int button = e.getButton();
			int X = e.getX();
			int Y = e.getY();
			// add constraints for X axis because of SpringLayout enlargements
			if (button == LEFT_CLICK) {
				model.releaseSmile(X, Y);
			}
			view.repaint();
		}
	}
}
