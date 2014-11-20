package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class BoardModel {

	private int currentSmile;
	
	private boolean gameOver;
	/**
	 * Specify the width of the playing board.
	 */
	private final int boardWidth;
	
	/**
	 * Specify the height of the playing board.
	 */
	private final int boardHeight;
	
	/**
	 * Specify the number of bombs on the playing board.
	 */
	private final int numBombs;
	
	/**
	 * 2-D array filled with Integers where bombs are 9's and near to bombs
	 * boxes are 1 - 8. 0's are boxes with no neighboring bombs.
	 */
	private final int[][] boardBombInnerValues;
	
	/**
	 * 2-D array of integer values which can be 0, 1, 2, 3 (closed, open, flag,
	 * question). Dimensions of this 2-D array are equal to dimensions of
	 * boardBombInnerValues.
	 */
	private final int[][] boardShowOuterValues;
	
	// outer show final values
	private final int CLOSED = 0;
	private final int OPEN = 1;
	private final int FLAG = 2;
	private final int QUESTION = 3;
	
	// inner board final values
	private final int EMPTY = 0;
	private final int BOMB = 9;
	private final int RED_BOMB = 10;
	private final int WRONG = 11;
	
	// final variables to indicate smiles
	private final int SMILE_NORMAL = 0;
	private final int SMILE_PUSHED = 1;
	private final int SMILE_DEAD = 2;
	private final int SMILE_COOL = 3;
	private final int SMILE_SCARED = 4;
	
	
	public BoardModel() {
		this(9, 9, 10);
	}
	
	public BoardModel(int boardWidth, int boardHeight, int numBombs){
		currentSmile = SMILE_NORMAL;
		gameOver = false;
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		boardBombInnerValues = new int[boardHeight][boardWidth];
		boardShowOuterValues = new int[boardHeight][boardWidth];
		this.numBombs = numBombs;
		
		fillBoardValues();
	}
	
	/**
	 * Fill in the variable boardBombInnerValues with bombs (numbers of 9) and bomb
	 * neighbors (0 - 8 numbers).
	 */
	private void fillBoardValues()
	{
		Random generator = new Random(new Long(42));
		
		// fill bombs (number 9) into boardBombInnerValues
		for (int i = 0; i < numBombs; i++)
		{
			boolean filled = true;
			
			while (filled)
			{
				int rowIndex = generator.nextInt(boardHeight);
				int columnIndex = generator.nextInt(boardWidth);
				if (boardBombInnerValues[rowIndex][columnIndex] != BOMB)
				{
					boardBombInnerValues[rowIndex][columnIndex] = BOMB;
					filled = false;
				}
			}
		}
		// fill in the board with count of neighboring bombs number (0-8)
		for (int i = 0; i < boardHeight; i++)
		{
			for (int j = 0; j < boardWidth; j++)
			{
				if (boardBombInnerValues[i][j] != BOMB)
				{
					boardBombInnerValues[i][j] = getNumberOfBombNeighbors(i, j);
				}
			}
		}
	}
	
	/**
	 * Count the number of neighboring boxes with bombs.
	 * Neighboring boxes with bombs are boxes (with value 9) connected 
	 * horizontally, vertically or diagonally to the indexed box.
	 * Possible return values are 0 through 8. From no bombs to all the
	 * neighbors are bombs.
	 * Both parameters must be within their corresponded 
	 * <code>boardBombInnerValues</code> indices.
	 * <p>
	 * It is a helper method for <code>initiateBoardValues</code> method of the
	 * <code>Board</code> class.
	 * @param rowIndex    index of the row of the <code>Board</code> class field 
	 * <code>boardBombInnerValues</code>.
	 * @param columnIndex index of the column of the <code>Board</code> class 
	 * field <code> boardBombInnerValues</code>.
	 * @return Number of neighboring boxes with bombs
	 * @see fillBoardValues
	 */
	private int getNumberOfBombNeighbors(int rowIndex, int columnIndex)
	{
		// initiate temporary helper list
		ArrayList<Integer> lst = new ArrayList<>();
		// get possible neighbors coordinates in 2_D array.
		int[][] neighbors = getNeighbors(rowIndex, columnIndex);
		
		// try adding neighbor values to our temporary helper list if this
		// neighbor is within the bounds
		for (int[] neighbor: neighbors)
		{
			int i = neighbor[0]; // get relative row coordinate of the neighbor
			int j = neighbor[1]; // get relative col coordinate of the neighbor
			try
			{
				lst.add(boardBombInnerValues[rowIndex + i][columnIndex + j]);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
		}
		return Collections.frequency(lst, 9);
	}
	
	/**
	 * Get all possible neighbors of current box.
	 * It is 8 neighbors with relative coordinates to the current box.
	 * @param rowIndex The index of the row of current box.
	 * @param columnIndex the index of the column of current box.
	 * @return Possible (relative to current box) neighbors coordinates 
	 * in 2-D array as int[8][2].
	 */
	private int[][] getNeighbors(int rowIndex, int columnIndex)
	{
		int[][] result = new int[8][2];
		int len = 0;
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				if (!(i == 0 && j ==0)) // do not check itself as a neighbor
				{
					int[] value = new int[2];
					value[0] = i;
					value[1] = j;
					result[len++] = value;
				}
			}
		}
		return result;
	}
	
	/**
	 * This method changes the value from false to true in boardShowOuterValues with
	 * coordinates rowIndex and columnIndex.
	 * If its corresponding value in boardBombInnerValues equals to zero (0) it also
	 * opens (changes from false to true) its neighbors and so on (recursive).
	 * @param rowIndex The index of the row of the box which was clicked on.
	 * @param columnIndex The index of the column of the box which was clicked.
	 * on.
	 */
	public void leftClickAt(int rowIndex, int columnIndex)
	{
		int currentShawValue = boardShowOuterValues[rowIndex][columnIndex];
		if (currentShawValue != OPEN) // cause clicks only on closed boxes.
		{
			boardShowOuterValues[rowIndex][columnIndex] = OPEN;
			int currentBombValue = getValueAt(rowIndex, columnIndex);
			if (currentBombValue == BOMB) {
				openBombs();
				setValueAt(RED_BOMB, rowIndex, columnIndex);
				setCurrentSmile(SMILE_DEAD);
				gameOver = true;
			}
			if (currentBombValue == EMPTY) // no neighboring bombs near
			{
				int[][] neighbors = getNeighbors(rowIndex, columnIndex);
				for (int[] neighbor: neighbors)
				{
					try
					{
						int i = neighbor[0] + rowIndex; // get height coordinate
						int j = neighbor[1] + columnIndex; // get width coordinate
						leftClickAt(i, j);
					}
					catch (ArrayIndexOutOfBoundsException e) {}
				}
			}
		}
	}
	
	/**
	 * Change the value of not OPEN box to FLAG, QUESTION or CLOSED according to
	 * rules. Changes are consequent CLOSED -> FLAG -> QUESTION -> CLOSED ...
	 * @param rowIndex The index of the row of the box which was clicked on.
	 * @param columnIndex The index of the column of the box which was clicked.
	 */
	public void rightClickAt(int rowIndex, int columnIndex)
	{
		int currentShawValue = boardShowOuterValues[rowIndex][columnIndex];
		if (currentShawValue != OPEN) // cause clicks only on closed boxes.
		{
			if (currentShawValue == CLOSED)
			{
				boardShowOuterValues[rowIndex][columnIndex] = FLAG;
			}
			if (currentShawValue == FLAG)
			{
				boardShowOuterValues[rowIndex][columnIndex] = QUESTION;
			}
			if (currentShawValue == QUESTION)
			{
				boardShowOuterValues[rowIndex][columnIndex] = CLOSED;
			}
		}
	}
	
	/**
	 * Open all unflagged and unquestioned boxes near current one.
	 * @param rowIndex The index of the row of the box which was clicked on.
	 * @param columnIndex The index of the column of the box which was clicked.
	 */
	public void bothClickAt(int rowIndex, int columnIndex)
	{
		int currentShawValue = boardShowOuterValues[rowIndex][columnIndex];
		if (currentShawValue == OPEN) // cause clicks only on open boxes.
		{
			int[][] neighbors = getNeighbors(rowIndex, columnIndex);
			for (int[] neighbor: neighbors)
			{
				try
				{
					int i = neighbor[0] + rowIndex;    // get height coordinate
					int j = neighbor[1] + columnIndex; // get width coordinate
					if (boardShowOuterValues[i][j] == CLOSED)
					{
						leftClickAt(i, j);
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
	}
	
	/**
	 * Get integer value at given row and column indices.
	 * @param rowIndex row index of needed value
	 * @param columnIndex column index of needed value
	 * @return Integer value at given rowIndex and columnIndex.
	 */
	public int getValueAt(int rowIndex, int columnIndex) {
		return boardBombInnerValues[rowIndex][columnIndex];
	}
	
	public void setValueAt(int value, int rowIndex, int columnIndex) {
		boardBombInnerValues[rowIndex][columnIndex] = value;
	}
	
	/**
	 * Open all the bombs (except flagged ones)  when loosing game.
	 */
	public void openBombs() {
		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (boardBombInnerValues[i][j] == BOMB 
					&& boardShowOuterValues[i][j] != FLAG)
				{
					boardShowOuterValues[i][j] = OPEN;
				}
				if (boardBombInnerValues[i][j] != BOMB
					&& boardShowOuterValues[i][j] == FLAG)
				{
					boardBombInnerValues[i][j] = WRONG;
					boardShowOuterValues[i][j] = OPEN;
				}
			}
		}
	}
	
	/**
	 * Get the copy of boardBombInnerValues to use publicly.
	 * @return 2-D int[][] array, the copy of variable boardBombInnerValues.
	 */
	public int[][] getBoardBombInnerValues()
	{
		int[][] result = new int[boardHeight][boardWidth];
		for (int i = 0; i < boardHeight; i++)
		{
			for (int j = 0; j < boardWidth; j++)
			{
				result[i][j] = boardBombInnerValues[i][j];
			}
		}
		return result;
	}
	
	/**
	 * Get the copy of boardShowOuterValues to use publicly.
	 * @return 2-D int[][] array, the copy of variable boardShowOuterValues.
	 */
	public int[][] getBoardShowOuterValues()
	{
		int[][] result = new int[boardHeight][boardWidth];
		for (int i = 0; i < boardHeight; i++)
		{
			for (int j = 0; j < boardWidth; j++)
			{
				result[i][j] = boardShowOuterValues[i][j];
			}
		}
		return result;
	}
	
	/**
	 * Get the width of this MinesweeperBoardModel instance.
	 * @return Integer value width of the current instance.
	 */
	public int getWidth()
	{
		return boardWidth;
	}
	
	/**
	 * Get the height of this MinesweeperBoardModel instance.
	 * @return Integer value height of the current instance.
	 */
	public int getHeight()
	{
		return boardHeight;
	}
	
	public boolean isGameOver()
	{
		return gameOver;
	}
	
	public int getCurrentSmile()
	{
		return currentSmile;
	}
	
	public void setCurrentSmile(int value)
	{
		currentSmile = value;
	}
	
	//
	//
	// junk
	//--------------------------------------------------------------------------
	
	public int getBoardLength()
	{
		return boardBombInnerValues.length * boardBombInnerValues[0].length;
	}
	
	public int getNumberOfBombs()
	{
		return numBombs;
	}
}
