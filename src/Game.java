import java.util.*;

public class Game {
    Board board;
//    static int blackScore = 0;
//    static int whiteScore = 0;
    private static final Map<Integer, Double> STICKS = Map.of(
            1, 4.0 / 16.0,
            2, 6.0 / 16.0,
            3, 4.0 / 16.0,
            4, 1.0 / 16.0,
            5, 1.0 / 16.0
    );
    public void startGame() {
        this.board = new Board();
        this.board.initBoard();
        this.board.printBoard(this.board.squares);

        while (!this.board.isFinal()) {

            this.board = whitePlay();
            if (this.board.isFinal()) break;

            this.board = blackPlay();
        }


    }

    private Board applyAction(Board board, Action action, Player player) {
        Board copiedBoard = board.deepCopy();
        if (action.isExitBoard()) {
            if (player == Player.BLACK) {
                copiedBoard.blackScore++;
            } else {
                copiedBoard.whiteScore++;
            }
            copiedBoard.squares.get(action.getFirstPosition()).setPlayer(null);
//            System.out.println("Black score: " + board.blackScore + "\t\t White score: " + board.whiteScore);
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
    public List<Board> generateNextStates(Board board,int roll,Player player) {
        List<Action> actions = this.getPossibleActions(board, roll, player);
        List<Board> states = new ArrayList<>();
        for (Action action : actions) {
            Board next = applyAction(board, action, player);
            states.add(next);
        }
        return states;
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
            Action action = actions.get(i);
            Board preview = applyAction(board, action, player);

            System.out.println((i + 1) + ") " + action);
            preview.printBoard(preview.squares);
            System.out.println("|---------------------------------------------------------------||");

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
        System.out.println("White You can move " + roll + " steps");
        Board child = chooseAndApply(this.board.deepCopy(), roll, Player.WHITE);
        System.out.println("Black score: " + child.blackScore + "\t\t White score: " + child.whiteScore);
        System.out.println("|---------------------------------------------------------------||");
        child.printBoard(child.squares);
        return child;
    }

    public Board blackPlay() {
        int roll = this.getRandomNumber();
        System.out.println("Black (Expectimax) You can move " + roll + " steps");

        int depth = 3;

        List<Action> actions = getPossibleActions(this.board, roll, Player.BLACK);

        if (actions.isEmpty()) {
            System.out.println("Black has no possible moves.");
            return this.board;
        }

        System.out.println("Black possible actions (Expectimax evaluation):");

        double bestValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);
            Board next = applyAction(this.board.deepCopy(), action, Player.BLACK);

            double value = chanceNode(next, depth - 1, Player.WHITE);

            System.out.printf(
                    "%d) %s  => Expected Value = %.2f%n",
                    i + 1,
                    action.toString(),
                    value
            );

            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
        }

        System.out.println("Black (Expectimax) chooses: " + bestAction.toString());

        Board child = applyAction(this.board, bestAction, Player.BLACK);
        System.out.println("Black score: " + child.blackScore + "\t\t White score: " + child.whiteScore);
        System.out.println("|---------------------------------------------------------------||");
        child.printBoard(child.squares);

        return child;
    }

    private int getRandomNumber() {
        return (int) (Math.random() * 5 + 1);
    }
    private double evaluateBoard(Board board) {
        //  Black score minus White score
        int blackProgress = board.blackScore;
        int whiteProgress = board.whiteScore;

        // sum of positions of pieces closer to the end
        for (Square sq : board.squares.values()) {
            if (sq.getPlayer() == Player.BLACK) blackProgress += sq.getIndex();
            else if (sq.getPlayer() == Player.WHITE) whiteProgress += sq.getIndex();
        }

        return blackProgress - whiteProgress;
    }


    private double chanceNode(Board board, int depth, Player player) {
        if (depth == 0 || board.isFinal()) {
            return evaluateBoard(board);
        }

        double expected = 0;

        for (int roll = 1; roll <= 5; roll++) {
            double p = STICKS.get(roll);

            expected += p * decisionNode(board, depth, player, roll);
        }

        return expected;
    }

    private double decisionNode(Board board, int depth, Player player, int roll) {
        List<Action> actions = getPossibleActions(board, roll, player);

        if (actions.isEmpty()) {
            return evaluateBoard(board);
        }

        double best;

        if (player == Player.BLACK) { // MAX
            best = Double.NEGATIVE_INFINITY;
            for (Action a : actions) {
                Board next = applyAction(board, a, player);
                best = Math.max(best,
                        chanceNode(next, depth - 1, Player.WHITE));
            }
        } else { // White (MIN or assumed optimal)
            best = Double.POSITIVE_INFINITY;
            for (Action a : actions) {
                Board next = applyAction(board, a, player);
                best = Math.min(best,
                        chanceNode(next, depth - 1, Player.BLACK));
            }
        }

        return best;
    }
    private Action chooseBestAction(Board board, int depth, int roll) {
        double bestValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        List<Action> actions = getPossibleActions(board, roll, Player.BLACK);

        for (Action action : actions) {
            Board next = applyAction(board, action, Player.BLACK);
            double value = chanceNode(next, depth - 1, Player.WHITE);

            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
        }

        return bestAction;
    }


}