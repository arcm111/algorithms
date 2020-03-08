import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;
import java.util.Random;

public class StringMatching {
    public static void naiveStringMatcher(String T, String P) {
        int n = T.length();
        int m = P.length();
        for (int s = 0; s <= n - m; s++) {
            boolean match = true;
            for (int i = 0; i < m; i++) {
                if (P.charAt(i) != T.charAt(s + i)) {
                    match = false;
                    break;
                }
            }
            if (match) System.out.println("Pattern occurs with shift " + s);
        }
    }

    public static void rabinKarpMatcher(String T, String P) {
        int d = 256;
        long q = BigInteger.probablePrime(24, new Random()).longValue();
        int n = T.length();
        int m = P.length();
        long h = 1;
        for (int i = 0; i < m - 1; i++) { // computes d^(m-1) % q
            h = mod(h * d, q);
        }
        long p = 0;
        long t = 0;
        for (int i = 0; i < m; i++) {
            p = mod(d * p + P.charAt(i), q);
            t = mod(d * t + T.charAt(i), q);
        }
        for (int s = 0; s < n - m; s++) {
            if (p == t) {
                if (areEqual(T, P, s)) {
                    System.out.println("Pattern occurs with shift: " + s);
                }
            }
            if (s < n - m - 1) {
                t = mod(d * (t - T.charAt(s) * h) + T.charAt(s + m), q);
            }
        }
    }

    private static long mod(long a, long b) {
        return (a % b + b) % b;
    }

    private static boolean areEqual(String T, String P, int shift) {
        for (int i = 0; i < P.length(); i++) {
            if (P.charAt(i) != T.charAt(i + shift)) return false;
        }
        return true;
    }

    public static void KMP_Matcher(String T, String P) {
        int n = T.length();
        int m = P.length();
        int[] pi = computePrefexFunction(P);
        System.out.println("Prefix function (pi): " + Arrays.toString(pi));
        int q = -1;
        for (int i = 0; i < n; i++) {
            while (q > 0 && P.charAt(q + 1) != T.charAt(i)) {
                q = pi[q];
            }
            if (P.charAt(q + 1) == T.charAt(i)) {
                q++;
            }
            if (q == m - 1) {
                System.out.println("Pattern occurs with shift " + (i - m + 1));
                q = pi[q];
            }
        }
    }

    private static int[] computePrefexFunction(String P) {
        int m = P.length();
        int[] pi = new int[m];
        pi[0] = -1;
        int k = -1;
        for (int q = 1; q < m; q++) {
            while (k > 0 && P.charAt(k + 1) != P.charAt(q)) {
                k = pi[k];
            }
            if (P.charAt(k + 1) == P.charAt(q)) {
                k++;
            }
            pi[q] = k;
        }
        return pi;
    }

    public static void finiteAutomatonMatcher(String T, String P) {
        char[] charset = getCharSet(T);
        int[][] delta = computeTransitionFunction(P, charset);
        Map<Character, Integer> indexes = generateHashMap(charset);
        int n = T.length();
        int m = P.length();
        int q = 0;
        for (int i = 0; i < n; i++) {
            q = delta[q][indexes.get(T.charAt(i))];
            if (q == m) {
                System.out.println("Pattern occurs with shift: " + (i - m + 1));
            }
        }
    }

    private static int[][] computeTransitionFunction(String P, char[] Sigma) {
        int m = P.length();
        int n = Sigma.length;
        int[][] delta = new int[m + 1][n];
        for (int q = 0; q <= m; q++) {
            for (int i = 0; i < n; i++) {
                char a = Sigma[i];
                int k = Math.min(m + 1, q + 2);
                do {
                    k--;
                } while (!isSuffix(P, k - 1, q - 1, a));
                delta[q][i] = k;
            }
        }
        return delta;
    }

    private static boolean isSuffix(String P, int k, int q, char a) {
        if (k == -1) return true;
        if (P.charAt(k--) != a) return false;
        while (k >= 0) {
            if (P.charAt(k--) != P.charAt(q--)) return false;
        }
        return true;
    }

    private static char[] getCharSet(String t) {
        Set<Character> set = new TreeSet<>();
        for (char a : t.toCharArray()) set.add(new Character(a));
        char[] res = new char[set.size()];
        int i = 0;
        for (Character c : set) res[i++] = c;
        return res;
    }

    private static Map<Character, Integer> generateHashMap(char[] sigma) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < sigma.length; i++) {
            map.put(sigma[i], i);
        }
        return map;
    }

    public static void main(String[] args) {
        // Testing naive string matcher:
        String T = "acaabc";
        String P = "aab";
        System.out.println("Naive: ");
        naiveStringMatcher(T, P);
        T = "bacbababaabcbabababacabcaabcbaababacabcaababbc"; // shifts 15, 30
        P = "ababaca";
        System.out.println("Rabin-Karp: ");
        rabinKarpMatcher(T, P);
        System.out.println("KMP: ");
        KMP_Matcher(T, P);
        T = "aaaaaaaaaaaaaaaaa";
        P = "aaaaaaa";
        KMP_Matcher(T, P);

        T = "bacbababaabcbabababacabcaabcbaababacabcaababbc"; // shifts 15, 30
        P = "ababaca";
        char[] s = {'a', 'b', 'c'};
        int[][] delta = computeTransitionFunction(P, s);
        System.out.println("Finite-automaton-matcher: ");
        System.out.println("Transition function (delta): ");
        for (int[] x : delta) System.out.println(Arrays.toString(x));
        finiteAutomatonMatcher(T, P);
    }
}
