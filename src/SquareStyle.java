
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * CS151 Project
 * @author Kenny Huynh, Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Represents a style with square pits. A concrete strategy.
 */
public class SquareStyle implements BoardStyle {

	Shape[] pits;
	Pit[] modelPits;
	boolean turn;
	final String mancala = "MANCALA";

	@Override
	public Shape[] drawPits() {
		pits = new Shape[14];
		int x = 200;
		int topY = 100;
		int botY = 250;
		for(int i = 0; i < 6; i++) {
			pits[i] = new Rectangle2D.Double(x, botY, 75, 75); //this draws the bottom pits.
			pits[12-i] = new Rectangle2D.Double(x, topY, 75, 75); //this draws the top pits.
			x += 100;
		}

		pits[6] = new Rectangle2D.Double(850, topY, 125, 250); //firstPlayer mancala
		pits[13] = new Rectangle2D.Double(50, topY, 125, 250); //secondPlayer mancala

		return pits;
	}

	@Override
	public void drawBoard(Graphics2D g2, MancalaModel model) {
		modelPits = model.getData();
		turn = model.getTurn();
		int stones;

		String status = "";
		int x = 220;
		int y = 150;
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g2.drawString("Undos: " + model.secondPlayerUndos, 75, 80);
		g2.drawString("Undos: " + model.firstPlayerUndos,875, 80);
		//this for loop is used for drawing MANCALA next to the mancala pits.
		for(int i = 0; i < mancala.length(); i++) {
			g2.drawString(mancala.substring(i, i+1), 20, y);
			g2.drawString(mancala.substring(i, i+1), 990, y);
			y += 20;
		}
		g2.drawString("B", 20, y + 30);
		g2.drawString("A", 990, y + 30);

		//used for labeling each pit. B6 B5 B4 ...
		for(int i = 1; i < 7; i++) {
			g2.drawString("B" + (7-i), x, 90);
			g2.drawString("A" + (i), x, 355);
			x += 100;
		}

		//drawing the stones in each pit
		for(int i = 0; i < pits.length; i++) {
			stones = modelPits[i].getStones();
			g2.setColor(Color.WHITE);
			g2.fill(pits[i]);

			if(i!= 6 && i!= 13){
				for(int j = 0; j < stones; j++) {
					g2.setColor(new Color((int)(Math.random()*254),(int)(Math.random()*254),(int)(Math.random()*254)));
					g2.fill(new Rectangle2D.Double((float) pits[i].getBounds2D().getMinX() + (Math.random() * 50) + 10,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 50) + 10, 15, 15));
				}
			}
			if(i==6 | i==13){
				for(int j = 0; j < modelPits[i].getStones(); j++) {
					g2.setColor(new Color((int)(Math.random()*254),(int)(Math.random()*254),(int)(Math.random()*254)));
					g2.fill(new Rectangle2D.Double((float) pits[i].getBounds2D().getMinX() + (Math.random() * 100) + 10,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 200) + 10, 15, 15));
				}
			}

			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2.drawString(Integer.toString(modelPits[i].getStones()), (float)pits[i].getBounds2D().getCenterX(), (float)pits[i].getBounds2D().getCenterY());
			g2.drawString("<-- Player B", 425, 60);
			g2.drawString("--> Player A", 425, 400);

			//this is used to show the winner after the game is over. 
			if (model.gameIsOver) {
				g2.drawString("Game Over!", 450, 420);
				g2.drawString("Player A score: " + modelPits[6].getStones(), 425, 440);
				g2.drawString("Player B score: " + modelPits[13].getStones(), 425, 460);
				g2.setFont(new Font("TimesRoman", Font.BOLD, 25));
				if(model.firstPlayerIsWinner) {
					g2.drawString("Player A wins!", 425, 480);
				} else if(model.secondPlayerIsWinner) {
					g2.drawString("Player B wins!", 425, 480);
				} else {
					g2.drawString("Tied game!", 425, 480);
				}
			}

			//only display turns when the game isn't over yet.
			if (!model.gameIsOver) {
				if (turn) {
					status = "Player A's Turn";
					g2.drawString(status, 800, 20);
				} else {
					status = "Player B's Turn";
					g2.drawString(status, 800, 20);
				}
			}

			if(model.error) {
				g2.drawString("ERROR: " + model.getErrorMessage(), 230,40);
			}
		}
	}

}
