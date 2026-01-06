public class Game {
    Board board;
    public void startGame() {
        this.board = new Board();
        this.board.printBoard();
    }
}
