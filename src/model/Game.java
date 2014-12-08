package model;

import java.util.ArrayList;
import java.util.HashMap;

import utils.StringUtils;
import controller.Controller;


public class Game {
	// final helper variables to indicate smiles
	public static final int SMILE_NORMAL = 0;
	public static final int SMILE_PRESSED = 1;
	public static final int SMILE_LOSE = 2;
	public static final int SMILE_WIN = 3;
	public static final int SMILE_SCARED = 4;
	

	private boolean isFinished;
	private boolean firstClick;
	private int closedCellsLeft;
	private int pressedCellRow;
	private int pressedCellColumn;
	private int smileState;
	private GameTimer timer;
	private Board board;
	private Controller controller;
	
	// right click map
	private HashMap<Integer,Integer> RIGHT_CLICK_MAP;
	{
		RIGHT_CLICK_MAP = new HashMap<Integer, Integer>();
		RIGHT_CLICK_MAP.put(Cell.CLOSED, Cell.FLAG);
		RIGHT_CLICK_MAP.put(Cell.FLAG, Cell.QUESTION);
		RIGHT_CLICK_MAP.put(Cell.QUESTION, Cell.CLOSED);
	}
	
	
	/**
	 * Game constructor.
	 * @param boardWidth The width of the playing board.
	 * @param boardHeight The height of the playing board.
	 * @param bombsTotal The number of bombs on the playing board.
	 */
	public Game(int boardWidth, int boardHeight, int bombsTotal) {
		board = new Board(boardWidth, boardHeight, bombsTotal);
		closedCellsLeft = boardHeight * boardWidth;
		resetHelperValues();
	}
	
	private void resetHelperValues() {
		isFinished = false;
		firstClick = true;
		smileState = SMILE_NORMAL;
		pressedCellRow = -1;
		pressedCellColumn = -1;
	}
	
	/**
	 * Check if the player has won the game.
	 * @return True for winning and false for losing or not yet winning.
	 */
	private boolean wonTheGame() {
		if (board.getBombsTotal() == closedCellsLeft && isFinished == false) {
			return true;
		}
		return false;
	}
	
	/**
	 * Start a new game when click on a smile.
	 */
	
	public void pressSmile() {
		smileState = SMILE_PRESSED;
	}
	
	public void releaseSmile() {
		smileState = SMILE_NORMAL;
		restart();
	}
	
	public void restart() {
		if (isGameStarted()) {
			int width = board.getWidth();
			int height = board.getHeight();
			board = new Board(width, height, board.getBombsTotal());
			closedCellsLeft = width * height;
			resetHelperValues();
			timer.cancelAndClear();
			controller.renewTimer();
		}
	}

	
	private boolean isGameStarted() {
		return !firstClick;
	}

	public void openPress(int row, int column) {
		Cell cell = board.getCell(row, column);
		if (cell.isClosed()) {
			pressedCellRow = row;
			pressedCellColumn = column;
			cell.press();
			smileState = SMILE_SCARED;
		}
	}
	
	public void openRelease(int row, int column) {
		smileState = SMILE_NORMAL;
		Cell cell = board.getCell(pressedCellRow, pressedCellColumn);
		if (cell.isPressed()) {
			cell.close();
		}
		// count as click when pressed and released on the same box
		if (pressedCellRow == row && pressedCellColumn == column) {
			openCell(row, column);
		}
	}
	
	public void openCell(int row, int column) {
		if (firstClick) {
			firstClick = false;
			board.fillIn(row, column);
			this.timer = new GameTimer(controller);
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
				smileState = SMILE_LOSE;
				isFinished = true;
				timer.cancel();
			}
			// open neighboring cells when clicked on empty box
			else if (cell.isEmpty()) {
				ArrayList<Cell> neighbors = cell.getNeighbours();
				for (Cell neighbor: neighbors) {
					openCell(neighbor);
				}
			}
			
			if (wonTheGame()) {
				smileState = SMILE_WIN;
				isFinished = true;
				timer.cancel();
				board.setFlags();
			}
		}
	}
	
	public void flagPress(int row, int column) {
		Cell cell = board.getCell(row, column);
		if (cell.isNotOpen()) {
			pressedCellRow = row;
			pressedCellColumn = column;
		}
	}
	
	public void flagRelease(int row, int column) {
		if (pressedCellRow == row && pressedCellColumn == column) {
			Cell cell = board.getCell(row, column);
			int changeFrom = cell.getState();
			int changeTo = RIGHT_CLICK_MAP.get(changeFrom);
			// check for flag before and after changing
			if (cell.isFlag()) { board.removeFlag(); }
			cell.setState(changeTo);
			if (cell.isFlag()) { board.addFlag(); }
		}
	}
	
	public void openNeighboursPress(int row, int column) {
		Cell cell = board.getCell(row, column);
		if (cell.isOpen()) {
			pressedCellRow = row;
			pressedCellColumn = column;
			for (Cell neighbour: cell.getNeighbours()) {
				if (neighbour.isClosed()) {
					neighbour.press();
				}
			}
			smileState = SMILE_SCARED;
		}
	}
	
	public void openNeighboursRelease(int row, int column) {
		
		smileState = SMILE_NORMAL;
		Cell cell = board.getCell(pressedCellRow, pressedCellColumn);
		// change pressed cells to closed (as before)
		for (Cell neighbor: cell.getNeighbours()) {
			if (neighbor.isPressed()) {
				neighbor.close();
			}
		}
		if (cell.isPressed()) {
			cell.close();
		}
		// count as click when pressed and released on the same cell
		if (pressedCellRow == row && pressedCellColumn == column) {
			openNeighbours(cell);
		}
	}
	
	/**
	 * Open all non-flagged and unquestioned cells near current one if number of
	 * bombs equals to number of flags.
	 * @param row    The index of the row of the cell which was clicked on.
	 * @param column The index of the column of the cell which was clicked on.
	 */
	public void openNeighbours(Cell cell) {
		if (cell.isOpen()) {
			ArrayList<Cell> neighbours = cell.getNeighbours();
			if (canOpenNeighbors(cell)) {
				for (Cell neighbour: neighbours) {
						openCell(neighbour);
				}
			}
		}
	}
	
	/**
	 * Check whether we can open neighbors on both click
	 * @param cell    Cell to check
	 * @return
	 */
	private boolean canOpenNeighbors(Cell cell) {		
		int value = cell.getValue();
		int flags = board.getFlaggedNeighbours(cell);
		return value == flags;
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
		int bombsLeft = board.getBombsTotal() - board.getFlagsNumber();
		String numberString = StringUtils.formatNumber(bombsLeft);
		return numberString;
	}
	
	public String timerToShow() {
		int time = timer == null ? 0 : timer.getTime(); //Though not only everybody like ternary form:)
		String numberString = StringUtils.formatNumber(time);
		return numberString;
	}
	
	public void addController(Controller controller) {
		this.controller = controller;
	}
}