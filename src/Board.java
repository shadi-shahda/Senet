import java.util.HashMap;
import java.util.Map;

public class Board {
    Map<Integer, Square> squares;

    public Board() {
        this.squares = new HashMap<>();
        initBoard();
    }

    private void initBoard() {

        for (int i = 1; i <= 30; i++) {
            if (i < 15) {
                SquareType type = SquareType.NORMAL;
                Player player = (i % 2 == 0) ? Player.WHITE : Player.BLACK;
                Square square = new Square(i, type);
                square.setPlayer(player);
                this.squares.put(i, square);
            } else {
                SquareType type = switch (i) {
                    case 15 -> SquareType.NEWSTARTBOX;
                    case 26 -> SquareType.BARRIERBOX;
                    case 27 -> SquareType.RETURNSQUARE;
                    case 28 -> SquareType.THREESQUARE;
                    case 29 -> SquareType.TWOSQUARE;
                    case 30 -> SquareType.FREESQUARE;
                    default -> SquareType.NORMAL;
                };
                Square square = new Square(i, type);
                this.squares.put(i, square);
            }
        }
    }

    public void printBoard() {
        for (int i = 1; i <= 10; i++) {
            printSquare(i);
        }
        System.out.println();

        for (int i = 20; i >= 11; i--) {
            printSquare(i);
        }
        System.out.println();

        for (int i = 21; i <= 30; i++) {
            printSquare(i);
        }
        System.out.println();
    }

    private void printSquare(int index) {
        Square sq = squares.get(index);
        String symbol;

        if (sq.getPlayer() != null) {
            symbol = sq.getPlayer().toString().substring(0, 1);
        } else if (sq.getSquareType() != SquareType.NORMAL) {
            symbol = sq.getSquareType().name().substring(0, 2);
        } else {
            symbol = "--";
        }

        System.out.printf("%-4s", symbol);
    }

}
