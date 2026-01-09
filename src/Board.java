import java.util.HashMap;
import java.util.Map;

public class Board {
    Map<Integer, Square> squares;

    public Board() {
        this.squares = new HashMap<>();
    }


    public Board(Map<Integer, Square> squares) {
        this.squares = new HashMap<>(squares);
    }

    public void initBoard() {
        int size = 30;
        for (int i = 1; i <= size; i++) {
            if (i <= 10) {
                SquareType type = SquareType.NORMAL;
                Player player = (i % 2 == 0) ? Player.BLACK : Player.WHITE;
                Square square = new Square(i, type);
                square.setPlayer(player);
                this.squares.put(i, square);
            } else if (i <= 20) {
                SquareType type = SquareType.NORMAL;
                Square square = new Square(i, type);
                if (i >= 15) {
                    type = i == 15 ? SquareType.NEWSTARTBOX : type;
                    square.setSquareType(type);
                    this.squares.put(i, square);
                    continue;
                }
                Player player = (i % 2 == 0) ? Player.BLACK : Player.WHITE;
                square.setPlayer(player);
                this.squares.put(i, square);
            } else {
                SquareType type = switch (i) {
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

    public boolean isFinal() {
        boolean noBlack = true;
        boolean noWhite = true;
        for (Map.Entry<Integer, Square> entry : this.squares.entrySet()) {
            if (entry.getValue().getPlayer() != null) {
                if (entry.getValue().getPlayer() == Player.BLACK) {
                    noBlack = false;
                }
                if (entry.getValue().getPlayer() == Player.WHITE) {
                    noWhite = false;
                }
            }
        }
        if (noBlack) {
            System.out.println("Black has won");
        } else if (noWhite) {
            System.out.println("White has won");
        }
        return (noBlack || noWhite);
    }

    public Board deepCopy() {
        return new Board(this.squares);
    }

    public void printBoard(Map<Integer, Square> squares) {
        for (int i = 1; i <= 10; i++) {
            printSquare(i, squares);
        }
        System.out.println();

        for (int i = 20; i >= 11; i--) {
            printSquare(i, squares);
        }
        System.out.println();

        for (int i = 21; i <= 30; i++) {
            printSquare(i, squares);
        }
        System.out.println();
    }

    private void printSquare(int index, Map<Integer, Square> squares) {
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