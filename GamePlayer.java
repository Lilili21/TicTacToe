import javax.swing.*;

public class GamePlayer {
    private char playerSign;
    private ImageIcon playerIcon;
    private boolean realPlayer = true;

    public GamePlayer(boolean realPlayer, char playerSign, ImageIcon playerIcon) {
        this.playerSign = playerSign;
        this.realPlayer = realPlayer;
        this.playerIcon = playerIcon;
    }
    public void setRealPlayer(boolean realPlayer) {
        this.realPlayer = realPlayer;
    }
    public boolean isRealPlayer() {
        return realPlayer;
    }
    public char getPlayerSign() {
        return playerSign;
    }
    public ImageIcon getPlayerIcon() {
        return playerIcon;
    }
}
