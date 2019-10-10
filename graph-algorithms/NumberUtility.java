public class NumberUtility {
    /**
     * Compare two {@code Number} types.
     * @param a the first number
     * @param b the second number
     * @return -1 if a is smaller, 1 if bigger or 0 if a equals b
     */
    public static <E extends Number> int compare(E a, E b) {
        if (a instanceof Float) return toFloat(a).compareTo(toFloat(b));
        if (a instanceof Double) return toDouble(a).compareTo(toDouble(b));
        if (a instanceof Long) return toLong(a).compareTo(toLong(b));
        if (a instanceof Short) return toShort(a).compareTo(toShort(b));
        return toInteger(a).compareTo(toInteger(b));
    }

    /**
     * Add the value of two {@code Number} type variables.
     * @param a the first number
     * @param b the second number
     * @return the sum of the two numbers
     */
    public static <E extends Number> Number add(E a, E b) {
        if (a instanceof Double) return a.doubleValue() + b.doubleValue();
        if (a instanceof Float) return a.floatValue() + b.floatValue();
        if (a instanceof Short) return a.shortValue() + b.shortValue();
        if (a instanceof Long) return a.longValue() + b.longValue();
        return a.intValue() + b.intValue();
    }

    /**
     * Substract two {@code Number} type variables.
     * @param a the first number
     * @param b the second number
     * @return the remainder of substracting the second number from the first
     */
    public static <E extends Number> Number substract(E a, E b) {
        if (a instanceof Double) return a.doubleValue() - b.doubleValue();
        if (a instanceof Float) return a.floatValue() - b.floatValue();
        if (a instanceof Short) return a.shortValue() - b.shortValue();
        if (a instanceof Long) return a.longValue() - b.longValue();
        return a.intValue() - b.intValue();
    }

    /**
     * Returns zero value of a {@code Number} type reference.
     */
    public static <E extends Number> Number zero(E typeReference) {
        if (typeReference instanceof Double) return Double.valueOf(0);
        if (typeReference instanceof Float) return Float.valueOf(0);
        // {@code Short.valueOf} does not accept integers as argument
        // because integer is larger than short. For small integers
        // the work around is to cast them to {@code short}
        if (typeReference instanceof Short) return Short.valueOf((short) 0);
        if (typeReference instanceof Long) return Long.valueOf(0);
        return Integer.valueOf(0);
    }

    private static Float toFloat(Number a) {
        return Float.valueOf(a.floatValue());
    }

    private static Double toDouble(Number a) {
        return Double.valueOf(a.doubleValue());
    }

    private static Long toLong(Number a) {
        return Long.valueOf(a.longValue());
    }

    private static Short toShort(Number a) {
        return Short.valueOf(a.shortValue());
    }

    private static Integer toInteger(Number a) {
        return Integer.valueOf(a.intValue());
    }
}
