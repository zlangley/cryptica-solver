import java.util.*;

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
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] instanceof TempleStone) {
                    TempleStone t = (TempleStone)board[r][c];
                    if (!t.hasDestination(r, c))
                        return false;
                }
            }
        }
        return true;
    }

    public void addTempleStone(int row, int col, int destRow, int destCol) { 
        board[row][col] = new TempleStone(destRow, destCol);
    }

    public void addMovingStone(int row, int col) {
        board[row][col] = new MovingStone();
    }

    public void addBlockingStone(int row, int col) {
        board[row][col] = new BlockingStone();
    }

    public Cryptica moveRight() {
        Cryptica crypt = new Cryptica(this);
        for (int r = 0; r < crypt.board.length; r++) {
            for (int c = crypt.board[r].length - 1; c > 0; c--) {
                if (crypt.board[r][c] == null && crypt.board[r][c-1] != null &&
                        !(crypt.board[r][c-1] instanceof BlockingStone)) {
                    crypt.board[r][c] = crypt.board[r][c-1];
                    crypt.board[r][c-1] = null;
                }
            }
        }
        return crypt;
    }

    public Cryptica moveLeft() {
        Cryptica crypt = new Cryptica(this);
        for (int r = 0; r < crypt.board.length; r++) {
            for (int c = 0; c < crypt.board[r].length - 1; c++) {
                if (crypt.board[r][c] == null && crypt.board[r][c+1] != null &&
                        !(crypt.board[r][c+1] instanceof BlockingStone)) {
                    crypt.board[r][c] = crypt.board[r][c+1];
                    crypt.board[r][c+1] = null;
                }
            }
        }
        return crypt;
    }

    public Cryptica moveDown() {
        Cryptica crypt = new Cryptica(this);
        for (int r = crypt.board.length - 1; r > 0; r--) {
            for (int c = 0; c < crypt.board[r].length; c++) {
                if (crypt.board[r][c] == null && crypt.board[r-1][c] != null &&
                        !(crypt.board[r-1][c] instanceof BlockingStone)) {
                    crypt.board[r][c] = crypt.board[r-1][c];
                    crypt.board[r-1][c] = null;
                }
            }
        }
        return crypt;
    }

    public Cryptica moveUp() {
        Cryptica crypt = new Cryptica(this);
        for (int r = 0; r < crypt.board.length - 1; r++) {
            for (int c = 0; c < crypt.board[r].length; c++) {
                if (crypt.board[r][c] == null && crypt.board[r+1][c] != null &&
                        !(crypt.board[r+1][c] instanceof BlockingStone)) {
                    crypt.board[r][c] = crypt.board[r+1][c];
                    crypt.board[r+1][c] = null;
                }
            }
        }
        return crypt;
    }

    @Override
    public int hashCode() {
        int ret = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                int val = 3*r + 17*c;
                if (board[r][c] instanceof BlockingStone)
                    val *= 2;
                else if (board[r][c] instanceof TempleStone)
                    val *= 3;
                else if (board[r][c] instanceof MovingStone)
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
                else if (board[r][c] instanceof BlockingStone)
                    str.append('#');
                else if (board[r][c] instanceof MovingStone)
                    str.append('@');
                else
                    str.append('*');
            }
            str.append('\n');
        }
        return str.toString().trim();
    }
}
