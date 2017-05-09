
import java.util.ArrayList;

import javax.swing.event.*;
/**
 * CS151 Project
 * @author Kenny Huynh, Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Model class that represents the game logic for mancala.
 */
public class MancalaModel {
	private ArrayList<ChangeListener> listeners;
	private Pit[] pits;
	boolean firstPlayerTurn, secondPlayerTurn, firstPlayerIsWinner, secondPlayerIsWinner, tied;
	int lastPit,lastPitStones, destinationPit, oppOnesPitStones, firstPlayerUndos, secondPlayerUndos;
	boolean gameStarted, gameIsOver, error;
	String errorMsg;

	/**
	 * Constructor that initializes all the instance variables.
	 */
	public MancalaModel() {
		listeners = new ArrayList<ChangeListener>();
		pits = new Pit[14]; // 6 for each side and 1 for each mancala
		for(int i = 0; i < pits.length; i++) {
			pits[i] = new Pit(0);
		}
		firstPlayerTurn = true;
		secondPlayerTurn = false;
		firstPlayerIsWinner = false;
		secondPlayerIsWinner = false;
		tied = false;
		lastPit = 0;
		lastPitStones = 0;
		firstPlayerUndos = 3;
		secondPlayerUndos = 3;
		destinationPit = 0;
		oppOnesPitStones = 0;
		gameStarted = false;
		gameIsOver = false;
		error = false;
		errorMsg = "";
	}
	
	/**
	 * The actual gameplay. This method moves the board for each player's turn.
	 * @param i the position or pit to move from.
	 */
	public void moveBoard(int i) {
		//If one is true, winner already decided.
		if(firstPlayerIsWinner || secondPlayerIsWinner || tied) {
			return;
		}
		
		//pits[6] and pits[13] are mancalas. Should not be pressable.
		if(i == 6 || i == 13) {
			error = true;
			errorMsg = "Can't choose mancalas as a pit.";
			changeState();
			getErrorMessage();
			return;
		}
		
		//If there are no more stones in the chosen pit.
		if(pits[i].getStones() == 0) {
			error = true;
			errorMsg = "No stones, please choose another pit.";
			changeState();
			getErrorMessage();
			return;
		}
		
		error = false;
		// used for the undo function.
		lastPit = i; //the pit that the user clicked on.
		lastPitStones = pits[i].getStones(); //the number of stones in that pit.
		
		
			int firstPlayerPitStones = 0; //both used to determine when game is over.
			int secondPlayerPitStones = 0;
			if (firstPlayerTurn && i >= 0 && i <= 5) { //makes sure that the pit is on firstplayer side.
				secondPlayerUndos = 3; //every time it is first player turn, reset second player's undo back to 3.
				gameStarted = true;
				int stonesInCurrentPit = pits[i].getStones();
				pits[i].setStones(0); //set chosen pit stones to 0.
				int mover = i + 1; //this is the value that is used to traverse through the board.
				//using the current pit's stones, move the stones to each pit following it
				for (int x = 0; x < stonesInCurrentPit; x++) {
					if (mover == 13) { //when mover is equal to the index of 2nd player mancala, set mover to first player pit.
						mover = 0;
					}
					pits[mover].setStones(pits[mover].getStones() + 1);
					mover++;
				}
				mover = mover - 1; //decrement back 
				destinationPit = mover; // keeps track of the pit that the last
										// stone was placed for undo function.
				// if last pit is on first player side and only has one stone.
				if (mover >= 0 && mover < 6 && pits[mover].getStones() == 1) {
					oppOnesPitStones = pits[12-mover].getStones();
					pits[6].setStones(pits[6].getStones() + pits[12 - mover].getStones());
					pits[12 - mover].setStones(0);
				}

				// determine if there are any stones left on firstPlayer side.
				for (int n = 0; n < 6; n++) {
					firstPlayerPitStones += pits[n].getStones();
				}

				// determine if there are any stones left on secondPlayer side.
				for (int n = 7; n < 13; n++) {
					secondPlayerPitStones += pits[n].getStones();
				}

				// if firstPlayer side has no stones, collect all of second
				// player stones and give to second player then determine whos
				// the winner.
				if (firstPlayerPitStones == 0) {
					determineWinner(true, secondPlayerPitStones);
				}

				// if secondPlayer side has no stones, collect all of second
				// player stones and give to first player then determine whos
				// the winner.
				if (secondPlayerPitStones == 0) {
					determineWinner(false, firstPlayerPitStones);
				}
				// if the last pit is the secondPlayer's mancala, it will still
				// be firstPlayer's turn. else, change turns.
				if (mover == 6) {
					firstPlayerTurn = true;
					secondPlayerTurn = false;
				} else {
					firstPlayerTurn = false;
					secondPlayerTurn = true;
				}
				// change listeners after.
				changeState();
			} else {
				if (secondPlayerTurn && i >= 6 && i <= 12) {
					firstPlayerUndos = 3;
					int mover = i + 1;
					int stonesInCurrentPit = pits[i].getStones();
					pits[i].setStones(0);
					int lastPit = 0;
					for (int x = 0; x < stonesInCurrentPit; x++) {
						if (mover == 13) {
							pits[mover].setStones(pits[mover].getStones() + 1);
							mover = 0;
							continue;
						}
						if (mover == 6) {
							mover = 7;
						}
						pits[mover].setStones(pits[mover].getStones() + 1);
						mover++;
					}
					mover = mover - 1;
					if(mover == -1) {
						lastPit = 13;
						destinationPit = lastPit;
					} else {
					destinationPit = mover;
					}
					//if the last pit stones equal to 1, take that and the opposite pit's stone and add to secondPlayer mancala.
					if (mover > 6 && mover < 13 && pits[mover].getStones() == 1) {
						oppOnesPitStones = pits[12-mover].getStones();
						pits[13].setStones(pits[13].getStones() +  pits[12 - mover].getStones());
						pits[12 - mover].setStones(0);
					}

					// determine if there are any stones left on firstPlayer side.
					for (int n = 0; n < 6; n++) {
						firstPlayerPitStones += pits[n].getStones();
					}

					// determine if there are any stones left on secondPlayer side.
					for (int n = 7; n < 13; n++) {
						secondPlayerPitStones += pits[n].getStones();
					}

					// if firstPlayer side has no stones, collect all of second
					// player stones and give to second player then determine whos
					// the winner.
					if (firstPlayerPitStones == 0) {
						determineWinner(true, secondPlayerPitStones);
						return;
					}

					// if secondPlayer side has no stones, collect all of second
					// player stones and give to first player then determine whos
					// the winner.
					if (secondPlayerPitStones == 0) {
						determineWinner(false, firstPlayerPitStones);
						return;
					}

					// if the last pit is the secondPlayer's mancala, it will still
					// be firstPlayer's turn. else, change turns.
					if (lastPit == 13) {
						firstPlayerTurn = false;
						secondPlayerTurn = true;
					} else {
						firstPlayerTurn = true;
						secondPlayerTurn = false;
					}
					// change listeners after.
					changeState();
				}
			}
	}

