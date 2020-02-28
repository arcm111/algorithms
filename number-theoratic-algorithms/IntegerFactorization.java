import java.util.Random;

/**
 * Finds factors of a prime n.
 * Factoring a large integer n seems to be much more difficult than determining
 * whether n is a prime of a composite.
 */
public class IntegerFactorization {
    /**
     * Pollard's rho factorization algorithm.
     * The algorithm is only a heuristic so neither its running time nor its
     * success is guaranteed. However, the procedure is very effective in 
     * practice and it only uses a constant number of memory locations; 
     * space complexity is <em>O(1)</em>.
     * Expected worst running time is <em>O(n^(1/4))</em> arithmetic operations.
     * Expected worst running time is <em>O(2^(B/4)B^2)</em> bit operations.
     * Expected average running time is <em>O(sqrt(p))</em> arithmetic
     * operations where p is the smallest prime factor of n.
     *
     * @param n the integer to factorise
     */
    public static int pollardRho(int n) {
        Random rand = new Random();
        int i = 1;
        int x0 = rand.nextInt(n);
        int y = x0;
        int k = 2;
        int d = 1;
        while (d == 1) {
            i++;
            int x = (x0 * x0 - 1) % n;
            d = ModularArithmetic.gcd(y - x, n);
            if (d != 1 && d != n) {
                return d;
            }
            if (i == k) {
                y = x;
                k *= 2;
            }
            x0 = x;
        }
        return -1;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        int n = 1387;
        int p = pollardRho(n);
        System.out.print("Factors[" + n + "]: ");
        if (p == -1) System.out.println("no factors found!");
        else {
            System.out.println(p + ", " + n / p);
        }
    }
}
