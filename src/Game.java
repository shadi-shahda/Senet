import java.util.*;

public class Game {
    Board board;

    public void startGame() {
        this.board = new Board();
        this.board.initBoard();
        this.board.printBoard(this.board.squares);
        while (!this.board.isFinal()) {
            Board child = this.whitePlay();
            this.blackPlay();
        }

    }

    private Board applyAction() {
        return new Board();
    }


    public Board whitePlay() {
        int roll = this.getRandomNumber();
        System.out.println("You can move " + roll + " steps");
        // TODO: apply get possible action
        // TODO: choose action and apply it
        return new Board();
    }

    public void blackPlay() {
        int roll = this.getRandomNumber();
        // todo expect-mini-max algorithm
        // todo apply action
    }

    private int getRandomNumber() {
        return (int) (Math.random() * 5 + 1);
    }
}
