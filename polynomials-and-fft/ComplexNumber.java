/**
 * Complex numbers and their arithmetic operations.
 */
public class ComplexNumber {
    private static final long PERCISION = (long) 1E+12; // 12 decimals
    private final double a;
    private final double b;

    public ComplexNumber(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public ComplexNumber(double u) {
        this.a = round(Math.cos(u));
        this.b = round(Math.sin(u));
    }

    public double real() {
        return a;
    }

    public double imaginary() {
        return b;
    }

    public static ComplexNumber conjugate(ComplexNumber x) {
        return new ComplexNumber(x.real(), -x.imaginary());
    }

    public ComplexNumber conjugate() {
        return conjugate(this);
    }

    public ComplexNumber divideBy(double n) {
        return new ComplexNumber(round(real() / n), round(imaginary() / n));
    }

    public static ComplexNumber multiply(double a, ComplexNumber b) {
        double r = a * b.real();
        double i = a * b.imaginary();
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        double r = a.real() * b.real() - a.imaginary() * b.imaginary();
        double i = a.real() * b.imaginary() + a.imaginary() * b.real();
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        double r = a.real() + b.real();
        double i = a.imaginary() + b.imaginary();
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber subtract(ComplexNumber a, ComplexNumber b) {
        double r = a.real() - b.real();
        double i = a.imaginary() - b.imaginary();
        return new ComplexNumber(r, i);
    }

    public ComplexNumber times(ComplexNumber b) {
        return multiply(this, b);
    }

    public ComplexNumber times(double a) {
        return multiply(a, this);
    }

    public ComplexNumber plus(ComplexNumber b) {
        return add(this, b);
    }

    public ComplexNumber minus(ComplexNumber b) {
        return subtract(this, b);
    }

    /**
     * Rounds a decimal number to its last 12 decimal points.
     * Used to improve the afloating-point-ccuracy of {@code Math.sin, Math.cos}
     * and division operations.
     */
    private double round(double a) {
        return (double) Math.round(a * PERCISION) / PERCISION;
    }

    public String toString() {
        if (b == 0) return "" + a;
        if (a == 0) {
            if (b == 1) return "i";
            if (b == -1) return "-i";
            return b + "i";
        }
        return "(" + a + ", " + b + ")";
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // testing complex number operations
        ComplexNumber x1 = new ComplexNumber(3, 2);
        ComplexNumber x2 = new ComplexNumber(1, 7);
        System.out.print(x1.toString() + " x " + x2.toString() + " = ");
        System.out.println(x1.times(x2));
        System.out.print("3 x " + x1.toString() + " = ");
        System.out.println(ComplexNumber.multiply(3, x1));
        System.out.print(x1.toString() + " + " + x2.toString() + " = ");
        System.out.println(x1.plus(x2));
        System.out.print(x1.toString() + " - " + x2.toString() + " = ");
        System.out.println(x1.minus(x2));
        System.out.println();
    }
}
