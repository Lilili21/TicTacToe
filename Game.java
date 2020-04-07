import javax.swing.*;

public class Game {
    private GameBoard    board;
    private GamePlayer[] players = new GamePlayer[2];
    private int playersTurn = 0;

    public Game() {
        this.board = new GameBoard(this);
    }

    void initGame(boolean second){
        players[0] = new GamePlayer(true, 'X', new ImageIcon("src/icons8-O.png"));
        players[1] = new GamePlayer(second, 'O', new ImageIcon("src/icons8-X.png"));
    }
    boolean secondPlayer() {
        return players[1].isRealPlayer();
    }
    void passTurn(){
        playersTurn = (playersTurn == 0) ? 1 : 0;
    }
    public int getPlayersTurn() {
        return playersTurn;
    }
    GamePlayer getCurrentPlayer(){
        return players[playersTurn];
    }
    void showMessage(String messageText){
        JOptionPane.showMessageDialog(board, messageText);
    }
}
