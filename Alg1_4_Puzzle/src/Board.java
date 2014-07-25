import java.util.Arrays;

public class Board {

    private int[][] blocks;
    private int size;

    /**
     * construct a board from an N-by-N array of blocks (where blocks[i][j] =
     * block in row i, column j)
     * 
     * @param blocks
     */
    public Board(int[][] blocks) {
        this.blocks = copyArray(blocks);
        size = blocks.length;
    }

    private int[][] copyArray(int[][] source) {
        int[][] target = new int[source.length][source.length];

        for (int i = 0; i < source.length; ++i) {
            for (int j = 0; j < source.length; ++j) {
                target[i][j] = source[i][j];
            }
        }
        return target;
    }

    /**
     * board dimension N
     * 
     * @return
     */
    public int dimension() {
        return size;
    }

    /**
     * number of blocks out of place
     * 
     * @return
     */
    public int hamming() {
        int exp = 1;
        int count = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] != exp++ && blocks[i][j] != 0)
                    count++;
            }
        }
        return count;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     * 
     * @return
     */
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                int value = blocks[i][j];
                if (value == 0)
                    continue;
                int goalCol = (value - 1) % size;
                int goalRow = (value - 1) / size;
                count += Math.abs(i - goalRow) + Math.abs(j - goalCol);
            }
        }
        return count;
    }

    /**
     * is this board the goal board?
     * 
     * @return
     */
    public boolean isGoal() {
        int exp = 1;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] != exp++ && (i != size - 1 || j != size - 1))
                    return false;
            }
        }
        return true;
    }

    private int[][] swap(int[][] source, int fromR, int fromC, int toR, int toC) {
        int[][] copy = copyArray(source);
        int value = copy[toR][toC];
        copy[toR][toC] = copy[fromR][fromC];
        copy[fromR][fromC] = value;
        return copy;
    }

    /**
     * a board obtained by exchanging two adjacent blocks in the same row
     * 
     * @return
     */
    public Board twin() {
        if (blocks[0][0] != 0 && blocks[0][1] != 0) {
            return new Board(swap(blocks, 0, 0, 0, 1));
        } else
            return new Board(swap(blocks, 1, 0, 1, 1));
    }

    /**
     * does this board equal y?
     */
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.blocks.length != that.blocks.length) return false;
        for (int row = 0; row < size; row++)
            if (!Arrays.equals(this.blocks[row], that.blocks[row]))
                return false;
        return true;

    }

    /**
     * all neighboring boards
     * 
     * @return
     */
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        
        int col = 0;
        int row = 0;
        boolean flag = false;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] == 0) {
                    row = i;
                    col = j;
                }
                if (flag) break;
            }
            if (flag) break;
        }
        
        if (row > 0)
            q.enqueue(new Board(swap(blocks, row, col, row - 1, col)));
        if (row < size - 1)
            q.enqueue(new Board(swap(blocks, row, col, row + 1, col)));
        if (col > 0)
            q.enqueue(new Board(swap(blocks, row, col, row, col - 1)));
        if (col < size - 1)
            q.enqueue(new Board(swap(blocks, row, col, row, col + 1)));
        
        return q;
    }

    /**
     * string representation of the board (in the output format specified below)
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(size + "\n");
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                str.append(blocks[i][j] + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}
