package model;

import java.util.ArrayList;

public class Cell {

	private int state;
	private int value;
	private ArrayList<Cell> neighbors;
	
	// states
	public static final int CLOSED = 0;
	public static final int OPEN = 1;
	public static final int FLAG = 2;
	public static final int QUESTION = 3;
	public static final int PRESSED = 4;
	
	// values
	public static final int EMPTY = 0;
	public static final int ONE = 0;
	public static final int TWO = 0;
	public static final int THREE = 0;
	public static final int FOUR = 0;
	public static final int FIVE = 0;
	public static final int SIX = 0;
	public static final int SEVEN = 0;
	public static final int EIGHT = 0;
	public static final int BOMB = 9;
	public static final int RED_BOMB = 10;
	public static final int WRONG = 11;
	
	public Cell() {
		state = CLOSED;
		value = EMPTY;
		neighbors = new ArrayList<Cell>();
	}
	
	public void close() {
		state = CLOSED;
	}
	
	public boolean isClosed() {
		return state == CLOSED;
	}
	
	public boolean isNotClosed() {
		return !isClosed();
	}
	
	public void open() {
		state = OPEN;
	}
	
	public boolean isOpen() {
		return state == OPEN;
	}
	
	public boolean isNotOpen() {
		return !isOpen();
	}
	
	public void setFlag() {
		state = FLAG;
	}
	
	public boolean isFlag() {
		return state == FLAG;
	}
	
	public boolean isNotFlag() {
		return !isFlag();
	}
	
	public boolean isQuestion() {
		return state == QUESTION;
	}
	
	public void press() {
		state = PRESSED;
	}
	
	public boolean isPressed() {
		return state == PRESSED;
	}
	
	public void setState(int newState) {
		state = newState;
	}
	
	public int getState() {
		return state;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isEmpty() {
		return value == EMPTY;
	}
	
	public boolean isBomb() {
		return value == BOMB;
	}
	
	public boolean isNotBomb() {
		return !isBomb();
	}
	
	public void addNeighbor(Cell neighbor) {
		if (!(neighbors.contains(neighbor))) {
			neighbors.add(neighbor);
			neighbor.addNeighbor(this);
		}
	}
	
	public ArrayList<Cell> getNeighbours() {
		return neighbors;
	}
}
