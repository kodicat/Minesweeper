package model;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;


public class GameModel {

	/**
	 * True for finished game, false for still in game progress.
	 */
	private boolean finishedGame;
	
	
	
	//==========================================================================
	// smile variables
	//==========================================================================
	
	/**
	 * The state of the current smile normal, pressed, dead, cool or scared 
	 * (0, 1, 2, 3, 4).
	 */
	private int currentSmile;
	
	// final helper variables to indicate smiles
	private final int SMILE_NORMAL = 0;
	private final int SMILE_PRESSED = 1;
	private final int SMILE_DEAD = 2;
	private final int SMILE_COOL = 3;
	private final int SMILE_SCARED = 4;
	//
	// end of smile variables
	//==========================================================================
	
	
	
	//==========================================================================
	// board variables
	//==========================================================================
	
	/**
	 * The width of the playing board.
	 */
	private final int boardWidth;
	
	/**
	 * The height of the playing board.
	 */
	private final int boardHeight;
	
	/**
	 * The number of bombs on the playing board.
	 */
	private final int numBombs;
	
	/**
	 * Number of boxes which are not opened: closed, flagged or questioned.
	 * <p>
	 * At the beginning of the game it equals to the product of boardWidth and
	 * boardHeight.
	 */
	private int numberOfNotOpenedBoxes;
	
	/**
	 * 3-D array of boxes filled with Integers. The dimensions of this board
	 * show height, width and floors accordingly.
	 * <p>
	 * Zero floor of the box presents values from 0 to 11. 0 means no
	 * neighboring bombs near (EMPTY), 1 - 8 values mean number of bombs near,
	 * 9 means bomb (BOMB), 10 means the red bomb which caused losing game
	 * (RED_BOMB), 11 means wrong flag put on losing game (WRONG).
	 * <br>
	 * First floor of the box presents values from 0 to 3. 0 means closed box
	 * with no flags or questions on it (CLOSED), 1 means that the box is
	 * already opened (OPEN), 2 means closed box with flag on it (FLAG), 3 means
	 * closed box with question on it (QUESTION).
	 */
	private int[][][] boardValues3D;
	
	// floor final helper board variables
	private final int ZERO_FLOOR = 0;
	private final int FIRST_FLOOR = 1;
	
	// zero floor final helper board variables
	private final int EMPTY = 0;
	private final int BOMB = 9;
	private final int RED_BOMB = 10;
	private final int WRONG = 11;

	// first floor final helper board variables
	private final int CLOSED = 0;
	private final int OPEN = 1;
	private final int FLAG = 2;
	private final int QUESTION = 3;
	private final int PRESSED = 4;
	
	// right click map
	private HashMap<Integer,Integer> RIGHT_CLICK_MAP;
	{
		RIGHT_CLICK_MAP = new HashMap<Integer, Integer>();
		RIGHT_CLICK_MAP.put(CLOSED, FLAG);
		RIGHT_CLICK_MAP.put(FLAG, QUESTION);
		RIGHT_CLICK_MAP.put(QUESTION, CLOSED);
	}
	
	int pressedBoxRow;
	int pressedBoxColumn;
	//
	// end of board variables
	//==========================================================================
	
	
	
	/**
	 * Game constructor.
	 * @param boardWidth The width of the playing board.
	 * @param boardHeight The height of the playing board.
	 * @param numBombs The number of bombs on the playing board.
	 */
	public GameModel(int boardWidth, int boardHeight, int numBombs) {
		
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		this.numBombs = numBombs;
		
		resetGameValues();
	}
	
	
	
	//==========================================================================
	// private helper methods
	//
	
	/**
	 * Reset and initiate the game values and variables.
	 * <p>
	 * Set game state finishedGame to false.
	 * <br>
	 * Fill in the board variable boardValues3D with zero floor values like
	 * bombs (BOMB) and bomb neighbors (0 - 8 numbers) and first floor values
	 * with all closed (CLOSED).
	 * <br>
	 * Set currentSmile to initial normal value (SMILE_NORMAL).
	 */
	private void resetGameValues() {
		
		finishedGame = false;
		currentSmile = SMILE_NORMAL;
		numberOfNotOpenedBoxes = boardHeight * boardWidth;
		boardValues3D = new int[boardHeight][boardWidth][2];
		pressedBoxRow = -1;
		pressedBoxColumn = -1;
		
		Random generator = new Random(new Long(42));
		
		// fill in zero floor with bombs (BOMB)
		for (int i = 0; i < numBombs; i++) {
			boolean bombSet = false;
			
			while (!bombSet) {
				int row = generator.nextInt(boardHeight);
				int column = generator.nextInt(boardWidth);
				if (boardValues3D[row][column][ZERO_FLOOR] != BOMB) {
					boardValues3D[row][column][ZERO_FLOOR] = BOMB;
					bombSet = true;
				}
			}
		}
		
		// fill in zero floor with neighboring bombs numbers (0-8)
		for (int row = 0; row < boardHeight; row++) {
			for (int column = 0; column < boardWidth; column++) {
				if (boardValues3D[row][column][ZERO_FLOOR] != BOMB)
				{
					int value = countBombNeighbors(row, column);
					boardValues3D[row][column][ZERO_FLOOR] = value;
				}
			}
		}
	}
	
