class TempleStone extends Stone {
    protected int destRow;
    protected int destCol;

    public TempleStone(int r, int c) {
        destRow = r;
        destCol = c;
    }

    public boolean hasDestination(int row, int col) {
        return row == destRow && col == destCol;
    }

    public boolean equals(Object o) {
        if (o instanceof TempleStone)
            return ((TempleStone)o).destRow == destRow && ((TempleStone)o).destCol == destCol;
        else
            return false;
    }

    public int getDestRow() { return destRow; }
    public int getDestCol() { return destCol; }
}

