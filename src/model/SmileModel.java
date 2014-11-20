package model;

public class SmileModel {

	private int currentSmile;
	
	// final variables to indicate smiles
	private final int SMILE_NORMAL = 0;
	private final int SMILE_PUSHED = 1;
	private final int SMILE_DEAD = 2;
	private final int SMILE_COOL = 3;
	private final int SMILE_SCARED = 4;
	
	
	public SmileModel()
	{
		currentSmile = SMILE_NORMAL;
	}
	
	public int getCurrentSmile()
	{
		return currentSmile;
	}
	
	public void setCurrentSmile(int smileMode)
	{
		currentSmile = smileMode;
	}
}