	/**
	 * Undo function of the game. 
	 */
	public void undo() {
		//if the game just started and first player tries to undo without making a first turn. 
		if(!gameStarted) {
			error = true;
			errorMsg = "Game just started. Please make move first.";
			changeState();
			getErrorMessage();
			return;
		}
		
		//if firstPlayerUndos/secondPlayerUndos are 0, print out error msg and return.
		if(firstPlayerUndos == 0 || secondPlayerUndos == 0) {
			error = true;
			errorMsg = "Used up all undos for this turn.";
			changeState();
			getErrorMessage();
			return;
		}
		
		//if one of the pits does not equal 0, that means that there are consecutive undos.
		if((pits[lastPit].getStones() != 0 && firstPlayerTurn && destinationPit == 6) || (pits[lastPit].getStones() != 0 && secondPlayerTurn && destinationPit == 13)) {
			error = true;
			errorMsg = "Can't have consecutive undos.";
			changeState();
			getErrorMessage();
			return;
		}
		
		error = false;
		//this is undo for firstPlayer if the last stone ends up in firstPlayer's mancala and it's firstPlayer's turn again;
		if(firstPlayerTurn && destinationPit == 6 && firstPlayerUndos > 0) {
			System.out.println(lastPit + " " + destinationPit);
			pits[lastPit].setStones(lastPitStones);
			int mover = destinationPit;
			//go backwards and decrement each pit's stones by 1.
			for(int i = lastPitStones; i > 0; i--) {
				if(mover == -1) { //if it equals -1, this means it hits the last pit on first player side, change it to 12.
					mover = 12;
				}
				pits[mover].setStones(pits[mover].getStones() - 1);
				mover--;
			}
			firstPlayerUndos--; //decrement undo by 1.
			changeState();
			return;
		}
		
		//same as last statement except for secondPlayer.
		if(secondPlayerTurn && destinationPit == 13 && secondPlayerUndos > 0) {
			pits[lastPit].setStones(lastPitStones);
			int mover = destinationPit;
			for(int i = lastPitStones; i > 0; i--) {
				if(mover == 6) {
					mover = 5;
				}
				pits[mover].setStones(pits[mover].getStones() - 1);
				mover--;
			}
			secondPlayerUndos--;
			changeState();
			return;
		}

		// this is the undo part for the first player. After he takes a turn,
		// firstPlayer will be false and this will make firstPlayer true again.
		if (!firstPlayerTurn && firstPlayerUndos > 0) {
			pits[lastPit].setStones(lastPitStones);//set lastPitStones to original amount of stones;
			int mover = destinationPit; //mover set to destined pit for traversing back.
			if (pits[mover].getStones() == 1) { // case where the last pit had one stone after a turn 
				System.out.println(pits[6].getStones());
				System.out.println(lastPitStones);
				System.out.println(mover);
				pits[12-mover].setStones(oppOnesPitStones);
				pits[6].setStones(pits[6].getStones() - oppOnesPitStones);
				for(int i = lastPitStones; i > 0; i--) {
					if(mover == -1) {
						mover = 12;
					}
					pits[mover].setStones(pits[mover].getStones() - 1);
					mover--;
				}
			} else { //traverses the mancala board and decrements each pit by 1 stone until it reaches the pit before the last pit.
				for (int i = lastPitStones; i > 0; i--) {
					if (mover == -1) {
						mover = 12;
					}
					pits[mover].setStones(pits[mover].getStones() - 1);
					mover--;
				}
			}
			firstPlayerUndos--; //decrement amount of undos.
			firstPlayerTurn = true; //set firstPlayerTurn back to true.
			changeState();
		}
		
		//undo function for player two.
		if(!secondPlayerTurn && secondPlayerUndos > 0) {
			pits[lastPit].setStones(lastPitStones);//set lastPitStones to original amount of stones;
			int mover = destinationPit; //mover set to destined pit for traversing back.
			if (pits[mover].getStones() == 1) { // case where the last pit had one stone after a turn 
				pits[12-mover].setStones(oppOnesPitStones);
				pits[13].setStones(pits[13].getStones() - oppOnesPitStones);
				for(int i = lastPitStones; i > 0; i--) {
					if(mover == -1) {
						mover = 13;
						pits[mover].setStones(pits[mover].getStones() - 1);
						continue;
					}
					if(mover == 6) {
						mover = 5;
					}
					pits[mover].setStones(pits[mover].getStones() - 1);
					mover--;
				}
			} else { //traverses the mancala board and decrements each pit by 1 stone until it reaches the pit before the last pit.
				for (int i = lastPitStones; i > 0; i--) {
					System.out.println(lastPitStones + " " + mover);
					if (mover == -1) {
						mover = 13;
					}
					if(mover == 6) {
						mover = 5;
					}
					pits[mover].setStones(pits[mover].getStones() - 1);
					mover--;
				}
			}
			secondPlayerUndos--; //decrement amount of undos.
			secondPlayerTurn = true; //set firstPlayerTurn back to true.
			changeState();
		}
	}
	
