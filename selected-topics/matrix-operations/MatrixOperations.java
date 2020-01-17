/**
 * Matrix Operations.
 */
public class MatrixOperations {
    /**
     * Finds LU decomposition of a nonsingular(invertible) matrix.
     * Doesn't work on all invertible matrices if there exist a 0 pivot.
     * It returns L and U combined in one nxn matrix execluding L diagonal
     * wich consiste of 1's values because it's a unit lower triangular matrix.
     * Running time is <em>O(n^3)</em>.
     * @param input the input matrix
     * @return LU decomposition composite matrix.
     */
    public static double[][] LUDecompose(double[][] input) {
        int n = input.length;
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = input[i][j];
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = k + 1; i < n; i++) {
                A[i][k] = A[i][k] / A[k][k]; // v vector
                for (int j = k + 1; j < n; j++) {
                    A[i][j] = A[i][j] - A[i][k] * A[k][j]; // schur component
                }
            }
        }
        return A;
    }

    /**
     * Finds LUP decomposition of a nonsingular(invertible) matrix.
     * It converts A to L and U combined matrix and returns permutation matrix.
     * Works on all invertible matrices.
     * Running time is <em>O(n^3)</em>.
     * @param input the input matrix
     * @return P the permutation matrix
     * @throws IllegalArgumentException if the matrix is singular
     */
    public static int[] LUPDecompose(double[][] A) {
        int n = A.length;
        int[] pi = new int[n];
        for (int i = 0; i < n; i++) pi[i] = i;
        for (int k = 0; k < n; k++) {
            double p = 0;
            int m = k; // index of the row with largest non-zero |pivot|
            for (int i = k; i < n; i++) {
                if (Math.abs(A[i][k]) > p) {
                    p = Math.abs(A[i][k]);
                    m = i;
                }
            }
            if (p == 0) throw new IllegalArgumentException("singular matrix");
            // swap pi[k] and pi[m]
            int tmp = pi[k];
            pi[k] = pi[m];
            pi[m] = tmp;
            // swap k and m rows
            for (int i = 0; i < n; i++) {
                double t = A[k][i];
                A[k][i] = A[m][i];
                A[m][i] = t;
            }
            for (int i = k + 1; i < n; i++) {
                A[i][k] = A[i][k] / A[k][k]; // v vector
                for (int j = k + 1; j < n; j++) {
                    A[i][j] = A[i][j] - A[i][k] * A[k][j]; // schur component
                }
            }
        }
        return pi;
    }

    /**
     * Extracts the lower triangular matrix L from LU combined matrix.
     * @param LU composite
     * @return L unit lower triangular matrix
     */
    private static double[][] extract_L(double[][] LU) {
        int n = LU.length;
        double[][] L = new double[n][n];
        for (int i = 0; i < n; i++) L[i][i] = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                L[i][j] = LU[i][j];
            }
        }
        return L;
    }

    /**
     * Extracts the upper triangular matrix U from LU combined matrix.
     * @param LU composite
     * @return U lower triangular matrix
     */
    private static double[][] extract_U(double[][] LU) {
        int n = LU.length;
        double[][] U = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                U[i][j] = LU[i][j];
            }
        }
        return U;
    }

    /**
     * Prints a matrix of double values.
     */
    private static void printArr(double[][] a) {
        for (double[] row : a) {
            for (double x : row) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints an array of int values.
     */
    private static void printArr(int[] a) {
        for (double x : a) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // testing LUDecompose
        double[][] a = {{2, 3, 1, 5}, 
                        {6, 13, 5, 19}, 
                        {2, 19, 10, 23}, 
                        {4, 10, 11, 31}};
        double[][] LU = MatrixOperations.LUDecompose(a);
        System.out.println("A: ");
        printArr(a);
        System.out.println();
        System.out.println("LU composite: ");
        printArr(LU);
        System.out.println();
        System.out.println("L: ");
        printArr(extract_L(LU));
        System.out.println();
        System.out.println("U: ");
        printArr(extract_U(LU));
        System.out.println();

        // testing LUPDecompose
        double[][] b = {{2, 0, 2, 0.6}, 
                        {3, 3, 4, -2}, 
                        {5, 5, 4, 2}, 
                        {-1, -2, 3.4, -1}};
        System.out.println("A: ");
        printArr(b);
        System.out.println();
        int[] P = MatrixOperations.LUPDecompose(b);
        System.out.println("LUP composite: ");
        printArr(b);
        System.out.println();
        System.out.println("L: ");
        printArr(extract_L(b));
        System.out.println();
        System.out.println("U: ");
        printArr(extract_U(b));
        System.out.println();
        System.out.println("P: ");
        printArr(P);
    }
}
