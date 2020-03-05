import java.util.Arrays;
import java.util.Random;

/**
 * Miller-Rabin primality test.
 * It overcomes the problems of the simple pseudoprime test through two
 * modifications; it tries several base values of the base a instead of just 2,
 * and it looks for non-trivial square roots of 1 modulo n during the final set
 * of squarings while computing each modulo exponentiation. If it finds a 
 * non-trivial square root of 1 modulo n then it return composite.
 */
public class MillerRabin {
    /**
     * Checks if base-a is a wintness to n compositeness.
     * If [a^n-1 !=== 1 mod n] then n is composite by Fermat's theorem.
     * If it finds a non-trivial square root of n base a then n is a composite
     * as well. Otherwise n is probably a prime.
     * The number of witnesses to the compositeness of n if n is a composite is
     * at least <em>(n - 1) / 2</em>.
     *
     * @param a an integer in the multiplicative group modulo n
     * @param n the modulus.
     * @return if n is a prime or not
     */
    private static boolean witness(int a, int n) {
        int u = n - 1;
        int t = 0;
        while (u % 2 == 0) {
            u = u >> 1;
            t++;
        }
        System.out.println(String.format("a:%d, n:%d, u:%d, t:%d", a, n, u, t));
        int[] x = new int[t + 1];
        x[0] = ModularArithmetic.modularExponentiation(a, u, n);
        for (int i = 1; i <= t; i++) {
            x[i] = x[i - 1] * x[i - 1] % n;
            // x[i - 1] is a square root of x[i]
            if (x[i] == 1 && x[i - 1] != 1 && x[i - 1] != n - 1) {
                System.out.print("Found non-trivial square root: ");
                System.out.println(Arrays.toString(x));
                return true;
            }
        }
        System.out.print("x_t = a^((2^t)u) = a^(n-1) !=== 1 (mod n): ");
        System.out.println(Arrays.toString(x));
        if (x[t - 1] != 1) return true;
        return false;
    }

    /**
     * Miller-Rabin primality test.
     * Selects s random values of base a from {1,2,...n-1} and check for each
     * value of base-a if it is a witness to compositeness of n.
     * Running time is <em>O(sB)</em> arithmetic operations.
     * Running time is <em>O(sB^3)</em> bit operations.
     * s should be choses to be at least <em>lg(lnn - 1)</em> for better
     * accuracy. In most cases s = 50 should suffice.
     *
     * @param n the modulus
     * @param s number of randome bases to use in primality test
     * @return if n is a prime or not
     */
    public static boolean isPrime(int n, int s) {
        Random rand = new Random();
        for (int j = 1; j <= s; j++) {
            int a = rand.nextInt(n - 1) + 1;
            if (witness(a, n)) return false;
        }
        return true;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // Testing witness
        boolean composite = witness(7, 561);
        System.out.println("a is witness to n compositness => " + composite);
    }
}
