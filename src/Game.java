import java.util.*;

public class Game {
    Board board;
    static int blackScore = 0;
    static int whiteScore = 0;

    public void startGame() {
        this.board = new Board();
        this.board.initBoard();
        this.board.printBoard(this.board.squares);

        while (!this.board.isFinal()) {
            Board child;
            child = this.whitePlay();
            this.blackPlay();
        }

    }

    private Board applyAction(Board board, Action action, Player player) {
        Board copiedBoard = board.deepCopy();
        if (action.isExitBoard()) {
            if (player == Player.BLACK) {
                blackScore++;
            } else {
                whiteScore++;
            }
            copiedBoard.squares.get(action.getFirstPosition()).setPlayer(null);
            System.out.println("Black: " + blackScore + "\t\t White: " + whiteScore);
        } else {
            for (int i = 27; i < 30; i++) {
                Player invalidPlayer = copiedBoard.squares.get(i).getPlayer();
                if (invalidPlayer != null && invalidPlayer == player) {
                    for (int j = 15; j >= 1; j--) {
                        if (copiedBoard.squares.get(j).getPlayer() == null) {
                            copiedBoard.squares.get(j).setPlayer(invalidPlayer);
                            copiedBoard.squares.get(i).setPlayer(null);
                            break;
                        }
                    }
                }
            }
            if (copiedBoard.squares.get(action.getSecondPosition()).getPlayer() == player.other()) {
                copiedBoard.squares.get(action.getFirstPosition()).setPlayer(player.other());
                copiedBoard.squares.get(action.getSecondPosition()).setPlayer(player);
            } else {
                copiedBoard.squares.get(action.getFirstPosition()).setPlayer(null);
                copiedBoard.squares.get(action.getSecondPosition()).setPlayer(player);
            }
            // moving to see after applying the move
            if (action.getSecondPosition() == 27) {
                for (int j = 15; j >= 1; j--) {
                    if (copiedBoard.squares.get(j).getPlayer() == null) {
                        copiedBoard.squares.get(j).setPlayer(player);
                        copiedBoard.squares.get(27).setPlayer(null);
                        break;
                    }
                }
            }
        }
        return copiedBoard;
    }

    public Board chooseAndApply(Board board, int roll, Player player) {
        List<Action> actions = this.getPossibleActions(board, roll, player);
        // TODO: get list of states from generateNextStates method (name it states)
        if (actions.isEmpty()) {
            System.out.println("No possible actions available.");
            return board;
        }

        System.out.println("Possible actions:");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println(i + 1 + ") " + actions.get(i).toString());
            // TODO: board.printBoard(states.get(i));
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
        Board child = applyAction(board, action, player);
        System.out.println("Applied action: " + action.toString());
        return child;
    }


    public List<Action> getPossibleActions(Board board, int roll, Player player) {
        List<Action> actions = new ArrayList<>();
        for (Map.Entry<Integer, Square> square : board.squares.entrySet()) {
            if (square.getValue().getPlayer() != null && square.getValue().getPlayer() == player) {
                if (square.getKey() >= 26) {
                    Action action = new Action(square.getKey(), square.getKey() + roll);
                    switch (square.getValue().getSquareType()) {
                        case THREESQUARE -> {
                            if (roll == 3) {
                                action.setExitBoard(true);
                                actions.add(action);
                            }
                        }
                        case TWOSQUARE -> {
                            if (roll == 2) {
                                action.setExitBoard(true);
                                actions.add(action);
                            }
                        }
                        case FREESQUARE -> {
                            action.setExitBoard(true);
                            actions.add(action);
                        }
                        case BARRIERBOX -> {

                            if (roll == 5) {
                                action.setExitBoard(true);
                            }
                            actions.add(action);
                        }
                    }
                    continue;
                }
                if (board.squares.get(square.getKey() + roll).getPlayer() == player) continue;
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
        Board child = chooseAndApply(this.board.deepCopy(), roll, Player.WHITE);
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
