import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int gridSize;
    private boolean[] grid;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF fullUF;

    /**
     * create N-by-N grid, with all sites blocked
     * 
     * @param N
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        gridSize = N;
        grid = new boolean[N * N];
        uf = new WeightedQuickUnionUF((N * N) + 2);
        fullUF = new WeightedQuickUnionUF((N * N) + 1);

        grid[0] = true;

        for (int i = 0; i < gridSize * gridSize; ++i) {
            grid[i] = false;
        }

    }

    /**
     * open site (row i, column j) if it is not open already
     * 
     * @param i
     * @param j
     */
    public void open(int i, int j) {
        if (i < 1 || j < 1 || i > gridSize || j > gridSize) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        final int X = i - 1;
        final int Y = j - 1;
        final int XY = gridXY(X, Y);

        if (grid[XY - 1])
            return;
        grid[XY - 1] = true;

        if (X == 0) { // first
            uf.union(0, XY);
            fullUF.union(0, XY);
        }
        if (X == gridSize - 1) { // last
            uf.union(XY, gridSize * gridSize + 1);
        }

        union(XY, X, Y - 1);
        union(XY, X, Y + 1);
        union(XY, X - 1, Y);
        union(XY, X + 1, Y);

    }

    /**
     * is site (row i, column j) open?
     * 
     * @param i
     * @param j
     * @return
     */
    public boolean isOpen(int i, int j) {
        if (i < 1 || j < 1 || i > gridSize || j > gridSize) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        return (grid[gridXY(i - 1, j - 1) - 1]);
    }

    /**
     * is site (row i, column j) full?
     * 
     * @param i
     * @param j
     * @return
     */
    public boolean isFull(int i, int j) {
        if (i < 1 || j < 1 || i > gridSize || j > gridSize) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        if (isOpen(i, j)) {
            return fullUF.connected(0, gridXY(i - 1, j - 1));
        }
        return false;

    }

    /**
     * does the system percolate?
     * 
     * @return
     */
    public boolean percolates() {
        if (gridSize == 1) {
            return (grid[0]);
        }

        return uf.connected(0, gridSize * gridSize + 1);
    }

    /**
     * test client (optional)
     * 
     * @param args
     */
    public static void main(String[] args) {

    }

    private void union(final int xy, final int X, final int Y) {
        if (X >= 0 && Y >= 0 && X < gridSize && Y < gridSize) {
            if (isOpen(X + 1, Y + 1)) {
                int ufIndex = X * gridSize + Y + 1;
                uf.union(xy, ufIndex);
                fullUF.union(xy, ufIndex);
            }
        }
    }

    private int gridXY(int x, int y) {
        return x * gridSize + y + 1;
    }
}
