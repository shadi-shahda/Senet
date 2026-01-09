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

    public Board chooseAndPlay(Board board, int roll, Player player) {
        List<Action> actions = this.getPossibleActions(roll, player);
        if (actions.isEmpty()) {
            System.out.println("No possible actions available.");
            return board;
        }

        System.out.println("Possible actions:");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println(i + 1 + ") " + actions.get(i).toString());
            board.printBoard(board.squares);
        }

        int choice = -1;
        do {
            System.out.print("Enter a number between (1 and " + actions.size() + ") ? ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.next();
            choice = Integer.parseInt(line);
        } while (choice < 1 || choice > actions.size());
        Action action = actions.get(choice - 1);
        Board child = applyAction();
        System.out.println("Applied action: " + action.toString());
        return child;
    }


    public List<Action> getPossibleActions(int roll, Player player) {
        List<Action> actions = new ArrayList<>();
        for (Map.Entry<Integer, Square> square : this.board.squares.entrySet()) {
            if (square.getValue().getPlayer() != null && square.getValue().getPlayer() == player) {
                if (this.board.squares.get(square.getKey() + roll).getPlayer() == player) continue;
                switch (square.getValue().getSquareType()) {
                    case THREESQUARE -> {
                        if (roll == 3) {
                            Action action = new Action(square.getValue().getIndex(),
                                    square.getValue().getIndex() + roll);
                            action.setExitBoard(true);
                            actions.add(action);
                        }
                        continue;
                    }
                    case TWOSQUARE -> {
                        if (roll == 2) {
                            Action action = new Action(square.getValue().getIndex(),
                                    square.getValue().getIndex() + roll);
                            action.setExitBoard(true);
                            actions.add(action);
                        }
                        continue;
                    }
                    case FREESQUARE -> {
                        Action action = new Action(square.getValue().getIndex(),
                                square.getValue().getIndex() + roll);
                        action.setExitBoard(true);
                        actions.add(action);
                        continue;
                    }
                    case BARRIERBOX -> {
                        Action action = new Action(square.getKey(), square.getKey() + roll);
                        if (roll == 5) {
                            action.setExitBoard(true);
                        }
                        actions.add(action);
                    }
                }
                // ignore the cells between last movable cell and BARRIER BOX
                // last movable cell (that moves to the BARRIER BOX)
                // 26 is BARRIER BOX index
                if (square.getKey() < 26 - roll + 1) {
                    int firstPosition = square.getKey();
                    int secondPosition = square.getKey() + roll;
                    Action action = new Action(firstPosition, secondPosition);
                    actions.add(action);
                }
            }
        }
        return actions;
    }

    public Board whitePlay() {
        int roll = this.getRandomNumber();
        System.out.println("You can move " + roll + " steps");
        Board child = chooseAndPlay(this.board.deepCopy(), roll, Player.WHITE);
        System.out.println("-----------------------------------------");
        child.printBoard(child.squares);
        return child;
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
