import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class ModularArithmetic {
    /**
     * Computes the greatest common divisor of two integegers.
     * Based on the mathematical fact that gcd(a,b) = gcd(b,a mod b).
     * Running time is <em>O(lgb)</em> arithmetic operations.
     * Running time is <em>O(B^3)</em> bit-operations.
     * It recurses less than k times if {@code b < F[k-1]} (F Fibonacci-number)
     * assuming that {@code a > b >= 1}. <em>"Lame's theorem"</em>.
     *
     * @param a the first integer
     * @param b the second integer
     * @return greatest common divisor of a and b
     */
    public static int euclid(int a, int b) {
        if (b == 0) return a;
        return euclid(b, a % b);
    }

    /**
     * Iterative variant of Euclid algorithm.
     *
     * @param a the first integer
     * @param b the second integer
     * @return greatest common divisor of a and b
     */
    public static int euclidIterative(int a, int b) {
        // fixes the issue with java modulo operator % returning negative 
        // results based on the fact that gcd(a, b) = gcd(|a|, |b|)
        if (a < 0) a = -a;
        if (b < 0) b = -b;

        while (b != 0) {
            int tmp = b;
            b = (a % b);
            a = tmp;
        }
        return a;
    }

    /**
     * Finds greatest common divisor using euclid algorithm.
     * Always returns a positive integer.
     *
     * @param a the first integer
     * @param b the second integer
     * @return greatest common divisor of a and b
     */
    public static int gcd(int a, int b) {
        return euclidIterative(a, b);
    }
    
    /**
     * Extended form of Euclid algorithm which returns gcd as the smallest
     * positive linear combination of a and b.
     *
     * @param a the first integer
     * @param b the second integer
     * @return d = gcd(a,b) and the coefficients of (d = ax + by) x and y
     */
    public static int[] extendedEuclid(int a, int b) {
        if (b == 0) return new int[] {a, 1, 0};
        int[] r = extendedEuclid(b, a % b);
        int d = r[0];
        int x_prime = r[1];
        int y_prime = r[2];
        int x = y_prime;
        int y = x_prime - (a / b) * y_prime;
        return new int[] {d, x, y};
    }

    /**
     * Computes Euler's phi-function of a multiplicative group modulo n.
     * phi-function of a multiplicative group modulo n is equal to its size.
     * phi(n) = n * product(1 - 1/p) for all distinct p prime-factors of n
     *
     * @param n the modulos of the mutiplicative group
     * @return phi(n)
     */
    public static int eulerPhi(int n) {
        Set<Integer> factors = primeFactors(n);
        int phi = n;
        // if n is prime phi(n) = n - 1
        if (factors == null) return n - 1; 
        for (int p : factors) phi = ((p - 1) * phi) / p;
        return phi;
    }

    /**
     * Finds all distinct prime-factors of a composite integer n.
     *
     * @param n the number to factorise
     * @return all distinct prime factors of n
     */
    public static Set<Integer> primeFactors(int n) {
        if (isPrime(n)) return null;
        Set<Integer> factors = new TreeSet<>();
        int num = n;
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n = n / i;
            }
        }
        return factors;
    }

    public static boolean isPrime(int n) {
        return trialDivision(n);
    }

    /**
     * Primality testing using trial division.
     * The smallest prime factor of n cannot be greater than sqrt(n), therefore
     * We divide by integers from 2 up to sqrt(n) until we find a factor.
     * Running time is O(sqrt(n)) = O(2^(B/2)) and B = ceil(log(n + 1))
     *
     * @param n the number to test its primality
     * @return if n is a prime or not
     */
    public static boolean trialDivision(int n) {
        if (n <= 1) return false; // "1" is not a prime
        if (n % 2 == 0) return n == 2; // even numbers are not prime except "2"
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Primality testing using pseudoprime algorithm.
     * The algorithm is deterministic for detecting composites, but probalistic
     * for detecting primes.
     * If it finds n to be a prime then it's probably is, if it turns out to be
     * a composite we call it a <em>base-2 pseudoprime</em>. If it finds that n 
     * is a composite, however, then it certainly is.
     * There is less than one in 10^20 chance that this algoithm finds a 
     * base-2 pseudoprime for 512-bits integers, and less than a one in 10^41
     * for 1024-bits integers.
     * Running time is same as modularExponentiation; <em>O(B)</em> arithmetic
     * operations and <em>O(B^3)</em> bits operations.
     *
     * @param n the number to test its primality
     * @return if n is a prime or not
     */
    public static boolean pseudoprime(int n) {
        if (n <= 1) return false;
        if (n % 2 == 0) return n == 2;
        if (modularExponentiation(2, n - 1, n) != 1) return false;
        return true;
    }

    /**
     * Solves modular linar equation [ax === b (mod) n] for x.
     * Such system is solvable only if d = gcd(a,n) divides b, in which case
     * the system has either d distinct solutions modulo n or none.
     * Solutions are given by [xi = x0 + i(n/d)] for i = {0,1,...,d-1} and
     * [x0 = x'(b/d) mod n] where x' is returned from extendedEuclid(a, n).
     *
     * @param a x's coefficient
     * @param b the remainder
     * @param n the modulus
     * @return values of x
     */
    public static int[] modularLinearEquationSolver(int a, int b, int n) {
        int[] r = extendedEuclid(a, n);
        int d = r[0];
        int x_prime = r[1];
        int y_prime = r[2];
        if (b > d && b % d == 0) { // d|b
            int[] x = new int[d];
            int x0 = (x_prime * (b / d)) % n;
            for (int i = 0; i < d; i++) {
                x[i] = (x0 + i * (n / d)) % n;
                if (x[i] < 0) {
                    x[i] = x[i] + n;
                }
            }
            return x;
        } else {
            return null;
        }
    }

    /**
     * Finds a correspondance between a system of equations modulo a set of 
     * pairwise relatively primes moduli and an equation modulo their product.
     *
     * @param a set of remainders
     * @param n set of prime moduli
     * @return correspondance a <-> (a1, a2, ..., ak) where k = |n|
     * @throws IllegalArgumentException if |a| != |n|
     * @throws IllegalArgumentException if noduli are not relatively prime
     */
    public static int chineseRemainder(int[] a, int[] n) {
        if (a.length != n.length) {
            throw new IllegalArgumentException("a and n are not compatible");
        }
        if (!pairwiseRelativelyPrime(n)) {
            String err = "n factors are not pairwise relatively prime";
            throw new IllegalArgumentException(err);
        }

        int N = 1;
        for (int i = 0; i < n.length; i++) N *= n[i];

        int[] m = new int[n.length];
        for (int i = 0; i < n.length; i++) {
            m[i] = N / n[i];
        }

        int[] c = new int[n.length];
        for (int i = 0; i < n.length; i++) {
            c[i] = m[i] * multiplicativeInverse(m[i], n[i]);
        }

        int x = 0;
        for (int i = 0; i < n.length; i++) {
            x += a[i] * c[i];
        }
        if (x % N < 0) return (x % N) + N;
        return x % N;
    }

    /**
     * Checks if two integers are relatively prime.
     * Running time is same as euclid <em>O(lgb)</em> arithmetic operations.
     * 
     * @param a first integer
     * @param b second integer
     * @return if a and b are relatively prime or not
     */
    public static boolean relativelyPrime(int a, int b) {
        return gcd(a, b) == 1;
    }

    /**
     * Checks if a set of integers are pairwise relatively prime.
     * Running time is <em>O(n lgb)</em> arithmetic operations.
     *
     * @param n set of integers
     * @return if each pairs in n are relatively prime
     */
    public static boolean pairwiseRelativelyPrime(int[] n) {
        for (int i = 0; i < n.length; i++) {
            for (int j = i + 1; j < n.length; j++) {
                if (!relativelyPrime(n[i], n[j])) return false;
            }
        }
        return true;
    }

    /**
     * Finds the multiplicative inverse of an integer a modulo n.
     * If a, n are relatively prime then the value x returned from 
     * extendedEuclid(a, n) is a^-1 the multiplicative-inverse of a.
     * Running time is same as extendedEuclid <em>O(lgn)</em> arithmetic 
     * operations.
     *
     * @param a integer we want to find its inverese
     * @param n the modulus
     * @return a^-1
     * @throws IllegalArgumentException if a and n are not relatively prime
     */
    public static int multiplicativeInverse(int a, int n) {
        if (!relativelyPrime(a, n)) {
            throw new IllegalArgumentException("a and n not relatively prime");
        }
        int r = extendedEuclid(a, n)[1];
        if (r < 0) return r + n;
        return r;
    }

    /**
     * Computes modular exponentiation of an integer a to the power b modulo n.
     * Shifting a number x one bit to the left doubles its decimal value 
     * if the last bit (added from left-sheft) is one, then x value becomes
     * double + 1 the original value.
     * ex: {@code 5 = 0b101, 5 << 1 = 0b1010 = 10, 0b1011 = 2 * 5 + 1 = 11}
     * Running time is <em>O(B)</em> arithmetic operations.
     * Running time is <em>O(B^3)</em> bit operations.
     *
     * @param a the base integer
     * @param b the power integer
     * @param n the modulus
     * @return the modular exponentiation
     */
    public static int modularExponentiation(int a, int b, int n) {
        int c = 0; // decimal value of most significat (k - i) bits of b
        int d = 1; // d = a^c mod n
        int k = (int) Math.floor(Math.log(b) / Math.log(2)) + 1;
        int[] bit = new int[k];
        for (int i = 0; i < k; i++) {
            bit[i] = (b >> i) & 1;
        }
        for (int i = k - 1; i >= 0; i--) {
            c *= 2;
            d = (d * d) % n; // left shift by one adding 0 as LSB
            if (bit[i] == 1) {
                c += 1;
                d = (d * a) % n; // left shift by one adding 1 as LSB
            }
            // String log = String.format("b:%d, c:%d, d:%d", bit[i], c, d);
            // System.out.println(log);
        }
        return d;
    }

    /**
     * Finds modular exponentiation of a^b mod n in reversed order of bits of b.
     *
     * @param a the base integer
     * @param b the power integer
     * @param n the modulus
     * @return the modular exponentiation
     */
    public static int modularExponentiationReverseOrder(int a, int b, int n) {
        int d = 1; // d = a^c mod n
        int k = (int) Math.floor(Math.log(b) / Math.log(2)) + 1;
        int[] bin = new int[k];
        for (int i = 0; i < k; i++) {
            bin[i] = b >> i & 1;
        }
        for (int i = 0; i < k; i++) {
            if (bin[i] == 1) {
                d = (d * a) % n;
            }
            a = (a * a) % n;
        }
        return d;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // Testing recursive euclid
        int gcd = euclid(30, 24);
        System.out.println("Euclid-recursive: gcd(30, 24) = " + gcd);

        // Testing iterative euclid
        gcd = euclidIterative(30, 24);
        System.out.println("Euclid-iterative: gcd(30, 24) = " + gcd);

        // Testing prime factorization
        for (int n : new int[] {126, 23, 4, 121, 255}) {
            Set<Integer> p = primeFactors(n);
            if (p == null) {
                System.out.println("Factors(" + n + "): " + n + " is a prime");
            } else {
                System.out.print("Factors(" + n + "): ");
                System.out.println(Arrays.toString(p.toArray(new Integer[0])));
            }
        }

        // Testing euler eulerPhi
        System.out.println("phi(126) = " + eulerPhi(126));
        System.out.println("Phi(45): " + eulerPhi(45));

        // Testing extendedEuclid
        int[] r = extendedEuclid(899, 493);
        System.out.print("Extended-Euclid(899,493): ");
        System.out.println(String.format("d:%d, x:%d, y:%d", r[0], r[1], r[2]));
        r = extendedEuclid(34, 21);
        System.out.print("Extended-Euclid(34,21): ");
        System.out.println(String.format("d:%d, x:%d, y:%d", r[0], r[1], r[2]));

        // Testing modularLinearEquationSolver:
        int[] solutions = modularLinearEquationSolver(14, 30, 100);
        System.out.println("Solving modular linear equation: 14x = 30 mod 100");
        if (solutions == null) {
            System.out.println("No solutions");
        } else {
            for (int i = 0; i < solutions.length; i++) {
                System.out.println("x" + i + ": " + solutions[i]);
            }
        }

        // Testing multiplicative-inverse
        System.out.print("Multiplicative inverse of 13 modulo 5: ");
        System.out.println(multiplicativeInverse(13, 5));

        // Testing chineseRemainder
        int[] n = {3, 5, 7};
        int[] a = {2, 3, 2};
        System.out.println("Testing Chinese remainder theorem:");
        for (int i = 0; i < n.length; i++) {
            System.out.println(String.format("x === %d (mod %d)", a[i], n[i]));
        }
        System.out.println("x = " + chineseRemainder(a, n));
        n = new int[] {5, 13};
        a = new int[] {2, 3};
        System.out.println("Testing Chinese remainder theorem:");
        for (int i = 0; i < n.length; i++) {
            System.out.println(String.format("x === %d (mod %d)", a[i], n[i]));
        }
        System.out.println("x = " + chineseRemainder(a, n));

        // Testing modular exponentiation
        int x = modularExponentiation(7, 560, 561);
        System.out.println("modular-exponentiation: 7^560 mod 561 = " + x);
        x = modularExponentiation(3, 8, 6);
        System.out.println("modular-exponentiation: 3^8 mod 6 = " + x);

        // modular exponentiation in LSB-to-MSB order of bits
        x = modularExponentiationReverseOrder(7, 560, 561);
        System.out.print("modular-exponentiation-reversed: ");
        System.out.println("7^560 mod 561 = " + x);

        // Testing pseudoprime
        System.out.println("Testing pseudoprime: ");
        int p1[] = {23, 71, 123, 101, 181};
        int p2[] = {341, 561, 645, 1105};
        for (int i = 0; i < p1.length; i++) {
            System.out.println(p1[i] + " is prime => " + pseudoprime(p1[i]));
        }
        System.out.print("Testing false positive on Charmichael numbers");
        System.out.println(" (composites): ");
        for (int i = 0; i < p2.length; i++) {
            Set<Integer> f = primeFactors(p2[i]);
            String s = Arrays.toString(f.toArray(new Integer[0]));
            boolean prime = pseudoprime(p2[i]);
            System.out.println(p2[i] + s + " is prime => " + prime);
        }
    }
}
