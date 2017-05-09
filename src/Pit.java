
/**
 * CS151 Project
 * @author Kenny Huynh, Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Represents each pit in the mancala.
 */
public class Pit {
	private int stones;
	
	/**
	 * Constructor that initializes each pit with a given number of stones.
	 * @param stones number of stones in each pit.
	 */
	public Pit(int stones) {
		this.stones = stones;
	}
	
	/**
	 * Gets the amount of stones in this pit.
	 * @return the amount of stones.
	 */
	public int getStones() {
		return this.stones;
	}
	
	/**
	 * Sets the amount of stones in this pit to the given amount.
	 * @param x the amount of stones that should be in this pit.
	 */
	public void setStones(int x) {
		this.stones = x;
	}
	
	
}