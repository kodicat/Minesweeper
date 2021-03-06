package model;

import java.util.ArrayList;
import java.util.Random;

public class Board {

	private int width;
	private int height;
	private int bombsTotal;
	private Cell[][] board;
	private int flagsNumber;
	
	public Board(int width, int height, int bombsTotal) {
		this.width = width;
		this.height = height;
		this.bombsTotal = bombsTotal;
		board = new Cell[height][width];
		flagsNumber = 0;
		
		// fill board with cells
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = new Cell();
			}
		}
		
		// put neighbors to all cells on the board
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				Cell cell = board[row][column];
				ArrayList<Cell> neighbors = getNeighbours(row, column);
				for (Cell neighbor: neighbors) {
					cell.addNeighbor(neighbor);
				}
			}
		}
	}
	
	public void fillIn(int notBombRow, int notBombColumn) {
		
		Random generator = new Random();
		
		for (int i = 0; i < bombsTotal; i++) {
			boolean isBomb = false;
			while (!isBomb) {
				int row = generator.nextInt(height);
				int column = generator.nextInt(width);
				Cell cell = board[row][column];
				if (cell.isNotBomb() && !(row ==  notBombRow
						&& column == notBombColumn)) {
					
					cell.setValue(Cell.BOMB);
					isBomb = true;
				}
			}
		}
		
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				Cell cell = board[row][column];
				if (cell.isNotBomb()) {
					int value = countBombNeighbours(cell);
					cell.setValue(value);
				}
			}
		}
	}
	
	private int countBombNeighbours(Cell cell) {
		int result = 0;
		ArrayList<Cell> neighbours = cell.getNeighbours();
		
		for (Cell neighbour: neighbours) {
			if (neighbour.isBomb()) {
				result++;
			}
		}
		return result;
	}
	
	private ArrayList<Cell> getNeighbours(int row, int column) {
		
		ArrayList<Cell> result = new ArrayList<>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!(i == 0 && j ==0)) { // do not check itself as a neighbor
					int newRow = row + i;
					int newColumn = column + j;
					if (newRow >= 0 && newRow < board.length
						&& newColumn >= 0 && newColumn < board[0].length) {
						
						Cell neighbour = board[newRow][newColumn];
						result.add(neighbour);
					}
				}
			}
		}
		return result;
	}
	
	public Cell getCell(int row, int column) {
		Cell cell = board[row][column];
		return cell;
	}
	
	public int getFlaggedNeighbours(Cell cell) {
		int result = 0;
		ArrayList<Cell> neighbours = cell.getNeighbours();
		
		for (Cell neighbour: neighbours) {
			if (neighbour.isFlag()) {
				result++;
			}
		}
		return result;
	}
	
	public void openBombs() {
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				Cell cell = board[row][column];
				// open all unflagged bombs
				if (cell.isBomb() && cell.isNotFlag()) {
					cell.open();
				}
				// display wrong flags
				if (cell.isNotBomb() && cell.isFlag()) {
					cell.setValue(Cell.WRONG);
					cell.open();
				}
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getBombsTotal() {
		return bombsTotal;
	}
	
	public void setLeftFlags() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Cell cell = getCell(i, j);
				if (cell.isClosed()) {
					cell.setFlag();
					addFlag();
				}
			}
		}
	}

	public int getFlagsNumber() {
		return flagsNumber;
	}
	
	public void addFlag() {
		flagsNumber++;
	}
	
	public void removeFlag() {
		flagsNumber--;
	}
}
