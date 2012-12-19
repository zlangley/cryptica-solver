import java.util.*;

public class Cryptica implements Comparable<Cryptica> {
    public static Cryptica parseBoard(String s) {
        Cryptica crypt = new Cryptica(5, 7);

        int[] srcR = new int[26];
        int[] srcC = new int[26];
        int[] dstR = new int[26];
        int[] dstC = new int[26];
        int templeStones = 0;

        Scanner scan = new Scanner(s);
        for (int r = 0; scan.hasNextLine(); r++) {
            String line = scan.nextLine();
            for (int c = 0; c < line.length(); c++) {
                char ch = line.charAt(c);
                if (ch == '@')
                    crypt.addMovingStone(r, c);
                else if (ch == '#')
                    crypt.addBlockingStone(r, c);
                else if (ch != '.') {
                    if (Character.isLowerCase(ch)) {
                        dstR[ch - 'a'] = r;
                        dstC[ch - 'a'] = c;
                    } else {
                        srcR[ch - 'A'] = r;
                        srcC[ch - 'A'] = c;
                        templeStones++;
                    }
                }
            }
        }

        for (int i = 0; i < templeStones; i++)
            crypt.addTempleStone(srcR[i], srcC[i], dstR[i], dstC[i]);

        return crypt;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String board = "";
        while (scan.hasNextLine()) {
            board += scan.nextLine() + "\n";
        }
        Cryptica crypt = parseBoard(board);
        printSolution(solve(crypt, args.length > 0));
    }

    public static Cryptica solve(Cryptica crypt, boolean aStar) {
        HashSet<Cryptica> seen = new HashSet<Cryptica>();
        Queue<Cryptica> q;
        if (aStar)
            q = new PriorityQueue<Cryptica>();
        else
            q = new LinkedList<Cryptica>();

        seen.add(crypt);
        q.add(crypt);
        while (q.size() > 0) {
            Cryptica c = q.poll();
            /*if (aStar) {
                System.out.println(c.distance());
                System.out.println(c.depth);
                System.out.println(c);
            }*/

            Cryptica right = c.moveRight();
            Cryptica left = c.moveLeft();
            Cryptica up = c.moveUp();
            Cryptica down = c.moveDown();

            if (!seen.contains(right)) {
                if (right.solved())
                    return right;
                q.add(right);
                seen.add(right);
            }
            if (!seen.contains(left)) {
                if (left.solved())
                    return left;
                q.add(left);
                seen.add(left);
            }
            if (!seen.contains(up)) {
                if (up.solved())
                    return up;
                q.add(up);
                seen.add(up);
            }
            if (!seen.contains(down)) {
                if (down.solved())
                    return down;
                q.add(down);
                seen.add(down);
            }
        }
        return null;
    }

    public static void printSolution(Cryptica c) {
        ArrayList<Direction> dirList = new ArrayList<Direction>();
        while (c.parent != null) {
            dirList.add(c.dir);
            c = c.parent;
        }

        for (int i = dirList.size() - 1; i >= 0; i--)
            System.out.println(dirList.get(i));
    }

    private Stone[][] board;
    private Cryptica parent;
    private Direction dir;
    private int depth;

    private enum Direction { LEFT, UP, RIGHT, DOWN };

    public Cryptica(int numRows, int numCols) {
        board = new Stone[numRows][numCols];
    }

    public Cryptica(Cryptica crypt) {
        parent = crypt;
        depth = parent.depth + 1;
        board = new Stone[crypt.board.length][crypt.board[0].length];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = crypt.board[r][c];
            }
        }
    }

/*    public int distance() {
        int dist = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] instanceof TempleStone) {
                    TempleStone t = (TempleStone)board[r][c];
                    dist = Math.max(dist, Math.abs(t.getDestRow() - r) + Math.abs(t.getDestCol() - c));
                }
            }
        }
        return dist + depth;
    }
    */

    public int distance() {
        int dist = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] instanceof TempleStone) {
                    Cryptica crypt = new Cryptica(this);
                    crypt.parent = null;
                    crypt.depth = 0;

                    for (int r1 = 0; r1 < board.length; r1++) {
                        for (int c1 = 0; c1 < board[r1].length; c1++) {
                            if ((r == r1 && c == c1) || !(crypt.board[r1][c1] instanceof TempleStone))
                                continue;
                            crypt.board[r1][c1] = new MovingStone();
                        }
                    }
                    Cryptica sol = solve(crypt, false);
                    dist = Math.max(dist, sol.depth);
                }
            }
        }
        return dist + depth;
 
    }

    public int compareTo(Cryptica c) {
        return distance() - c.distance();
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
        crypt.dir = Direction.RIGHT;
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
        crypt.dir = Direction.LEFT;
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
        crypt.dir = Direction.DOWN;
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
        crypt.dir = Direction.UP;
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
