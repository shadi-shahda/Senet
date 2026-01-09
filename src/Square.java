public class Square {
    private int index;
    private SquareType squareType;
    private Player player;

    public Square(int index, SquareType squareType) {
        this.index = index;
        this.squareType = squareType;
        this.player = null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SquareType getSquareType() {
        return squareType;
    }

    public void setSquareType(SquareType squareType) {
        this.squareType = squareType;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}