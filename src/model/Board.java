package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Board {

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
	private final int[][] boardBombValues;
	
	/**
	 * 2-D array of boolean values where true boxes are opened and false ones
	 * are closed. Dimensions of this 2-D array are equal to dimensions of
	 * boardBombValues.
	 */
	private final boolean[][] boardShowValues;
	
	public Board()
	{
		this(9, 9, 10);
	}
	
	public Board(int boardWidth, int boardHeight, int numBombs)
	{
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		boardBombValues = new int[boardHeight][boardWidth];
		boardShowValues = new boolean[boardHeight][boardWidth];
		this.numBombs = numBombs;
		
		fillBoardValues();
	}
	
	/**
	 * Fill in the variable boardBombValues with bombs (numbers of 9) and bomb
	 * neighbors (0 - 8 numbers).
	 */
	private void fillBoardValues()
	{
		Random generator = new Random(new Long(42));
		
		// fill bombs (number 9) into boardBombValues
		for (int i = 0; i < numBombs; i++)
		{
			boolean filled = true;
			
			while (filled)
			{
				int rowIndex = generator.nextInt(boardHeight);
				int columnIndex = generator.nextInt(boardWidth);
				if (boardBombValues[rowIndex][columnIndex] != 9)
				{
					boardBombValues[rowIndex][columnIndex] = 9;
					filled = false;
				}
			}
		}
		// fill in the board with count of neighboring bombs number (0-8)
		for (int i = 0; i < boardHeight; i++)
		{
			for (int j = 0; j < boardWidth; j++)
			{
				if (boardBombValues[i][j] != 9)
				{
					boardBombValues[i][j] = getNumberOfBombNeighbors(i, j);
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
	 * <code>boardBombValues</code> indices.
	 * <p>
	 * It is a helper method for <code>initiateBoardValues</code> method of the
	 * <code>Board</code> class.
	 * @param rowIndex    index of the row of the <code>Board</code> class field 
	 * <code>boardBombValues</code>.
	 * @param columnIndex index of the column of the <code>Board</code> class 
	 * field <code> boardBombValues</code>.
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
				lst.add(boardBombValues[rowIndex + i][columnIndex + j]);
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
	 * This method changes the value from false to true in boardShowValues with
	 * coordinates rowIndex and columnIndex.
	 * If its corresponding value in boardBombValues equals to zero (0) it also
	 * opens (changes from false to true) its neighbors and so on (recursive).
	 * @param rowIndex The index of the row of the box which was clicked on.
	 * @param columnIndex the index of the column of the box which was clicked
	 * on.
	 */
	public void leftClickAt(int rowIndex, int columnIndex)
	{
		boolean currentShawValue = boardShowValues[rowIndex][columnIndex];
		if (currentShawValue == false)
		{
			boardShowValues[rowIndex][columnIndex] = true;
			int currentBombValue = boardBombValues[rowIndex][columnIndex];
			if (currentBombValue == 0)
			{
				int[][] neighbors = getNeighbors(rowIndex, columnIndex);
				for (int[] neighbor: neighbors)
				{
					try
					{
						int i = neighbor[0];
						int j = neighbor[1];
						leftClickAt(rowIndex + i, columnIndex + j);
					}
					catch (ArrayIndexOutOfBoundsException e) {}
				}
			}
		}
		// call view to refresh.
	}
	
	public int[][] getBoardBombValues()
	{
		int[][] result = new int[boardHeight][boardWidth];
		for (int i = 0; i < boardHeight; i++)
		{
			for (int j = 0; j < boardWidth; j++)
			{
				result[i][j] = boardBombValues[i][j];
			}
		}
		return result;
	}
	
	public boolean[][] getBoardShowValues()
	{
		boolean[][] result = new boolean[boardHeight][boardWidth];
		for (int i = 0; i < boardHeight; i++)
		{
			for (int j = 0; j < boardWidth; j++)
			{
				result[i][j] = boardShowValues[i][j];
			}
		}
		return result;
	}
	
	//
	//
	//
	//
	
	public int getBoardLength()
	{
		return boardBombValues.length * boardBombValues[0].length;
	}
	
	public int getNumberOfBombs()
	{
		int number = 0;
		for (int[] row: boardBombValues)
		{
			for (int box: row)
			{
				if (box == 9)
				{
					number++;
				}
			}
		}
		return number;
	}
}
