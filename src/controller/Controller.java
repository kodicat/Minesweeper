package controller;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import view.MainFrame;
import model.BoardModel;

public class Controller extends MouseInputAdapter {

	private BoardModel board;
	private MainFrame frame;
	
	// final click variables
	private final int LEFT_CLICK = 1;
	private final int BOTH_CLICK = 2;
	private final int RIGHT_CLICK = 3;
	
	// final variables to indicate smiles
	private final int SMILE_NORMAL = 0;
	private final int SMILE_PUSHED = 1;
	private final int SMILE_DEAD = 2;
	private final int SMILE_COOL = 3;
	private final int SMILE_SCARED = 4;
	
	public Controller(BoardModel board, MainFrame frame)
	{
		this.board = board;
		this.frame = frame;
		
		frame.addFieldMouseListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {
		int boxSize = frame.getFieldBoxSize();
		int columnIndex = e.getX() / boxSize;
		int rowIndex = e.getY() / boxSize;
		int button = e.getButton();
		
		if (button == LEFT_CLICK) {
			board.leftClickAt(rowIndex, columnIndex);
			if (board.valueAt(rowIndex, columnIndex) == 9) { // open bomb
				board.openBombs();
				frame.removeFieldMouseListener(this);
			}
		}
		if (button == RIGHT_CLICK) {
			board.rightClickAt(rowIndex, columnIndex);
		}
		if (button == BOTH_CLICK) {
			board.bothClickAt(rowIndex, columnIndex);
		}
		frame.reset();
	}
}
