import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameActionListener implements ActionListener {
	private int row;
	private int cell;
	private GameButton button;

	public GameActionListener(int row, int cell, GameButton button) {
		this.row = row;
		this.cell = cell;
		this.button = button;
	}

	//ход человека
	void updateByPlayersData(GameBoard board) {
		board.updateGameField(row, cell);
		if (board.isClassicSymbols()) {
			button.setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
		} else {
			button.setIcon(board.getGame().getCurrentPlayer().getPlayerIcon());
		}
		checkResult(board);
	}

	void checkResult(GameBoard board) {
		if (board.checkWin()) {
			button.getGameBoard().getGame().showMessage("Выиграл " + board.getGame().getCurrentPlayer().getPlayerSign() +"!");
			board.emptyField();
		} else if (board.isFull()) {
			board.getGame().showMessage("Ничья!");
			board.emptyField();
		} else {
			board.getGame().passTurn();
		}
	}

	//ход компьютера
	void updateByAiData(GameBoard board){
		int[] coordinates;
		coordinates = board.findBestStrategy();
		board.updateGameField(coordinates[0], coordinates[1]);
		int cellIndex = GameBoard.dimension * coordinates[0] + coordinates[1];
		if (board.isClassicSymbols()) {
			board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
		} else {
			board.getButton(cellIndex).setIcon(board.getGame().getCurrentPlayer().getPlayerIcon());
		}
		checkResult(board);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GameBoard board = button.getGameBoard();
		if (board.isTurnable(row, cell)) {
			updateByPlayersData(board);
			if (!board.getGame().secondPlayer()) {
				updateByAiData(board);
			}
		} else {
			board.getGame().showMessage("Некорректный ход!");
		}
	}
}
