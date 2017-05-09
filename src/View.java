

import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * CS151 Project
 * @author Kenny Huynh. Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Represents the GUI portion or context of the Strategy pattern. This is where the model will interact with the controller and update the view.
 */
public class View extends JPanel implements ChangeListener {
	
	private MancalaModel model;
	private BoardStyle style;
	private Shape[] pits;
	private JButton mainMenu;
	private JButton undo;
	
	/**
	 * Intializes a View object.
	 * @param model the model to be used
	 * @param style the strategy/style to be used for this view.
	 */
	public View(MancalaModel model, BoardStyle style) {
		this.model = model;
		this.style = style;
		this.model.attach(this);
		pits = style.drawPits();
		this.setVisible(true);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for(int i = 0; i < pits.length; i++) { //check to see if the point is clicked within the pit.
					if(pits[i].contains(e.getPoint())) {
						model.moveBoard(i);
					}
				}
			}
		});
		
		//put undo/mainMenu in View class so that it updates automatically due to the state change method.
		undo = new JButton("Undo");
		mainMenu = new JButton("Main Menu");
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.undo();
			}
		});
	}
	
	/**
	 * Gets the undo JButton
	 * @return the undo JButton
	 */
	public JButton getUndoButton() {
		return undo;
	}
	
	/**
	 * Gets the main menu JButton
	 * @return the main menu JButton
	 */
	public JButton getMainMenu() {
		return mainMenu;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		pits = this.style.drawPits();
		style.drawBoard(g2, this.model);
		
		//if game is over, make undo disappear and mainMenu appear.
		if(model.gameIsOver) {
			undo.setVisible(false);
			mainMenu.setVisible(true);
		}
	}
	
	@Override
	//this will update the view whenever the board's state is changed.
	public void stateChanged(ChangeEvent arg0) {
		this.repaint();
		
	}
	
}