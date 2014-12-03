package model;

import java.util.ArrayList;
import java.util.HashMap;

import view.SmileView;

import controller.Controller;


public class Game {

	private boolean isFinished;
	private boolean firstClick;
	
	private int smileState;
	private GameTimer timer;
	private Board board;
	private Controller controller;
	
	
	// final helper variables to indicate smiles
	private final int SMILE_NORMAL = 0;
	private final int SMILE_PRESSED = 1;
	private final int SMILE_DEAD = 2;
	private final int SMILE_COOL = 3;
	private final int SMILE_SCARED = 4;
	
//	/**
//	 * Number of boxes which are not opened: closed, flagged or questioned.
//	 * <p>
//	 * At the beginning of the game it equals to the product of boardWidth and
//	 * boardHeight.
//	 */
	private int closedCellsLeft;
	
	// right click map
	private HashMap<Integer,Integer> RIGHT_CLICK_MAP;
	{
		RIGHT_CLICK_MAP = new HashMap<Integer, Integer>();
		RIGHT_CLICK_MAP.put(Cell.CLOSED, Cell.FLAG);
		RIGHT_CLICK_MAP.put(Cell.FLAG, Cell.QUESTION);
		RIGHT_CLICK_MAP.put(Cell.QUESTION, Cell.CLOSED);
	}
	
	int pressedCellRow;
	int pressedCellColumn;
	
	
	/**
	 * Game constructor.
	 * @param boardWidth The width of the playing board.
	 * @param boardHeight The height of the playing board.
	 * @param numBombs The number of bombs on the playing board.
	 */
	public Game(int boardWidth, int boardHeight, int numBombs) {
		board = new Board(boardWidth, boardHeight, numBombs);
		closedCellsLeft = boardHeight * boardWidth;
		resetValues();
	}
	
	private void resetValues() {
		isFinished = false;
		firstClick = true;
		smileState = SMILE_NORMAL;
		pressedCellRow = -1;
		pressedCellColumn = -1;
	}
	
	/**
	 * Check whether we can open neighbors on both click
	 * @param row    The index of the row of current box.
	 * @param column The index of the column of current box.
	 * @return
	 */
	private boolean canOpenNeighbors(int row, int column) {
		
		int value = board.getCell(row, column).getValue();
		int flags = board.flagNeighbors(row, column);
		return value == flags;
	}
	
	/**
	 * Check if the player has won the game.
	 * @return True for winning and false for losing or not yet winning.
	 */
	private boolean wonTheGame() {
		if (board.getNumBombs() == closedCellsLeft && isFinished == false) {
			return true;
		}
		return false;
	}
	
	private String numberToString(int number) {
		int positiveNumber = Math.abs(number);
		String numberString = "" + number;
		// add zeroes and/or minuses if needed to get three character string
		if (number / 10 == 0) {
			numberString = ((number >= 0) ? "00" : "-0") + positiveNumber;
		} else if (number / 100 == 0) {
			numberString = ((number >= 0) ? "0" : "-") + positiveNumber;
		}
		return numberString;
	}
	
	/**
	 * Start a new game when click on a smile.
	 */
	public void leftClickAtSmile() {
		
		if (board.isNotNewGame()) {
			int width = board.getWidth();
			int height = board.getHeight();
			board = new Board(width, height, board.getNumBombs());
			closedCellsLeft = width * height;
			resetValues();
			timer.cancelAndClear();
			controller.tick();
		}
	}
	
	/**
	 * Change picture when press left button on a smile.
	 */
	public void pressSmile() {
		
		smileState = SMILE_PRESSED;
	}
	
	/**
	 * Change picture of a smile back when release the button.
	 */
	public void releaseSmile(int X, int Y) {
		
		smileState = SMILE_NORMAL;
		
		if (X >= 0 && X <= SmileView.SMILE_SIZE
				&& Y >= 0 && Y <= SmileView.SMILE_SIZE) {
			leftClickAtSmile();
		}
		
	}
	
	/**
	 * This method changes the value from CLOSED to OPEN on the first floor of
	 * boardValues3D with coordinates row and column.
	 * <p>
	 * If its corresponding value on the zero floor of boardValues3D equals to
	 * 0 (EMPTY) the method also opens (changes from CLOSED to OPEN) its
	 * neighbors and so on (recursive).
	 * @param row    The index of the row of the box which was clicked on.
	 * @param column The index of the column of the box which was clicked on.
	 */
	public void leftClickAt(int row, int column) {
		
		if (firstClick) {
			firstClick = false;
			
			board.fillIn(row, column);
			
			GameTimer timer = new GameTimer(controller);
			this.timer = timer;
		}
		Cell cell = board.getCell(row, column);
		openCell(cell);
	}
	
