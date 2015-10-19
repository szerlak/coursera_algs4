public class PercolationStats {
    private double[] values;
    private double mean;
    private double stdDev;
    private int t;

    /**
     * perform T independent experiments on an N-by-N grid
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        t = T;

        values = new double[T];

        for (int i = 0; i < T; ++i) {
            int count = 0;
            Percolation p = new Percolation(N);
            boolean bPercolate = false;

            while (!bPercolate) {
                int pI = StdRandom.uniform(1, N + 1);
                int pJ = StdRandom.uniform(1, N + 1);
                if (!p.isOpen(pI, pJ)) {
                    p.open(pI, pJ);
                    ++count;
                    bPercolate = p.percolates();
                }
            }
            values[i] = (double) count / (double) (N * N);
        }

        mean = StdStats.mean(values);
        stdDev = StdStats.stddev(values);

    }

    /**
     * sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {
        return stdDev;
    }
    
    /**
     * low  endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLo() {
        double common = 1.96 * stdDev / Math.sqrt((double) t);
        return mean - common;
    }

    /**
     * high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHi() {
        double common = 1.96 * stdDev / Math.sqrt((double) t);
        return mean + common;
    }

    /**
     * test client
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2)
            throw new java.lang.IllegalArgumentException();

        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        

        PercolationStats ps = new PercolationStats(N, T);
        double mean = ps.mean();
        double stddev = ps.stddev();

        double confLow = ps.confidenceLo();
        double confHigh = ps.confidenceHi();

        StdOut.println("mean                    = " + Double.toString(mean));
        StdOut.println("stddev                  = " + Double.toString(stddev));
        StdOut.println("95% confidence interval = " + Double.toString(confLow)
                + ", " + Double.toString(confHigh));
    }
}
