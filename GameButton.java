import javax.swing.*;

public class GameButton extends JButton {
	private GameBoard board;

	public GameButton(int gameButtonIndex, GameBoard currentGameBoard){
		this.board = currentGameBoard;
		int rowNum = gameButtonIndex / GameBoard.dimension;
		int cellNum = gameButtonIndex % GameBoard.dimension;

		setSize(GameBoard.cellSize - 5, GameBoard.cellSize - 5);
		addActionListener(new GameActionListener(rowNum, cellNum, this));
	}
	public GameBoard getGameBoard() {
		return board;
	}
}
