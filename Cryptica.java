public class Cryptica {
    private Stone[][] board;

    public Cryptica(int numRows, int numCols) {
        board = new Stone[numRows][numCols];
    }

    public Cryptica(Cryptica crypt) {
        board = new Stone[crypt.board.length][crypt.board[0].length];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = crypt.board[r][c];
            }
        }
    }

    public boolean solved() {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[r].length; c++)
                if (board[r][c] != null && board[r][c].isTempleStone())
                    if (!board[r][c].hasDestination(r, c))
                        return false;
        return true;
    }

    public void addTempleStone(int row, int col, int destRow, int destCol) { 
        board[row][col] = new Stone(destRow, destCol);
    }

    public void addMovingStone(int row, int col) {
        board[row][col] = Stone.MOVING_STONE;
    }

    public void addBlockingStone(int row, int col) {
        board[row][col] = Stone.BLOCKING_STONE;
    }

    public Cryptica moveRight() {
        for (int r = 0; r < board.length; r++) {
            for (int c = board[r].length - 1; c > 0; c--) {
                if (board[r][c] == null && board[r][c-1] != null &&
                        !board[r][c-1].isBlockingStone()) {
                    board[r][c] = board[r][c-1];
                    board[r][c-1] = null;
                }
            }
        }
        return this;
    }

    public Cryptica moveLeft() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length - 1; c++) {
                if (board[r][c] == null && board[r][c+1] != null &&
                        !board[r][c+1].isBlockingStone()) {
                    board[r][c] = board[r][c+1];
                    board[r][c+1] = null;
                }
            }
        }
        return this;
    }

    public Cryptica moveDown() {
        for (int r = board.length - 1; r > 0; r--) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null && board[r-1][c] != null &&
                        !board[r-1][c].isBlockingStone()) {
                    board[r][c] = board[r-1][c];
                    board[r-1][c] = null;
                }
            }
        }
        return this;
    }

    public Cryptica moveUp() {
        for (int r = 0; r < board.length - 1; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null && board[r+1][c] != null &&
                        !board[r+1][c].isBlockingStone()) {
                    board[r][c] = board[r+1][c];
                    board[r+1][c] = null;
                }
            }
        }
        return this;
    }

    @Override
    public int hashCode() {
        int ret = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null)
                    continue;

                int val = 3*r + 17*c;
                if (board[r][c].isBlockingStone())
                    val *= 2;
                else if (board[r][c].isTempleStone())
                    val *= 3;
                else if (board[r][c].isMovingStone())
                    val *= 5;
                ret += val;
            }
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        Cryptica other = (Cryptica)o;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] != null && other.board[r][c] == null)
                    return false;
                if (board[r][c] == null && other.board[r][c] != null)
                    return false;
                if (board[r][c] != null && other.board[r][c] != null && !board[r][c].equals(other.board[r][c]))
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null)
                    str.append('.');
                else if (board[r][c].isBlockingStone())
                    str.append('#');
                else if (board[r][c].isMovingStone())
                    str.append('@');
                else
                    str.append('*');
            }
            str.append('\n');
        }
        return str.toString().trim();
    }
}
