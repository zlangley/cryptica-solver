import java.util.*;

public class CrypticaSolver {
    private static enum Direction { LEFT, UP, RIGHT, DOWN };

    private static class Node implements Comparable<Node> {        
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
                new Node(state.moveRight(), this, Direction.RIGHT),
                new Node(state.moveLeft(), this, Direction.LEFT),
                new Node(state.moveUp(), this, Direction.UP),
                new Node(state.moveDown(), this, Direction.DOWN)
            };
        }
        
        @Override
        public int compareTo(Node n) {
            return 0;
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

    public static Node solve(Cryptica crypt, boolean aStar) {
        HashSet<Cryptica> seen = new HashSet<Cryptica>();
        Queue<Node> q;
        if (aStar)
            q = new PriorityQueue<Node>();
        else
            q = new LinkedList<Node>();

        seen.add(crypt);
        q.add(new Node(crypt));

        while (q.size() > 0) {
            Node node = q.poll();
            
            for (Node child : node.children()) {
                if (!seen.contains(child.state)) {
                    if (child.state.solved())
                        return child;
                    q.add(child);
                    seen.add(child.state);
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
        printSolution(solve(crypt, false));
    }
}