	/**
	 * Count the number of neighboring boxes with bombs.
	 * <p>
	 * Neighboring boxes with bombs are boxes with value 9 (BOMB) on the zero
	 * floor connected horizontally, vertically or diagonally to the current box.
	 * Possible return values are 0 through 8. From no bombs to all the
	 * neighbors are bombs.
	 * <br>
	 * It is a helper method for resetGameValues method of the GameModel class.
	 * @param row    The index of the row of the box needed to count.
	 * @param column The index of the column of the box needed to count.
	 * @return Number of neighboring boxes with bombs. Possible return values
	 * are 0 through 8.
	 */
	private int countBombNeighbors(int row, int column) {
		
		return countValueNeighbors(BOMB, row, column, ZERO_FLOOR);
	}
	
	/**
	 * Count the number of neighboring boxes with flags.
	 * @param row    The index of the row of the box needed to count.
	 * @param column The index of the column of the box needed to count.
	 * @return Number of neighboring boxes with flags.
	 */
	private int countFlagNeighbors(int row, int column) {
		
		return countValueNeighbors(FLAG, row, column, FIRST_FLOOR);
	}
	
	/**
	 * Count the number of neighboring boxes with needed value on needed floor.
	 * @param value  The Integer value to count (BOMB, FLAG, etc.).
	 * @param row    The index of the row of the box needed to count neighbors.
	 * @param column The index of the column of the box needed to count.
	 * @param floor  The floor to count on (ZERO_FLOOR or FIRST_FLOOR).
	 * neighbors.
	 * @return Number of neighboring boxes with needed value on needed floor.
	 */
	private int countValueNeighbors(int value, int row, int column, int floor) {
		// initiate temporary helper neighbor list with floor values
		ArrayList<Integer> values = new ArrayList<>();
		
		// get neighbors coordinates in 2-D array.
		ArrayList<ArrayList<Integer>> neighbors;
		neighbors = getNeighbors(row, column);
		
		// fill in temporary helper neighbor list with floor values
		for (ArrayList<Integer> neighbor: neighbors) {
			int i = neighbor.get(0); // get row of the neighbor
			int j = neighbor.get(1); // get column of the neighbor
			values.add(boardValues3D[i][j][floor]);
		}
		// count and return the number of neighbors with needed value
		return Collections.frequency(values, value);
	}
	
