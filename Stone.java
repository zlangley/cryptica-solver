class Stone {
    public static final Stone BLOCKING_STONE = new Stone();
    public static final Stone MOVING_STONE = new Stone();

    private int destRow;
    private int destCol;

    private Stone() { }

    public Stone(int r, int c) {
        destRow = r;
        destCol = c;
    }

    public boolean hasDestination(int row, int col) {
        return row == destRow && col == destCol;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (o != BLOCKING_STONE && o != MOVING_STONE)
            return ((Stone)o).destRow == destRow && ((Stone)o).destCol == destCol;
        else
            return false;
    }

    public boolean isTempleStone() {
        return this != BLOCKING_STONE && this != MOVING_STONE;
    }

    public boolean isMovingStone() {
        return this == MOVING_STONE;
    }

    public boolean isBlockingStone() {
        return this == BLOCKING_STONE;
    }

    public int getDestRow() { return destRow; }
    public int getDestCol() { return destCol; }
}
