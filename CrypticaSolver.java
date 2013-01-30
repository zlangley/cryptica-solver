import java.util.*;

public class CrypticaSolver {
    private static enum Direction { LEFT, UP, RIGHT, DOWN };

    private static class Node {        
        public int depth;
        public Node parent;
        public Direction dir;
        public Cryptica state;
        
        public Node(Cryptica state) {
            this.state = state;
        }
        
        public Node(Cryptica state, Node parent, Direction dir) {
            this.state = state;
            this.parent = parent;
            this.dir = dir;
            this.depth = parent.depth + 1;
        }
        
        public Node[] children() {
            return new Node[] {
                new Node(new Cryptica(state).moveRight(), this, Direction.RIGHT),
                new Node(new Cryptica(state).moveLeft(), this, Direction.LEFT),
                new Node(new Cryptica(state).moveUp(), this, Direction.UP),
                new Node(new Cryptica(state).moveDown(), this, Direction.DOWN)
            };
        }
    }  
  
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

    public static Node solve(Cryptica crypt) {
        HashSet<Integer> seen = new HashSet<Integer>();
        Queue<Node> q = new LinkedList<Node>();

        seen.add(crypt.hashCode());
        q.add(new Node(crypt));

        while (q.size() > 0) {
            Node node = q.poll();
            for (Node child : node.children()) {
                int hash = child.state.hashCode();
                if (!seen.contains(hash)) {
                    if (child.state.solved())
                        return child;

                    q.add(child);
                    seen.add(hash);
                }
            }
        }
        return null;
    }

    public static void printSolution(Node node) {
        ArrayList<Direction> dirList = new ArrayList<Direction>();
        while (node.parent != null) {
            dirList.add(node.dir);
            node = node.parent;
        }

        for (int i = dirList.size() - 1; i >= 0; i--)
            System.out.println(dirList.get(i));
    }
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        StringBuilder board = new StringBuilder();
        while (scan.hasNextLine()) {
            board.append(scan.nextLine());
            board.append('\n');
        }
        Cryptica crypt = parseBoard(board.toString());
        printSolution(solve(crypt));
    }
}
