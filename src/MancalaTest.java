
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * CS151 Project
 * @author Kenny Huynh, Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Represents the main method to run the program.
 */
public class MancalaTest {
		static JFrame styleSelectFrame;
		static MancalaModel model;
		
	/**
	 * Main method to run the program.
	 * @param args Arguments inputted.
	 */
	public static void main(String[] args) {
		model = new MancalaModel();
		styleSelectFrame = new JFrame();
		styleSelectFrame.setSize(300, 150);
		JTextArea text = new JTextArea("Welcome to Mancala. Please choose your style.");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		JButton circleView =  new JButton("Circle");
		JButton squareView = new JButton("Square");
		circleView.addActionListener(chooseView(new CircleStyle()));
		squareView.addActionListener(chooseView(new SquareStyle()));
		panel.add(circleView);
		panel.add(squareView);
		styleSelectFrame.setLayout(new BoxLayout(styleSelectFrame.getContentPane(),BoxLayout.Y_AXIS));
		styleSelectFrame.add(text);
		styleSelectFrame.add(panel);
		styleSelectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		styleSelectFrame.setVisible(true);
		
	}
	
	/**
	 * Method to determine which style the user wants.
	 * @param strat the style or strategy to be used
	 * @return an actionlistener that draws the board with the given strategy.
	 */
	public static ActionListener chooseView(BoardStyle strat) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				styleSelectFrame.setVisible(false); 
				JFrame board = new JFrame();
				final MancalaModel myModel = new MancalaModel();
				View view = new View(myModel, strat);
				
				JPanel inputUndo = new JPanel();
				inputUndo.setLayout(new FlowLayout());
				inputUndo.setSize(600, 40);
				view.getMainMenu().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board.setVisible(false);
						styleSelectFrame.setVisible(true);
					}
				});
				view.getMainMenu().setVisible(false);
				view.getUndoButton().setVisible(false);
				JTextArea prompt = new JTextArea("Please enter number of stones: 3 or 4.");
				prompt.setEditable(false);
				JTextField input = new JTextField("", 10);
				input.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String text = input.getText().trim();
						try {
							int stones = Integer.parseInt(text);
							if (stones == 3 || stones == 4) {
								myModel.setStones(stones);
								prompt.setVisible(false);
								input.setVisible(false);
								view.getUndoButton().setVisible(true);
							} else {
								throw new Exception();
							}
						} catch (Exception error) {
							prompt.setText("Error. Either not a number or number is not 3 or 4.");
						}
					}
				});
				
				inputUndo.add(prompt);
				inputUndo.add(input);
				inputUndo.add(view.getUndoButton());
				inputUndo.add(view.getMainMenu());
				board.add(inputUndo, BorderLayout.NORTH);
				board.add(view, BorderLayout.CENTER);

				board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				board.setSize(1050, 600);
				board.setVisible(true);
			}
		};
	}
}
