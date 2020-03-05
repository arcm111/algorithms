import java.util.Arrays;

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

    public static void KMP_Matcher(String T, String P) {
        int n = T.length();
        int m = P.length();
        int[] pi = computePrefexFunction(P);
        System.out.println("pi: " + Arrays.toString(pi));
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

    private static int[][] computeTransitionFunction(String P, char[] Sigma) {
        int m = P.length();
        int n = Sigma.length;
        int[][] delta = new int[m][n];
        for (int q = 0; q < m; q++) {
            for (int i = 0; i < n; i++) {
                char a = Sigma[i];
                int k = Math.min(m, q + 1);
                do {
                    k--;
                } while (!isSuffix(P, k, q, a));
                delta[q][i] = k;
            }
        }
        return delta;
    }

    private static boolean isSuffix(String P, int k, int q, char a) {
        if (k < 0 || q < 0 || k > q + 1) {
            throw new IllegalArgumentException("invalid arguments");
        }
        if (k == 0) return true;
        if (P.charAt(k--) != a) return false;
        while (k >= 0) {
            if (P.charAt(k--) != P.charAt(q--)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // Testing naive string matcher:
        String T = "acaabc";
        String P = "aab";
        naiveStringMatcher(T, P);
        T = "bacbababaabcbabababacabcaabcbaababacabcaababbc"; // shifts 15, 30
        P = "ababaca";
        KMP_Matcher(T, P);
        T = "aaaaaaaaaaaaaaaaa";
        P = "aaaaaaa";
        KMP_Matcher(T, P);

        String a = "whatevermayhappenswhatever";
        System.out.println(isSuffix(a, 7, 24, 'r'));
        System.out.println(isSuffix("a", 1, 0, 'a'));
        P = "ababaca";
        char[] s = {'a', 'b', 'c'};
        int[][] delta = computeTransitionFunction(P, s);
        for (int[] x : delta) System.out.println(Arrays.toString(x));
    }
}
