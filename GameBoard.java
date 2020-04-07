import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    static int              dimension = 3;
    static int              cellSize = 150;
    private char[][]        gameField;
    private GameButton[]    gameButtons;
    private Game            game;
    private boolean         classicSymbols;

    static char nullSymbol = '\u0000';

    public boolean isClassicSymbols() {
        return classicSymbols;
    }
    public void setClassicSymbols(boolean classicSymbols) {
        this.classicSymbols = classicSymbols;
    }

    public GameBoard(Game currentGame) {
        this.game = currentGame;
        initField();
    }
    private void initField(){
        this.classicSymbols = true;
        setBounds(cellSize * dimension, cellSize * dimension, 400,300);
        setTitle("Крестики-нолики");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

         setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("Новая игра");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField();
            }
        });
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        controlPanel.add(newGameButton);
        controlPanel.setSize(cellSize * dimension, 150);
        JButton smileButton = new JButton("Смайлы");
        smileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField();
                if (isClassicSymbols()) {
                    smileButton.setText("X / O");
                    setClassicSymbols(false);
                } else {
                    smileButton.setText("Смайлы");
                    setClassicSymbols(true);
                }
            }
        });
        controlPanel.add(smileButton);
        JButton changePlayer = new JButton("Игрок vs Игрок");
        changePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField();
                if (game.secondPlayer()) {
                    changePlayer.setText("Игрок vs Игрок");
                    game.showMessage("Ваш противник компьютер");
                    game.initGame(false);
                } else {
                    changePlayer.setText("Игрок vs Компьютер");
                    game.showMessage("Игра для двоих игроков");
                    game.initGame(true);
                }

            }
        });
        controlPanel.add(changePlayer);
        JPanel gameFieldPanel = new JPanel();
        gameFieldPanel.setLayout(new GridLayout(dimension, dimension));
        gameFieldPanel.setSize(cellSize * dimension, cellSize * dimension);
        gameField = new char[dimension][dimension];
        gameButtons = new GameButton[dimension * dimension];
        for (int i = 0; i < dimension * dimension; i++) {
            GameButton fieldButton = new GameButton(i, this);
            gameFieldPanel.add(fieldButton);
            gameButtons[i] = fieldButton;
        }
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    Game getGame(){
        return game;
    }
    public GameButton getButton(int buttonIndex) {
        return gameButtons[buttonIndex];
    }
    void emptyField(){
        for(int i = 0; i < (dimension * dimension); i++) {
            gameButtons[i].setText("");
            gameButtons[i].setIcon(null);
            int x = i / GameBoard.dimension;
            int y = i % GameBoard.dimension;
            gameField[x][y] = nullSymbol;
            if (game.getPlayersTurn() == 1) {
                game.passTurn();
            }
        }
    }
    boolean isTurnable(int x, int y){
        return gameField[x][y] == nullSymbol;
    }
    void updateGameField(int x, int y){
        gameField[x][y] = game.getCurrentPlayer().getPlayerSign();
    }

    boolean checkDiagonal(char elem) {
        boolean checker1 = true;
        boolean checker2 = true;

        for (int i = 0, j = 0; i < dimension; i++, j++) {
            if (gameField[i][j] != elem) {
                checker1 = false;
            }
            if (gameField[i][dimension - j - 1] != elem) {
                checker2 = false;
            }
        }
        return checker1 || checker2;
    }
    boolean checkRowColumn(char elem) {
        boolean checker1;
        boolean checker2;

        for (int i = 0; i < dimension; i++) {
            checker1 = true;
            checker2 = true;
            for (int j = 0; j < dimension; j++) {
                if (gameField[i][j] != elem) {
                    checker1 = false;
                }
                if (gameField[j][i] != elem) {
                    checker2 = false;
                }
            }
            if (checker1 || checker2) {
                return true;
            }
        }
        return false;
    }
    boolean checkWin(){
        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();
        return checkDiagonal(playerSymbol) || checkRowColumn(playerSymbol);
    }

    int countDiagonal(char elem, int type) {
        int quantity = 0;
        for (int i = 0, j = dimension - 1; i < dimension; i++, j--) {
            if (type == 1) {
                if (gameField[i][i] == elem) quantity++;
                if (gameField[i][i] != nullSymbol && gameField[i][i] != elem) return -1;
            } else {
                if (gameField[i][j] == elem) quantity++;
                if (gameField[i][j] != nullSymbol && gameField[i][j] != elem) return -1;
            }
        }
        return quantity;
    }
    int countColumn(char elem, int column) {
        int quantity = 0;
        for (int i = 0; i < dimension; i++) {
            if (gameField[i][column] == elem) quantity++;
            if (gameField[i][column] != nullSymbol && gameField[i][column] != elem) return -1;
        }
        return quantity;
    }
    int countRow(char elem, int row) {
        int quantity = 0;
        for (int i = 0; i < dimension; i++) {
            if (gameField[row][i] == elem) quantity++;
            if (gameField[row][i] != nullSymbol && gameField[row][i] != elem) return -1;
        }
        return quantity;
    }
    int[] findBestStrategy() {
        int quantityColumn, quantityRow, quantityDiagonal;
        int numberOfChance;
        int weight;

        int[] params = {-1, -1, -1, -1}; // row, column, numberofchance, weight
        char elem = this.game.getCurrentPlayer().getPlayerSign();
        for (int i = 0; i < dimension; i++) {
            numberOfChance = 0;
            for (int j = 0; j < dimension; j++) {
                if (gameField[i][j] == nullSymbol) {
                    quantityDiagonal = (i==j) ? countDiagonal(elem, 1) :
                            (dimension == j + 1 + i) ? countDiagonal(elem, 2) : -1;
                    quantityColumn = countColumn(elem, j);
                    quantityRow = countRow(elem, i);
                    if (quantityColumn > 0) numberOfChance++;
                    if (quantityDiagonal > 0) numberOfChance++;
                    if (quantityRow > 0) numberOfChance++;
                    weight = Math.max(Math.max(quantityDiagonal, quantityColumn), quantityRow);
                    if ((params[3] < weight) || (params[3] == weight && params[2] < numberOfChance)) {
                        params[0] = i;
                        params[1] = j;
                        params[2] = numberOfChance;
                        params[3] = weight;
                    }
                }
            }
        }
        return params;
    }
    boolean isFull() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (gameField[i][j] == nullSymbol)
                    return false;
            }
        }
        return true;
    }
}
