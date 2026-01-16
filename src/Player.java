public enum Player {
    BLACK, WHITE;

    public Player other() {
        return this == BLACK ? WHITE : BLACK;
    }
}