	/**
	 * Gets the data from the array of pits.
	 * @return an array of pits.
	 */
	public Pit[] getData() {
		return this.pits.clone();
	}
	
	/**
	 * Gets the player's turn. Player 1's turn returns true. Player 2's turn returns false
	 * @return boolean
	 */
	public boolean getTurn(){
		return firstPlayerTurn;
	}
	
	/**
	 * Attaches changelisteners to the model.
	 * @param l the listener to add.
	 */
	public void attach(ChangeListener l) {
		listeners.add(l);
	}
	
	/**
	 * Clears the board and sets to 0. Used when there is a winner.
	 */
	public void clearBoard() {
		for(int i = 0; i < 6; i++) {
			pits[i].setStones(0);
			pits[i+7].setStones(0);
		}
		changeState();
	}
	
	/**
	 * Used for deciding how many stones should be in each pit in the beginning of the game.
	 * @param num Number of stones each pit should start off with.
	 */
	public void setStones(int num) {
		for(int i = 0; i < pits.length; i++) {
			if(i == 6 || i == 13) {
				continue;
			}
			pits[i].setStones(num);
		}
		changeState();
	}
	
	/**
	 * Changes the state of the listeners.
	 */
	public void changeState() {
		for(ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
	
	/**
	 * determine the winner
	 * @param side boolean to determine which side has 0 pits. true for firstplayer false for secondplayer
	 */
	public void determineWinner(boolean side, int stones) {
		if(side) { //this means that first player has no more pits.
			pits[13].setStones(pits[13].getStones() + stones); //set second mancala stones to current stones and how many stones were left on second player side.
			if (pits[6].getStones() > pits[13].getStones()) {
				System.out.println("First player won");
				firstPlayerIsWinner = true;
			} else if(pits[13].getStones() > pits[6].getStones()){
				System.out.println("Second player won");
				secondPlayerIsWinner = true;
			} else {
				System.out.println("Tied game!");
				tied = true;
			}
		} else {
			pits[6].setStones(pits[6].getStones() + stones);
			if (pits[6].getStones() > pits[13].getStones()) {
				System.out.println("First player won");
				firstPlayerIsWinner = true;
			} else if(pits[13].getStones() > pits[6].getStones()){
				System.out.println("Second player won");
				secondPlayerIsWinner = true;
			} else {
				System.out.println("Tied game!");
				tied = true;
			}
		}
		gameIsOver = true;
		clearBoard();
	}
	
	/**
	 * Gets the error message whenever a game logic error occurs.
	 * @return the error
	 */
	public String getErrorMessage() {
		return errorMsg;
	}
}
