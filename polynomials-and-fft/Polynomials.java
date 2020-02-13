/**
 * A <b>polynomial</b> in the variable x over an algebric feild F represents a 
 * function {@code A(x) = sum_j=0^n-1 ajx^j} where {@code a0, a1, ..., an-1}
 * are the <b>coefficients</b> of the polynomail, and the polynomial has a 
 * <b>degree</b> of {@code n - 1} and a <b>bound-degree</b>.
 * Polynomials can be represented mainly in two forms: <b>coefficients form</b>
 * and <b>point-value</b> form.
 * In coefficients form, ddition and evaluation(Horner's rule) takes 
 * <em>THETA(n)</em> and multiplication takes <em>THETA(n^2)</em>. While in
 * point-value form, addition and multiplication takes <em>THETA(n)</em> and
 * <b>interpolation</b>(inverse of evaluation) takes <em>THETA(n^2)</em> using
 * lagrange's formula.
 * Evaluating and interpolating polynomials takes THETA(n^2) using normal 
 * methods, however, if we use comple roots of unity we can perform these 
 * operations in <em>THETA(nlgn)</em> time using the fast Fourier transform 
 * FFT algorithm.
 */
public class Polynomials {
    /**
     * Fast Fourier Transform converts a polynomial from its coefficient 
     * representation into its point-value form using divide-and-conquer. 
     * It evaluates a polynomial of degree n at n distinct points. The n 
     * distinct points are the n-th roots of unity.
     * It exploits unique properties of complex nth roots of unity(cacellation, 
     * halving, and summation) to evaluate or interpolate a polynomial in
     * <em>THETA(nlgn)</em> time.
     * 
     * @param a the polynomial coefficients
     * @return point-value form at the n complex nth roots of unity
     * @throws IllegalArgumentException if n is not a power of 2
     */
    public static ComplexNumber[] FFT_recursive(double[] a) {
        int n = a.length;

        if (n == 1) {
            return new ComplexNumber[] {new ComplexNumber(a[0], 0)};
        }

        if (n % 2 != 0) {
            throw new IllegalArgumentException("n must be a power of 2!");
        }

        ComplexNumber wn = new ComplexNumber(2 * Math.PI / n);
        ComplexNumber w = new ComplexNumber(1, 0);
        double[] a0 = new double[n/2];
        double[] a1 = new double[n/2];
        for (int i = 0; i < n / 2; i++) {
            a0[i] = a[2 * i];
            a1[i] = a[2 * i + 1];
        }
        ComplexNumber[] y0 = FFT_recursive(a0);
        ComplexNumber[] y1 = FFT_recursive(a1);
        ComplexNumber[] y = new ComplexNumber[n];
        for (int k = 0; k < n / 2; k++) {
            ComplexNumber t = w.times(y1[k]); // twiddle factor
            y[k] = y0[k].plus(t);
            y[k + (n/2)] = y0[k].minus(t);
            w = w.times(wn);
        }
        return y;
    }

    /**
     * Converts a polynomial from point-value form to coefficients 
     * representation using inverse FFT algorithm.
     * Runs in <em>THETA(nlgn)</em>.
     *
     * @param y the y components vector of the n point-value points evaluated
     *			at the n complex nth roots of unity
     * @return the coefficients vector of the polynomial
     * @throws IllegalArgumentException if n is not a power of 2
     */
    public static ComplexNumber[] FFT_inverse_recursive(ComplexNumber[] y) {
        ComplexNumber[] a = ifft(y);
        double n = a.length;
        // divide by n to produce the original values of the coefficients
        for (int k = 0; k < n; k++) a[k] = a[k].divideBy(n);
        return a;
    }

    /**
     * Computes the polynomial coefficients from its n point-value points.
     * The results are scalled by n.
     * Runs in <em>THETA(nlgn)</em> time.
     *
     * @param y the y components vector of the n point-value points evaluated
     *			at the n complex nth roots of unity
     * @return the coefficients vector of the polynomial
     * @throws IllegalArgumentException if n is not a power of 2
     */
    private static ComplexNumber[] ifft(ComplexNumber[] y) {
        int n = y.length;
        if (n == 1) {
            return new ComplexNumber[] {y[0]};
        }

        if (n % 2 != 0) {
            throw new IllegalArgumentException("n must be a power of 2!");
        }

        ComplexNumber wn = new ComplexNumber(-2 * Math.PI / n);
        ComplexNumber w = new ComplexNumber(1, 0);
        ComplexNumber[] y0 = new ComplexNumber[n/2];
        ComplexNumber[] y1 = new ComplexNumber[n/2];
        for (int i = 0; i < n / 2; i++) {
            y0[i] = y[2 * i];
            y1[i] = y[2 * i + 1];
        }
        ComplexNumber[] a0 = ifft(y0);
        ComplexNumber[] a1 = ifft(y1);
        ComplexNumber[] a = new ComplexNumber[n];
        for (int k = 0; k < n / 2; k++) {
            ComplexNumber t = w.times(a1[k]); // twiddle factor
            a[k] = a0[k].plus(t);
            a[k + (n/2)] = a0[k].minus(t);
            w = w.times(wn);
        }
        return a;
    }