	private void openCell(Cell cell) {
		if (cell.isClosed()) {
			cell.open();
			// count that we opened one box
			closedCellsLeft--;
			
			// change variables when lose the game
			if (cell.isBomb()) {
				board.openBombs();
				cell.setValue(Cell.RED_BOMB);
				smileState = SMILE_DEAD;
				isFinished = true;
				timer.cancel();
			}
			// open neighboring cells when clicked on empty box
			else if (cell.isEmpty()) {
				ArrayList<Cell> neighbors = cell.getNeighbors();
				for (Cell neighbor: neighbors) {
					openCell(neighbor);
				}
			}
			
			if (wonTheGame()) {
				smileState = SMILE_COOL;
				isFinished = true;
				timer.cancel();
				board.setFlags();
			}
		}
	}
	
	/**
	 * Open all unflagged and unquestioned boxes near current one.
	 * @param row    The index of the row of the box which was clicked on.
	 * @param column The index of the column of the box which was clicked.
	 */
	public void bothClickAt(int row, int column) {
		Cell cell = board.getCell(row, column);
		if (cell.isOpen()) {
			ArrayList<Cell> neighbors = cell.getNeighbors();
			for (Cell neighbor: neighbors) {
				if (canOpenNeighbors(row, column) && neighbor.isClosed()) {
					openCell(neighbor);
				}
			}
		}
	}
	
	public void leftPressAt(int row, int column) {
		Cell cell = board.getCell(row, column);
		if (cell.isClosed()) {
			pressedCellRow = row;
			pressedCellColumn = column;
			cell.press();
			smileState = SMILE_SCARED;
		}
	}
	
	public void leftReleaseAt(int row, int column) {
		smileState = SMILE_NORMAL;
		Cell cell = board.getCell(pressedCellRow, pressedCellColumn);
		if (cell.isPressed()) {
			cell.close();
		}
		// count as click when pressed and released on the same box
		if (pressedCellRow == row && pressedCellColumn == column) {
			leftClickAt(row, column);
		}
	}
	
	public void rightPressAt(int row, int column) {
		Cell cell = board.getCell(row, column);
		if (cell.isNotOpen()) {
			int changeTo = RIGHT_CLICK_MAP.get(cell.getState());
			cell.setState(changeTo);
		}
	}
	
	public void bothPressAt(int row, int column) {
		Cell cell = board.getCell(row, column);
		if (cell.isOpen()) {
			pressedCellRow = row;
			pressedCellColumn = column;
			for (Cell neighbor: cell.getNeighbors()) {
				if (neighbor.isClosed()) {
					neighbor.press();
				}
			}
			smileState = SMILE_SCARED;
		}
	}
	
	public void bothReleaseAt(int row, int column) {
		
		smileState = SMILE_NORMAL;
		Cell cell = board.getCell(pressedCellRow, pressedCellColumn);
		// change pressed cells to closed (as before)
		for (Cell neighbor: cell.getNeighbors()) {
			if (neighbor.isPressed()) {
				neighbor.close();
			}
		}
		// count as click when pressed and released on the same cell
		if (pressedCellRow == row && pressedCellColumn == column) {
			bothClickAt(row, column);
		}
	}
	
	/**
	 * Get the copy of boardValues3D to use publicly.
	 * @return 3-D int[][][] array, the copy of variable boardValues3D.
	 */
	public int[][][] getBoardValues() {
		int boardWidth = getBoardWidth();
		int boardHeight = getBoardHeight();
		int[][][] result = new int[boardHeight][boardWidth][2];
		for (int row = 0; row < boardHeight; row++) {
			for (int column = 0; column < boardWidth; column++) {
				Cell cell = board.getCell(row, column);
				result[row][column][0] = cell.getValue();
				result[row][column][1] = cell.getState();
			}
		}
		return result;
	}
	
	public int getBoardWidth() {
		return board.getWidth();
	}
	
	public int getBoardHeight() {
		
		return board.getHeight();
	}
	
	public boolean isFinishedGame()
	{
		return isFinished;
	}
	
	public int getSmileState() {
		return smileState;
	}
	
	
	public String bombsToShow() {
		int flagsOnBoard = 0;
		for (int row = 0; row < getBoardHeight(); row++) {
			for (int column = 0; column < getBoardWidth(); column++) {
				Cell cell = board.getCell(row, column);
				if (cell.isFlag()) {
					flagsOnBoard++;
				}
			}
		}
		
		int maxNumber = Math.max(board.getNumBombs() - flagsOnBoard, -99);
		int numberToShow = Math.min(maxNumber, 999);
		String numberString = numberToString(numberToShow);
		return numberString;
	}
	
	public String timerToShow() {
		int time = 0;
		// catch error if timer is not yet initialized
		try {
			time = timer.getTime();
		} catch (NullPointerException e) {}
		int numberToShow = Math.min(time, 999);
		String numberString = numberToString(numberToShow);
		return numberString;
	}
	
	public void addController(Controller controller) {
		this.controller = controller;
	}
}
