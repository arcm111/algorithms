/**
 * Multi-threaded Fibonacci algorithm.
 * Multithreaded algorithms take advantage of multiple processors to 
 * speed up the running time.
 */
public class Fibonacci {
    /**
     * Fibonacci algorithm using dynamic Multithreaded.
     * Running time is <em>T_p >= T_1/p >= O(phi^n)/p</em> where <em>p</em>
     * is the number of processors.
     * The parallelism is <em>T_1/T_inf = O(phi^n)/O(n) = O(phi^n/n)</em> which
     * is exponential, meaning that we would never have enough processors to
     * acheive perfect-linear-speedup.
     * The speedup is {@code T_1/T_p <= T_1/(T_1/p) <= p} maximum p. However,
     * threads creation and destruction are slow processes, they are too slow
     * that the serial algorithm would become faster for larger values of n.
     *
     * @param n the nth Fibonacci number to compute
     * @return the nth Fibonacci number
     */
    public static int parallelCompute(int n) {
        if (n <= 1) return n;
        int x = parallelCompute(n - 1);
        FibRunnable runnable = new FibRunnable(n - 2);
        Thread t = new Thread(runnable);
        t.start();
        try {
            t.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int y = runnable.value;
        return x + y;
    }

    /**
     * Helper class for {@code parallelCompute} method.
     */
    private static class FibRunnable implements Runnable {
        private final int n;
        public int value;

        public FibRunnable(int n) {
            this.n = n;
        }

        public void run() {
            this.value = parallelCompute(n);
        }
    }

    /**
     * Serial Fibonacci algorithm.
     * Running time is <em>O(phi^n)</em> where <em>phi</em> is the golden ratio.
     * @param n the nth Fibonacci number to compute
     * @return the nth Fibonacci number
     */
    public static int serialCompute(int n) {
        if (n <= 1) return n;
        int x = serialCompute(n - 1);
        int y = serialCompute(n - 2);
        return x + y;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        int n = 20;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
            System.out.println("n: " + n);
        }
        long start = System.currentTimeMillis();
        int fib = Fibonacci.serialCompute(n);
        long end = System.currentTimeMillis();
        long runtime = end - start;
        System.out.println("F" + n + "(serial): " + fib);
        System.out.println("serial running time: " + ((end - start) / 1000.0));

        start = System.currentTimeMillis();
        fib = Fibonacci.parallelCompute(n);
        end = System.currentTimeMillis();
        runtime = end - start;
        System.out.println("F" + n + "(parallel): " + fib);
        System.out.println("parallel running time: " + runtime);
    }
}
