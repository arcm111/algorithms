import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;
import java.util.Random;

/**
 * String matching problem is a problem of finding all occurences of a pattern
 * in a given text.
 * If a pattern occurs with a shift s in a text then it is a valid shift,
 * otherwise, the shift is invalid.
 */
public class StringMatching {
    /**
     * Brute force algorithm for finding all pattern occurences.
     * Tests all possible shifts in the text and check for valid ones.
     * Running time is <em>O((n - m + 1) m)</em> and requires no preprocessing.
     * @param T the string
     * @param P the pattern
     */
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

    /**
     * Rabin-Karp algorithm.
     * Uses hashing to find exact pattern occurences in a text.
     * Although its worst running time is no better than the naive method,
     * in practice the algorithm performs very well.
     * Preprocessing time is <em>Theta(m)</em>.
     * Worst-case running time is <em>O((n - m + 1) m)</em>.
     * Expected running time is <em>O(n + m)</em>.
     * @param T the string
     * @param P the pattern
     */
    public static void rabinKarpMatcher(String T, String P) {
        int d = 256; // size of the characters set
        // we choose q so that d*q fits within a computer word (32 bits)
        // d is 8 bits long so q should be 24 bits
        // preprocessing:
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
        // matching:
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

    /**
     * Finite automaton matching algorithm.
     * Uses a deterministic finite automaton DFA to scan a text for all 
     * occurences of a given pattern.
     * DFA is a finite state machine that accepts or rejects a string of 
     * symbols or charachters by running through a sequence of states 
     * uniquely determined by the pattern string. A valid shift is found when
     * the automaton reaches the final state.
     * Preprocessing time is <em>O(m|Sigma|)</em> where Sigma is the size of the
     * finite input alphabet.
     * Running time is <em>Theta(n)</em>.
     * @param T the string
     * @param P the pattern
     */
    public static void 
            finiteAutomatonMatcher(String T, String P, char[] Sigma) {
        HashMap<Character, Integer>[] delta = 
                computeTransitionFunction(P, Sigma);
        int n = T.length();
        int m = P.length();
        int q = 0;
        for (int i = 0; i < n; i++) {
            q = delta[q].get(T.charAt(i));
            if (q == m) {
                System.out.println("Pattern occurs with shift: " + (i - m + 1));
            }
        }
    }

    /**
     * Creates the transition function of a finite automaton from a pattern.
     * The automoaton has an initial state and a final state, starting from
     * the initial state it scans the next symbol in the text string and 
     * determines the next state using the transition function. Moving between
     * two states is called a transition.
     * Running time is <em>O(m|Sigma|)</em>.
     * @param P the pattern string
     * @param Sigma the set of input alphabet
     * @return the transition function as a matrix where each row represent
     * a pattern's character and each column represent the transition state
     * to another character in the finite input alphabet.
     */
    @SuppressWarnings("unchecked")
    private static HashMap<Character, Integer>[]
            computeTransitionFunction(String P, char[] Sigma) {
        int m = P.length();
        int n = Sigma.length;
        HashMap<Character, Integer>[] delta = 
                (HashMap<Character, Integer>[]) new HashMap[m + 1];
        for (int q = 0; q <= m; q++) {
            HashMap<Character, Integer> transition = new HashMap<>();
            for (int i = 0; i < n; i++) {
                char a = Sigma[i];
                int k = Math.min(m + 1, q + 2);
                do {
                    k--;
                } while (!isSuffix(P, k, q, a));
                transition.put(a, k);
            }
            delta[q] = transition;
        }
        return delta;
    }

    /**
     * Checks if a P[0..k-1] is a suffix of P[0...q-1] + [a].
     * Running time is <em>Theta(k)</em>.
     * @param P the pattern string
     * @param k the size of the k-Character prefix Pk
     * @param q the size of the q-Character prefix Pq
     * @param a the character added at the end of Pq
     * @return true if Pk is a suffix of Pq + [a], otherwise false
     */
    private static boolean isSuffix(String P, int k, int q, char a) {
        // 0 means Pk is an empty string which is a suffix of any other string
        if (k == 0) return true; 
        // if last Character of Pk is not 'a' then it's not a suffix of (Pq + a)
        if (P.charAt(k - 1) != a) return false;
        // compare rest of characters in P(k-1) and Pq
        for (int i = 1; i < k; i++) {
            if (P.charAt(k - i - 1) != P.charAt(q - i)) return false;
        }
        return true;
    }

    /**
     * Knuth-Morris-Pratt algorithm.
     * Similar to finite-automaton-matcher but avoids computing the transition
     * function altogether by using a prefix function which it computes in
     * linear time of pattern's length.
     * Preprocessing time is <em>Theta(m)</em>.
     * Running time is <em>Theta(n)</em>.
     * @param T the text string
     * @param P the pattern
     */
    public static void KMP_Matcher(String T, String P) {
        int n = T.length();
        int m = P.length();
        int[] pi = computePrefexFunction(P);
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

    /**
     * Computes the prefix function for KMP algorithm.
     * The prefix function contains information about how the pattern matches
     * against shifts of itself.
     * Note: unlike the pseudocode in the book, indexes need to start from -1 
     * instead of 0.
     * Running time is <em>Theta(m)</em> where m is the size of the pattern.
     * @param P the pattern string
     * @return the prefix function pi
     */
    private static int[] computePrefexFunction(String P) {
        int[] pi = new int[m];
        int m = P.length();
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

    /**
     * Computes the positive remainder of a modular operation.
     * In java, % operator computes the remainder rather than the modulo.
     * Java operator % can return negative values, we fix this by adding
     * one multiple of the modulus to the modulo and perform another modulo
     * operation to the sum.
     * @param a first operand
     * @param b second operand
     * @return the positive modulo
     */
    private static long mod(long a, long b) {
        return (a % b + b) % b;
    }

    /**
     * Checks if a pattern occurs at a certain shift.
     * @param T the text string
     * @param P the pattern
     * @param shift the shift position
     * @return true if the shift is valid, otherwise false
     */
    private static boolean areEqual(String T, String P, int shift) {
        for (int i = 0; i < P.length(); i++) {
            if (P.charAt(i) != T.charAt(i + shift)) return false;
        }
        return true;
    }

    /**
     * Generates a set of unique characters in a given text string.
     * Running time is <em>Theta(nlgn)</em> where n is the size of the text.
     * @param t the text string
     * @return the alphabet set
     */
    private static char[] getCharSet(String t) {
        Set<Character> set = new TreeSet<>();
        for (char a : t.toCharArray()) {
            set.add(Character.valueOf(a));
        }
        char[] res = new char[set.size()];
        int i = 0;
        for (Character c : set) res[i++] = c;
        return res;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // Testing Naive-String-Matcher:
        String T = "acaabc";
        String P = "aab";
        System.out.println("Naive: ");
        naiveStringMatcher(T, P);

        // Testing Rabin-Karp:
        T = "bacbababaabcbabababacabcaabcbaababacabcaababbc"; // shifts 15, 30
        P = "ababaca";
        System.out.println("Rabin-Karp: ");
        rabinKarpMatcher(T, P);

        // Testing Knuth-Morris-Pratt
        System.out.println("KMP: ");
        int[] pi = computePrefexFunction(P);
        System.out.println("Prefix function (pi): " + Arrays.toString(pi));
        KMP_Matcher(T, P);
        T = "aaaaaaaaaaaaaaaaa";
        P = "aaaaaaa";
        KMP_Matcher(T, P);

        // Testing Finite-Automaton-Matcher
        T = "bacbababaabcbabababacabcaabcbaababacabcaababbc"; // shifts 15, 30
        P = "ababaca";
        char[] s = getCharSet(T);
        HashMap<Character, Integer>[] delta = computeTransitionFunction(P, s);
        System.out.println("Finite-automaton-matcher: ");
        System.out.println("Transition function (delta): ");
        for (HashMap<Character, Integer> x : delta) {
            System.out.println(x.values());
        }
        finiteAutomatonMatcher(T, P, s);
    }
}
