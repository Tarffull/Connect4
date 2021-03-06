import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Creates and runs a game of Connect 4. Contains the graphic interface.
 * @author Tova Linder och Pontus Persson
 *
 */
@SuppressWarnings("serial")
public class Game extends JFrame implements MouseListener {
	private static Board board;
	private JPanel gamePanel, statsPanel, containerPanel;
	private int rows, cols;	
	private boolean bot = false;
	
	public Player playerRed = new Player(MarkerType.RED, "Red");
	public Player playerYellow = new Player(MarkerType.YELLOW, "Yellow");
	public AI botPlayer = new AI(MarkerType.YELLOW, "Easy");
	
	private Player currentPlayer;
	private JLabel text;

	/**
	 * Creates new game of Connect 4.
	 * 
	 * @param winWidth
	 *            Game window width.
	 * @param winHeight
	 *            Game window height.
	 * @param cols
	 *            Number of columns on board.
	 * @param rows
	 *            Number of rows on board.
	 */
	@SuppressWarnings("static-access")
	public Game(int winWidth, int winHeight, int cols, int rows) {
		super("Fyra i rad");
		currentPlayer = playerRed;
		this.cols = cols;
		this.rows = rows;

		board = new Board(rows, cols);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(winWidth, winHeight);
		setResizable(true);
		setLocationRelativeTo(null);
		containerPanel = new JPanel();
		statsPanel = new JPanel();
		gamePanel = new JPanel(new GridLayout(rows, cols));

		GridBagLayout gridbagLayoutSettings = new GridBagLayout();
		containerPanel.setLayout(gridbagLayoutSettings);

		GridBagConstraints gb = new GridBagConstraints();

		text = new JLabel("Red player: 0 Yellow player: 0");
		text.setForeground(Color.white);
		text.setFont(new Font("Serif", Font.BOLD, 20));
		statsPanel.setBorder(BorderFactory.createLineBorder(Color.white, 5));
		statsPanel.add(text);
		statsPanel.setBackground(board.getColor());

		for (int pos = (rows * cols) - cols; pos >= 0; pos -= cols) {

			for (int i = 0; i < cols; i++) {
				// panel.add(new Marker(board.getMarkerPos(pos+i), pos+i));
				Marker marker = new Marker(board.getMarkerType(pos + i), pos
						+ i);
				marker.addMouseListener(this);
				gamePanel.add(marker);

			}

		}
		gamePanel.setBackground(Board.getColor());
		gb.weighty = 0.0;
		gb.weightx = 1.0;
		gb.gridx = 0;
		gb.gridy = 0;
		// gb.gridheight=1;
		gb.fill = GridBagConstraints.HORIZONTAL;

		containerPanel.add(statsPanel, gb);

		gb.gridx = 0;
		gb.gridy = 1;
		gb.weighty = 1.0;
		gb.weightx = 1.0;
		gb.fill = GridBagConstraints.BOTH;
		containerPanel.add(gamePanel, gb);
		add(containerPanel);
		setVisible(true);

		new StartDialog(this);

	}

	/**
	 * Starts a new thread for GUI.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Game(800, 600, 7, 6);

			}
		});

	}
/**
 * Returns the value of the variable bot.
 * @return True is bot game, else false.
 */
	public boolean isBotGame() {
		return this.bot; 
	}
	/**
	 * Set the value of the variable bot.
	 * @param isBotGame
	 */
	public void setBot(boolean isBotGame) {
		this.bot = isBotGame;
	}
	
	/**
	 * @param pos
	 *            Position on board.
	 * @return Returns the MarkerType currently occupying the position.
	 */
	public static MarkerType getBoardMarker(int pos) {
		return board.getMarkerType(pos);
	}

	/**
	 * Actions to perform when mouse is clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Marker clicked = (Marker) e.getSource();
		int pos = clicked.getPos();
		if (bot) {
			humanBot(pos);
			text.setText("Red player: " + playerRed.getMoves()
					+ "    Yellow player: " + botPlayer.getMoves());

		} else {
			humanHuman(pos);
			text.setText("Red player: " + playerRed.getMoves()
					+ "    Yellow player: " + playerYellow.getMoves());
		}
		
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Performs the moves and checks for winner when playing human vs. human.
	 * 
	 * @param pos
	 *            Position selected for move.
	 */
	public void humanHuman(int pos) {
		try {
			board.placeMove(pos, currentPlayer);;
			if (currentPlayer.getColor() == board.checkWin(currentPlayer)) {
				new WinDialog(currentPlayer, this);
			}
			
			if (currentPlayer == playerRed) {
				currentPlayer = playerYellow;

			} else {
				currentPlayer = playerRed;

			}
		} catch (NoSpaceLeftInColumnException e1) {
			if(board.isBoardFull()) {
				new WinDialog(); 
			}
		}

	}

	/**
	 * Performs the moves and checks for winner when playing human vs. bot.
	 * 
	 * @param pos
	 *            Position selected for move.
	 */
	public void humanBot(int pos) {
		try {
			board.placeMove(pos, currentPlayer);
			if (board.checkWin(currentPlayer) == currentPlayer.getColor() ) {
				new WinDialog(currentPlayer, this);
			}
			if (board.checkWin(currentPlayer) == MarkerType.EMPTY) {
				board.placeMove(
						botPlayer.getMove(board, rows*cols-1),
						botPlayer);
			}
			if (board.checkWin(botPlayer) == botPlayer.getColor()) {
				new WinDialog(botPlayer, this);
			}
		} catch (NoSpaceLeftInColumnException e1) {
			if(board.isBoardFull()) {
				new WinDialog(); 
			}
		}

	}

	/**
	 * Resets the game.
	 */
	public void resetGame() {
		board.emptyBoard();
		currentPlayer = playerRed;
		playerRed.resetMoves(); 
		playerYellow.resetMoves();
		botPlayer.resetMoves();
		text.setText("Red player: 0 Yellow player: 0");
		repaint();
		new StartDialog(this);
	}

}