	/**
	 * Get all neighbors of current box and set it in 2-D array.
	 * From 3, 5 or 8 neighbors with its coordinates.
	 * @param row    The index of the row of current box.
	 * @param column The index of the column of current box.
	 * @return Neighbor coordinates in 2-D array as
	 * ArrayList<ArrayList<Integer>>.
	 */
	private ArrayList<ArrayList<Integer>> getNeighbors(int row, int column) {
		
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!(i == 0 && j ==0)) { // do not check itself as a neighbor
					try {
						int rowIndex = row + i;
						int columnIndex = column + j;
						
						// check whether the neighbor within the bounds
						@SuppressWarnings("unused")
						int temp = boardValues3D[rowIndex][columnIndex][0];
						
						// run if not ArrayIndexOutOfBoundsException
						ArrayList<Integer> neighbor = new ArrayList<>();
						neighbor.add(rowIndex);
						neighbor.add(columnIndex);
						result.add(neighbor);
					}
					catch (ArrayIndexOutOfBoundsException e) {}
				}
			}
		}
		return result;
	}
	
	/**
	 * Check whether we can open neighbors on both click
	 * @param row    The index of the row of current box.
	 * @param column The index of the column of current box.
	 * @return
	 */
	private boolean canOpenNeighbors(int row, int column) {
		
		int zeroFloorValue = boardValues3D[row][column][ZERO_FLOOR];
		int flags = countFlagNeighbors(row, column);
		return zeroFloorValue == flags;
	}
	
	/**
	 * Check if the game is new or not.
	 * @return True for new game and false for not new.
	 */
	private boolean isNewGame() {
		
		for (int[][] row: boardValues3D) {
			for (int[] box: row) {
				if (box[FIRST_FLOOR] != CLOSED) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Open all the bombs without flag and wrong flags when loosing game.
	 */
	private void openBombs() {
		
		for (int row = 0; row < boardHeight; row++) {
			for (int column = 0; column < boardWidth; column++) {
				// open all unflagged bombs
				if (boardValues3D[row][column][ZERO_FLOOR] == BOMB 
					&& boardValues3D[row][column][FIRST_FLOOR] != FLAG) {
					
					boardValues3D[row][column][FIRST_FLOOR] = OPEN;
				}
				
				// display wrong flags
				if (boardValues3D[row][column][ZERO_FLOOR] != BOMB
					&& boardValues3D[row][column][FIRST_FLOOR] == FLAG) {
					
					boardValues3D[row][column][ZERO_FLOOR] = WRONG;
					boardValues3D[row][column][FIRST_FLOOR] = OPEN;
				}
			}
		}
	}
	
	/**
	 * Check if the player has won the game.
	 * @return True for winning and false for losing or not yet winning.
	 */
	private boolean wonTheGame() {
		
		if (numBombs == numberOfNotOpenedBoxes && finishedGame == false) {
			return true;
		}
		return false;
	}
	//
	// end of private helper methods
	//==========================================================================
	
	
	
	//==========================================================================
	// click event methods
	//
	
	/**
	 * Start a new game when click on a smile.
	 */
	public void leftClickAtSmile() {
		
		if (!isNewGame()) {
			resetGameValues();
		}
	}
	
	/**
	 * Change picture when press left button on a smile.
	 */
	public void pressSmile() {
		
		currentSmile = SMILE_PRESSED;
	}
	
	/**
	 * Change picture of a smile back when release the button.
	 */
	public void releaseSmile(MouseEvent e, int smileSize) {
		
		currentSmile = SMILE_NORMAL;
		
		// count as click when released in bounds of current smile
		if (e.getX() >= 0 && e.getX() < smileSize
				&& e.getY() >= 0 && e.getY() < smileSize) {
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
		
		int firstFloorValue = boardValues3D[row][column][FIRST_FLOOR];
		if (firstFloorValue == CLOSED) { // cause clicks only on closed boxes
			boardValues3D[row][column][FIRST_FLOOR] = OPEN;
			int zeroFloorValue = boardValues3D[row][column][ZERO_FLOOR];
			
			// count that we opened one box
			numberOfNotOpenedBoxes--;
			
//			// change scared smile to normal if it was scared
//			if (currentSmile == SMILE_SCARED) {
//				currentSmile = SMILE_NORMAL;
//			}
			
			// change variables when lose the game
			if (zeroFloorValue == BOMB) {
				openBombs();				// show location of all the bombs
				boardValues3D[row][column][ZERO_FLOOR] = RED_BOMB;
				currentSmile = SMILE_DEAD;
				finishedGame = true;
			}
			// open neighboring boxes when clicked on empty box
			else if (zeroFloorValue == EMPTY) {
				ArrayList<ArrayList<Integer>> neighbors;
				neighbors = getNeighbors(row, column);
				for (ArrayList<Integer> neighbor: neighbors) {
					int i = neighbor.get(0); // get height coordinate
					int j = neighbor.get(1); // get width coordinate
					leftClickAt(i, j);
				}
			}
//			// scared smile on many bombs near
//			else if (zeroFloorValue > 4) {
//				currentSmile = SMILE_SCARED;
//			}
			
			if (wonTheGame()) {
				currentSmile = SMILE_COOL;
				finishedGame = true;
				// TODO
				// stop timer
				// record high score
			}
			
		}
	}
	
	/**
	 * Change the value of not OPEN box to FLAG, QUESTION or CLOSED according to
	 * rules. Changes are consequent CLOSED -> FLAG -> QUESTION -> CLOSED ...
	 * @param row    The index of the row of the box which was clicked on.
	 * @param column The index of the column of the box which was clicked.
	 */
	public void rightClickAt(int row, int column) {
		
		int firstFloorValue = boardValues3D[row][column][FIRST_FLOOR];
		if (firstFloorValue != OPEN) {   // cause clicks only on not open boxes.
			int changeTo = RIGHT_CLICK_MAP.get(firstFloorValue);
			boardValues3D[row][column][FIRST_FLOOR] = changeTo;
		}
		
//		// change scared smile to normal if scared
//		if (currentSmile == SMILE_SCARED) {
//			currentSmile = SMILE_NORMAL;
//		}
	}
	
	/**
	 * Open all unflagged and unquestioned boxes near current one.
	 * @param row    The index of the row of the box which was clicked on.
	 * @param column The index of the column of the box which was clicked.
	 */
	public void bothClickAt(int row, int column) {
		
		int firstFloorValue = boardValues3D[row][column][FIRST_FLOOR];
		if (firstFloorValue == OPEN) { // cause clicks only on open boxes.
			ArrayList<ArrayList<Integer>> neighbors = getNeighbors(row, column);
			
			for (ArrayList<Integer> neighbor: neighbors) {
				int i = neighbor.get(0);    // get neighbor height coordinate
				int j = neighbor.get(1);    // get neighbor width coordinate
				if (boardValues3D[i][j][FIRST_FLOOR] == CLOSED
					&& canOpenNeighbors(row, column)) {
					
					leftClickAt(i, j);
				}
			}
		}
	}
	
	public void leftPressAt(int row, int column) {
		
		int firstFloorValue = boardValues3D[row][column][FIRST_FLOOR];
		if (firstFloorValue == CLOSED) { // cause pressing only on closed boxes
			pressedBoxRow = row;
			pressedBoxColumn = column;
			boardValues3D[row][column][FIRST_FLOOR] = PRESSED;
		}
	}
	
	public void leftReleaseAt(int row, int column) {
		
		int firstFloorValue = boardValues3D[pressedBoxRow][pressedBoxColumn][FIRST_FLOOR];
		if (firstFloorValue == PRESSED) {
			boardValues3D[pressedBoxRow][pressedBoxColumn][FIRST_FLOOR] = CLOSED;
		}
		
		// count as click when pressed and released on the same box
		if (pressedBoxRow == row && pressedBoxColumn == column) {
			leftClickAt(row, column);
		}
	}
	
	public void bothPressAt(int row, int column) {
		
		int firstFloorValue = boardValues3D[row][column][FIRST_FLOOR];
		if (firstFloorValue == OPEN) { // cause pressing only on open boxes
			pressedBoxRow = row;
			pressedBoxColumn = column;
			for (ArrayList<Integer> neighbor: getNeighbors(row, column)) {
				int i = neighbor.get(0);
				int j = neighbor.get(1);
				if (boardValues3D[i][j][FIRST_FLOOR] == CLOSED) {
					boardValues3D[i][j][FIRST_FLOOR] = PRESSED;
				}
			}
		}
	}
	
	public void bothReleaseAt(int row, int column) {
		
		// change pressed boxes to closed (as before)
		for (ArrayList<Integer> neighbor: getNeighbors(pressedBoxRow, pressedBoxColumn)) {
			int i = neighbor.get(0);
			int j = neighbor.get(1);
			if (boardValues3D[i][j][FIRST_FLOOR] == PRESSED) {
				boardValues3D[i][j][FIRST_FLOOR] = CLOSED;
			}
		}
		// count as click when pressed and released on the same box
		if (pressedBoxRow == row && pressedBoxColumn == column) {
			bothClickAt(row, column);
		}
	}
	//
	// end of click event methods
	//==========================================================================
	
	
	
	//==========================================================================
	// public methods
	//
	/**
	 * Get the copy of boardValues3D to use publicly.
	 * @return 3-D int[][][] array, the copy of variable boardValues3D.
	 */
	public int[][][] getBoardValues()
	{
		int[][][] result = new int[boardHeight][boardWidth][2];
		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < boardWidth; j++) {
				result[i][j][ZERO_FLOOR] = boardValues3D[i][j][ZERO_FLOOR];
				result[i][j][FIRST_FLOOR] = boardValues3D[i][j][FIRST_FLOOR];
			}
		}
		return result;
	}
	
	public int getWidth() {
		return boardWidth;
	}
	
	public int getHeight() {
		
		return boardHeight;
	}
	
	public boolean isGameOver()
	{
		return finishedGame;
	}
	
	public int getCurrentSmile() {
		return currentSmile;
	}
	
	
	public int bombsToShow() {
		int flagsOnBoard = 0;
		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (boardValues3D[i][j][FIRST_FLOOR] == FLAG) {
					flagsOnBoard++;
				}
			}
		}
		return Math.min(Math.max(numBombs - flagsOnBoard, -99), 999);
	}
	//
	// end of public methods
	//==========================================================================
}