    /**
     * Discrete Fourier transformation.
     * Converts a polynomial into point-value representation in 
     * <em>THETA(n^2)</em> time.
     *
     * @param a the coefficients vector
     * @param x the x coordinates vector of an n points to evaluate
     * @return the y coordinates vector of the n points
     */
    public static ComplexNumber[] DFT(double[] a, ComplexNumber[] x) {
        int n = a.length;
        ComplexNumber[] y = new ComplexNumber[n];
        for (int k = 0; k < n; k++) {
            ComplexNumber yk = new ComplexNumber(0, 0);
            ComplexNumber wk = x[k];
            ComplexNumber w = new ComplexNumber(1, 0);
            for (int j = 0; j < n; j++) {
                yk = yk.plus(ComplexNumber.multiply(a[j], w));
                w = w.times(wk);
            }
            y[k] = yk;
        }
        return y;
    }

    /**
     * An iterative form of FFT algorithm which improves on the running time
     * of the recursive approach by lowering the constant hidden in the 
     * THETA-notation.
     * Uses bottom-up approach instead of top-down used in recursive algorithm.
     * Running time is <em>THETA(nlgn)</em>.
     *
     * @param a the polynomial coefficients
     * @return point-value form at the n complex nth roots of unity
     * @throws IllegalArgumentException if n is not a power of 2
     */
    public static ComplexNumber[] FFT_iterative(double[] a) {
        int n = a.length;
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n must be a power of 2");
        }
        ComplexNumber[] A = bitReverseCopy(a);
        print(A, "bit-reversed-a");
        for (int s = 0; s <= log(n); s++) {
            int m = (int) Math.pow(2, s);
            System.out.println("m: " + m);
            ComplexNumber wm = new ComplexNumber(2 * Math.PI / m);
            for (int k = 0; k < n; k += m) {
                ComplexNumber w = new ComplexNumber(1, 0);
                for (int j = 0; j < m / 2; j++) {
                    ComplexNumber t = w.times(A[k + j + (m/2)]);
                    ComplexNumber u = A[k + j];
                    A[k + j] = u.plus(t);
                    A[k + j + (m/2)] = u.minus(t);
                    w = w.times(wm);
                }
            }
        }
        return A;
    }

    /**
     * Computes the logarithm base 2 of an integer.
     * @param n the integer
     * @return the logarithm base 2 as an integer
     */
    private static int log(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    /**
     * Rearranges the polynomial coefficients in an array by their 
     * reversed-bits order.
     * The total number of bits is {@code lgn} where n is the size of the array.
     * For example if n = 8 and x = 001(1) then reversed-bits x is 100(4).
     * n must be a power of 2 otherwise the bit-reversed indexes might become
     * out of array's bounds.
     * Running time is <em>THETA(nlgn)</em>.
     *
     * @param n the number to reverse its bits order
     * @return n in reversed-bits order
     * @throws IllegalArgumentException if n is not a power of 2
     */
    private static ComplexNumber[] bitReverseCopy(double[] a) {
        int n = a.length; // n is a power of 2
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n must be a power of 2");
        }
        ComplexNumber[] b = new ComplexNumber[n];
        int bits = log(n) - 1; // extra shift needed at end
        for (int i = 0; i < n; i++) {
            int s = bits;
            int x = i;
            int r = x & 1; // first get LSB of x
            for (x >>= 1; x != 0; x >>= 1) {
                r <<= 1;
                r |= x & 1;
                s--;
            }
            r <<= s;
            b[r] = new ComplexNumber(a[i], 0);
        }
        return b;
    }

    /**
     * Prints an array of complex numbers.
     * @param a the complex numbers array
     * @param s array's label
     */
    private static void print(ComplexNumber[] a, String s) {
        int n = a.length;
        System.out.println(s + ": ");
        System.out.print("[");
        for (int i = 0; i < n - 1; i++) {
            System.out.print(a[i].toString() + ", ");
        }
        System.out.println(a[n - 1].toString() + "]");
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // Testing FFT
        // polynomial 9 -10x + 7x^2 + 6x^3
        double[] a = {9, -10, 7, 6};
        // bound-degree of the polynomial
        int n = a.length;
        // n complex nth roots of unity
        ComplexNumber[] x = new ComplexNumber[n];
        for(int i = 0; i < n; i++) {
            x[i] = new ComplexNumber(2 * Math.PI * i / n);
        }
        print(x, "x");
        // DFT of coefficient vector a at the n complex nth root of unity
        print(DFT(a, x), "DFT");
        // FFT of coefficient vector a at the n complex nth root of unity
        ComplexNumber[] y = FFT_recursive(a);
        print(y, "FFT-Recursive");
        // Testing inverse FFT
        print(FFT_inverse_recursive(y), "FFT-Inverse");
        // Testing FFT_iterative
        print(FFT_iterative(a), "FFT-Iterative");
    }
}
