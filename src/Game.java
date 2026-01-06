public class Game {
    Board board;

    public void startGame() {
        this.board = Board.getInstance();
        this.board.printBoard();
        while (!this.board.isFinal()) {
            this.whitePlay();
            this.blackPlay();
        }
    }

    public void whitePlay() {
        int roll = this.getRandomNumber();
        System.out.println("You can move " + roll + " steps");
        // todo get possible actions
        // todo apply action
    }

    public void blackPlay() {
        int roll = this.getRandomNumber();
        // todo expect-mini-max algorithm
        // todo apply action
    }

    private int getRandomNumber() {
        return (int) (Math.random() * 6);
    }
}
