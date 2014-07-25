public class Solver {

    private Node solved;

    private class Node implements Comparable<Node> {
        private final int priority;
        private final int moves;
        private final Board board;
        private final Node prev;

        public Node(Board board, Node node) {
            this.board = board;
            prev = node;
            if (prev == null)
                moves = 0;
            else
                moves = prev.moves + 1;
            priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(Node o) {
            return priority - o.priority;
        }

    }

    /**
     * find a solution to the initial board (using the A* algorithm)
     * 
     * @param initial
     */
    public Solver(Board initial) {
        if (initial.isGoal()) {
            solved = new Node(initial, null);
        } else {
            solved = solve(initial);
        }
    }

    private Node solve(Board initial) {
        Board twin = initial.twin();

        MinPQ<Node> mainQueue = new MinPQ<Node>();
        MinPQ<Node> twinQueue = new MinPQ<Node>();
        mainQueue.insert(new Node(initial, null));
        twinQueue.insert(new Node(twin, null));
        while (true) {
            Node prev = nextStep(mainQueue);
            if (prev.board.isGoal())
                return prev;
            if (nextStep(twinQueue).board.isGoal())
                return null;
        }
    }

    private Node nextStep(MinPQ<Node> queue) {
        Node min = queue.delMin();
        for (Board neighbor : min.board.neighbors()) {
            if (min.prev == null || !neighbor.equals(min.prev.board))
                queue.insert(new Node(neighbor, min));
        }
        return min;
    }

    /**
     * is the initial board solvable?
     * 
     * @return
     */
    public boolean isSolvable() {
        return solved != null;
    }

    /**
     * min number of moves to solve initial board; -1 if no solution
     * 
     * @return
     */
    public int moves() {
        if (!isSolvable())
            return -1;
        else
            return solved.moves;
    }

    /**
     * sequence of boards in a shortest solution; null if no solution
     * 
     * @return
     */
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        else {
            Stack<Board> sequence = new Stack<Board>();
            Node iter = solved;
            while (iter != null) {
                sequence.push(iter.board);
                iter = iter.prev;
            }

            return sequence;
        }
    }

    /**
     * solve a slider puzzle (given below)
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create initial board from file
        if (args.length == 0)
            return;
        In in = new In(args[0]);

        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
