import javax.swing.*;

public class GameButton extends JButton {
	private GameBoard board;

	public GameButton(int gameButtonIndex, GameBoard currentGameBoard){
		this.board = currentGameBoard;
		int row = gameButtonIndex / GameBoard.dimension;
		int cell = gameButtonIndex % GameBoard.dimension;

		setSize(GameBoard.cellSize - 5, GameBoard.cellSize - 5);
		addActionListener(new GameActionListener(row, cell, this));
	}
	public GameBoard getGameBoard() {
		return board;
	}
}
