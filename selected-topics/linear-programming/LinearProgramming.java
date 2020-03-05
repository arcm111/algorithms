import java.util.Arrays;

/**
 * Linear programming is a mathematical technique for maximizing or 
 * minimizing a linear function for n variables subject to m constraints.
 */
public class LinearProgramming {
    /**
     * The simplex algorithm.
     * Takes as an input a linear system in standard form and maximizes its
     * objective function.
     * Running time is exponential in worst case <em>O(2^n)</em>, however,
     * the algorithm runs much faster in practice and typically in polynomial
     * time.
     *
     * @param ls linear system matrix of n variables and m constraints
     * @param bs the basic solution to the linear system
     * @param of the objective function
     * @return optimal solution to the linear system
     * @throws IllegalArgumentException if system is unbounded
     */
    public static double[] simplex(double[][] ls, double[] bs, double[] of) {
        int m = ls.length; // matrix rows
        int n = ls[0].length; // matrix columns
        // make the argumets immutable by creating independent copies
        double[][] A = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = ls[i][j];
            }
        }
        double[] b = Arrays.copyOf(bs, bs.length);
        double[] c = Arrays.copyOf(of, of.length);

        // construct basic and nonbasic variables sets
        int[] N = new int[n];
        int[] B = new int[m];
        for (int i = 0; i < n; i++) N[i] = i + 1;
        for (int i = 0; i < m; i++) B[i] = n + i + 1;

        // make sure the system is feasible and produce a slack form for
        // which the basic solution is feasible
        double v = initialiseSimplex(A, b, c, N, B);

        // keep pivoting until an optimal solution is found
        while (true) {
            // choose a nonbasic variable index with a positive coefficient
            int e = bland(c, N);
            if (e == -1) {
                // if all coefficients in objective fuction are negative
                // then we found an optimal solution
                break; 
            }
            int eInd = index(N, e);
            double[] delta = new double[m];
            for (int i = 0; i < m; i++) {
                if (A[i][eInd] > 0) {
                    delta[i] = b[i] / A[i][eInd];
                } else {
                    delta[i] = Double.POSITIVE_INFINITY;
                }
            }
            // find the index which minimizes delta[l]
            int l = minimumRatioTest(delta, B);
            int lInd = index(B, l);
            if (Double.isInfinite(delta[lInd])) {
                // there is no limit on the amount by which we can increase
                // the value of the entering variable e. This means that the
                // linear system has some feasible solutions but does not have
                // a finite objective value, therefore, it is unbounded
                throw new IllegalArgumentException("Unbounded");
            }
            v = pivot(N, B, A, b, c, v, l, e);
        }

        // construct and return the optimal solution
        double[] x = new double[n + m];
        for (int i = 1; i <= n + m; i++) {
            if (inArray(N, i)) x[i - 1] = 0;
            else x[i - 1] = b[index(B, i)];
        }
        return x;
    }

    /**
     * Test whether the linear program is feasible and construct a slack
     * form with a feasible basic solution.
     * <ul>First we check if the linear system already has a feasible basic
     * solution (all coefficients in the basic solution are positive) in which
     * case we don't do anything.
     * <ul>Second we check if the linear system is feasible, we do that by 
     * constructing an auxiliary linear system from it by adding an extra
     * nonbasic variable and check if the optimal solution sets x0 to 0, if it
     * does then the linear system is feasible, otherwise, it's infeasible.
     * <ul>If in the final auxiliary slack form x0 is still a basic variable,
     * we have a degenerate slack form and we need to pivot one more time with
     * x0 as the entering variable to get rid of this degeneracy.
     * <ul>Lastly, if the system turns out to be feasible we return the final 
     * slack form of the auxiliary system after removing x0 and reconstructing
     * the original objective function by replacing basic variables with right
     * hand side of associated constraint.
     *
     * @param A the linear system matrix
     * @param b the basic solution
     * @param c the objective function
     * @param N the nonbasic variables set
     * @param B the basic variables set
     * @return v the objective value of the auxiliary system
     * @throws IllegalArgumentException if linear system is infeasible
     * @throws IllegalArgumentException if auxiliary system is unbounded
     */
    private static double initialiseSimplex(double[][] A, double[] b, 
            double[] c, int[] N, int[] B) {
        int m = A.length; // matrix rows
        int n = A[0].length; // matrix columns
        double v = 0; // objective function value

        // find index k of the minimum value in b
        int k = 0;
        for (int i = 1; i < m; i++) {
            if (b[i] < b[k]) k = i;
        }

        // if the initial basic solution is feasible we are done
        if (b[k] >= 0) {
            // N and B are already passed through method arguments
            return v;
        }

        // creating the auxiliary linear program system Laux
        double[][] Laux = new double[m][n + 1];
        for (int i = 0; i < m; i++) {
            // add -x0 at the end of each constraint
            Laux[i][n] = -1; 
            // copy the rest of the coefficients from A
            for (int j = 0; j < n; j++) {
                Laux[i][j] = A[i][j];
            }
        }
        // copy original nonbasic variables and add x0 at the end
        int[] Naux = Arrays.copyOf(N, N.length + 1); // appends 0 at n
        int[] Baux = Arrays.copyOf(B, B.length);
        double[] baux = Arrays.copyOf(b, b.length);
        // set all coefficients in the objective function of the auxiliary 
        // linear program to 0 except the one for x0 which we set to -1
        double[] caux = new double[n + 1];
        caux[n] = -1; 
        int l = Baux[k]; // k + n won't work because we index from 0 not 1
        int e = 0;
        double vaux = 0;

        // pivot once to make the basic solution to Laux feasible
        vaux = pivot(Naux, Baux, Laux, baux, caux, vaux, l, e);

        // find optimal solution to Laux
        // same as in simplex method we pivot until we find an optimal solution
        while (true) {
            e = bland(caux, Naux);
            if (e == -1) {
                break; 
            }
            int eInd = index(Naux, e);
            double[] delta = new double[m];
            for (int i = 0; i < m; i++) {
                if (Laux[i][eInd] > 0) {
                    delta[i] = baux[i] / Laux[i][eInd];
                } else {
                    delta[i] = Double.POSITIVE_INFINITY;
                }
            }
            l = minimumRatioTest(delta, Baux);
            int lInd = index(Baux, l);
            if (Double.isInfinite(delta[lInd])) {
                throw new IllegalArgumentException("Unbounded");
            }
            vaux = pivot(Naux, Baux, Laux, baux, caux, vaux, l, e);
        }
        
        // determin if the original linear program is infeasible or unbounded
        // if feasible then return the modified final slack form of Laux
        if (inArray(Naux, 0) || b[index(Baux, 0)] == 0) {
            // here the optimal solution to Laux sets x0 to 0, the linear 
            // system is therfore feasible

            // check for degeneracy and fix it if it exists
            if (inArray(Baux, 0)) {
                // x0 is a basic variable and we need to perform one more
                // degenerate pivot to turn it into a nonbasic variable
                l = 0; // exiting variable is x0
                // find any entering variable with a non-zero coefficient
                for (int j = 0; j < n + 1; j++) {
                    if (Laux[l][j] != 0) {
                        e = index(Naux, j);
                        break;
                    }
                }
                // perform one (degenerate) pivot to make x0 nonbasic
                vaux = pivot(Naux, Baux, Laux, baux, caux, vaux, l, e);
            }

            // update original linear system with the final auxiliary slack form
            // temporary copy of original objective function
            double[] fn = Arrays.copyOf(c, c.length);
            // temporary copy of original nonbasic variables
            int[] vars = Arrays.copyOf(N, N.length);
            // copy final slack form of auxiliary linear system into the
            // original one after removing x0 variable from Laux and Naux
            for (int i = 0; i < m; i++) {
                B[i] = Baux[i];
                b[i] = baux[i];
                int q = 0;
                for (int j = 0; j < n + 1; j++) {
                    if (Naux[j] == 0) continue;
                    N[q] = Naux[j];
                    A[i][q] = Laux[i][j];
                    q++;
                }
            }
            // restore original objective function 
            Arrays.fill(c, 0); // reset objective function
            // copy coefficients of nonbasic variables in A's objective function
            for (int j = 0; j < n; j++) {
                // a nonbasic variable in original objective function
                int cj = vars[j];
                // if it still is a nonbasic variable in Laux then copy it
                if (inArray(N, cj)) {
                    // find c index in new nonbasic variables set
                    int cInd = index(N, cj);
                    // copy original coefficient of c to new objective function
                    c[cInd] = fn[j];
                }
            }
            // replace basic variables with rhs of associated constraint
            for (int j = 0; j < n; j++) {
                int cj = vars[j];
                if (inArray(Baux, cj)) {
                    // c is a basic variable
                    int cInd = index(Baux, cj);
                    for (int q = 0; q < n; q++) {
                        c[q] = c[q] - fn[j] * A[cInd][q];
                    }
                    // update objective function value v
                    v = v + fn[j] * b[cInd];
                }
            }
            return v;
        }
        // Linear system is infeasible because optimal solution to auxiliary
        // linear system does not set x0 value = 0
        throw new IllegalArgumentException("infeasible");
    }

    /**
     * Pivoting procedure.
     * Takes a slack form as an input as well as an entering variable (enteres
     * the basic variables set) and a leaving variable (leaves basic variables
     * set) and returns another slack form with a greater objective value.
     * We increase the value of the entering variable as much as possible
     * without violating any of the constraints.
     *
     * @param N the nonbasic variables set
     * @param B the basic variables set
     * @param A the linear system matrix
     * @param b the basic solution
     * @param c the objective function
     * @param e the entering variable (as 1 in x1 and not its index in N)
     * @param l the leaving variable (as 2 in x2 and not its index in B)
     * @param v the objective value of the input slack form
     * @return the objective value of the output slack form
     */
    private static double pivot(int[] N, int[] B, double[][] A, double[] b, 
            double[] c, double v, int l, int e) {
        // find indexes of leaving and entering variables in B and N 
        // respectively
        int lInd = index(B, l);
        int eInd = index(N, e);

        int m = A.length; // matrix rows
        int n = A[0].length; // matrix columns
        
        // compute the coefficients of the leaving basic variable constraint
        b[lInd] = b[lInd] / A[lInd][eInd];
        for (int j = 0; j < n; j++) {
            if (j == eInd) continue;
            A[lInd][j] = A[lInd][j] / A[lInd][eInd];
        }
        A[lInd][eInd] = 1 / A[lInd][eInd];

        // compute the coefficients of the remaining constraints
        for (int i = 0; i < m; i++) {
            if (i == lInd) continue;
            b[i] = b[i] - A[i][eInd] * b[lInd];
            for (int j = 0; j < n; j++) {
                if (j == eInd) continue;
                A[i][j] = A[i][j] - A[i][eInd] * A[lInd][j];
            }
            A[i][eInd] = -1 * (A[i][eInd] * A[lInd][eInd]);
        }

        // compute the objective function
        v = v + c[eInd] * b[lInd];
        for (int j = 0; j < n; j++) {
            if (j == eInd) continue;
            c[j] = c[j] - c[eInd] * A[lInd][j];
        }
        c[eInd] = -1 * (c[eInd] * A[lInd][eInd]);

        // compute new sets of basic and nonbasic variables
        N[eInd] = l;
        B[lInd] = e;

        return v;
    }

    /**
     * Bland's method for determining the entering variable.
     * It finds the smallest indexed nonbasic variable with a positive 
     * coefficient in the objective function as the entering variable.
     * <p>Bland's rule is used to prevent cycling while running simplex which
     * is the only possible reason the algorithm might not terminate.
     *
     * @param c the objective function
     * @param N nonbasic variables set
     * @return non-cycling entering variable
     */
    private static int bland(double[] c, int[] N) {
        int e = -1;
        int n = c.length;
        for (int i = 0; i < n; i++) {
            if (c[i] > 0) {
                if (e == -1 || N[i] < e) e = N[i];
            }
        }
        return e;
    }

    /**
     * Minimum Ratio test is used for determining the leaving variable.
     * It finds a basic variable with the lowest b[l]/A[l][e] ratio and 
     * breaks ties by choosing the variable with the smallest index.
     * <p>Minimum ratio test is used to prevent cycling while running simplex
     * which is the only possible reason the algorithm might not terminate.
     *
     * @param c the objective function
     * @param N nonbasic variables set
     * @return non-cycling entering variable
     */
    private static int minimumRatioTest(double[] delta, int[] B) {
        int m = B.length;
        int l = B[0];
        double dl = delta[0];
        for (int i = 1; i < m; i++) {
            if (delta[i] < dl) {
                dl = delta[i];
                l = B[i];
            } else if (delta[i] == dl && B[i] < l) {
                l = B[i];
            }
        }
        return l;
    }

    private static int index(int[] a, int x) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == x) return i;
        }
        throw new IllegalArgumentException(x + " not in " + Arrays.toString(a));
    }

    private static boolean inArray(int[] a, int x) {
        for (int y : a) if (y == x) return true;
        return false;
    }

    private static void printArray(String label, int[] a) {
        System.out.println(label + ": ");
        System.out.println(Arrays.toString(a));
    }

    private static void printArray(String label, double[] a) {
        System.out.println(label + ": ");
        System.out.println(Arrays.toString(a));
    }

    private static void printArray(String label, double[][] a) {
        System.out.println(label + ": ");
        for (double[] b : a) {
            System.out.println(Arrays.toString(b));
        }
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // testing pivot method
        double[][] A = {{1, 1, 3}, {2, 2, 5}, {4, 1, 2}};
        int[] N = {1, 2, 3};
        int[] B = {4, 5, 6};
        double[] b = {30, 24, 36};
        double[] c = {3, 1, 2};
        double v = 0;
        int[] l = {6, 5, 3};
        int[] e = {1, 3, 2};
        for (int i = 0; i < 3; i++) {
            System.out.println("l = " + l[i] + ", e = " + e[i]);
            v = pivot(N, B, A, b, c, v, l[i], e[i]);
            printArray("A", A);
            printArray("N", N);
            printArray("B", B);
            printArray("b", b);
            printArray("c", c);
            System.out.println("v: " + v);
            System.out.println("--------------------\n");
        }

        // testing initialiseSimplex method
        A = new double[][] {{2, -1}, {1, -5}};
        N = new int[] {1, 2};
        B = new int[] {3, 4};
        b = new double[] {2, -4};
        c = new double[] {2, -1};
        v = initialiseSimplex(A, b, c, N, B);
        printArray("A-initialised", A);
        printArray("N", N);
        printArray("B", B);
        printArray("b", b);
        printArray("c", c);
        System.out.println("v: " + v);
        System.out.println("--------------------\n");

        // testing simplex method
        A = new double[][] {{1, 1, 3}, {2, 2, 5}, {4, 1, 2}};
        b = new double[] {30, 24, 36};
        c = new double[] {3, 1, 2};
        double[] x = LinearProgramming.simplex(A, b, c);
        printArray("solution", x);
        v = 0;
        for (int i = 0; i < A.length; i++) v += c[i] * x[i];
        System.out.println("v: " + v);
        System.out.println("--------------------\n");

        // simplex test 2
        A = new double[][] {{-2, -7.5, -3}, {-20, -5, -10}};
        b = new double[] {-10000, -30000};
        c = new double[] {-1, -1, -1};
        x = LinearProgramming.simplex(A, b, c);
        printArray("solution", x);
        v = 0;
        for (int i = 0; i < A.length; i++) v += c[i] * x[i];
        System.out.println("v: " + v);
        System.out.println("--------------------\n");

        // simplex test 3 "basic solution is not feasible"
        A = new double[][] {{1, -1}, {-1, -1}, {-1, 4}};
        b = new double[] {8, -3, 2};
        c = new double[] {1, 3, 0};
        x = LinearProgramming.simplex(A, b, c);
        printArray("solution", x);
        v = 0;
        for (int i = 0; i < A.length; i++) v += c[i] * x[i];
        System.out.println("v: " + v);
        System.out.println("--------------------\n");

        // simplex test 4 "infeasible"
        A = new double[][] {{1, 2}, {-2, -6}, {0, 1}};
        b = new double[] {4, -12, 1};
        c = new double[] {1, -2};
        try {
            x = LinearProgramming.simplex(A, b, c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("--------------------\n");

        // simplex test 5 "unbounded"
        A = new double[][] {{-1, 1}, {-1, -1}, {-1, 4}};
        b = new double[] {-1, -3, 2};
        c = new double[] {1, 3};
        try {
            x = LinearProgramming.simplex(A, b, c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("--------------------\n");
    }
}
