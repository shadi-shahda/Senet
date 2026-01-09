public class Action {
    private int firstPosition;
    private int secondPosition;
    private boolean exitBoard;

    public Action(int firstPosition, int secondPosition) {
        this.firstPosition = firstPosition;
        this.secondPosition = secondPosition;
        this.exitBoard = false;
    }

    public int getFirstPosition() {
        return firstPosition;
    }

    public void setFirstPosition(int firstPosition) {
        this.firstPosition = firstPosition;
    }

    public int getSecondPosition() {
        return secondPosition;
    }

    public void setSecondPosition(int secondPosition) {
        this.secondPosition = secondPosition;
    }

    public boolean isExitBoard() {
        return exitBoard;
    }

    public void setExitBoard(boolean exitBoard) {
        this.exitBoard = exitBoard;
    }
}
