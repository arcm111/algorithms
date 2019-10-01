/**
 * Helper class for storing and performing numeric operations on numeric
 * data-type values.
 * <p>The supported data-types of stored distances are: {@code Integer},
 * {@code Double}, {@code Float}, {@code Long} and {@code Short}.
 * <p>It also implements +INF, -INF, 0 as generic values for all supported
 *  data types.
 * <p>The class implements {@code Comparable} interface allowing two instances
 * to be compared together even if their values are set to +/-INF.
 *
 * Depends on {@code NumberUtility} class.
 */
public class NumericKey<E extends Number> implements Comparable<NumericKey<E>> {
    public static final INF POSITIVE_INFINITY = INF.POSITIVE;
    public static final INF NEGATIVE_INFINITY = INF.NEGATIVE;
    public static final INF ZERO = INF.ZERO;
    public static final int NIL = -1;
    public INF infinity;
    public E key; // numeric key

    public static enum INF {
        POSITIVE,
        NEGATIVE,
        ZERO,
        NONE
    }

    public NumericKey() {
        this.infinity = INF.NONE;
        this.key = null;
    }

    public NumericKey(E key) {
        setKey(key);
    }

    public NumericKey(INF infinity) {
        setKey(infinity);
    }

    public E getKey() {
        return key;
    }

    public void setKey(E key) {
        this.infinity = INF.NONE;
        this.key = key;
    }

    public void setKey(INF infinity) {
        this.infinity = infinity;
        this.key = null;
    }


    @SuppressWarnings("unchecked")
    public NumericKey<E> plus(E val) {
        if (val == null) {
            throw new IllegalArgumentException("val can't be null");
        }
        NumericKey<E> result = new NumericKey<>();
        switch (this.infinity) {
            case POSITIVE:
            case NEGATIVE:
                result.setKey(this.infinity);
                break;
            case ZERO:
                result.setKey(val);
                break;
            default:
                result.setKey((E) NumberUtility.add(this.key, val));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public NumericKey<E> plus(NumericKey<E> that) {
        INF k1 = this.infinity;
        INF k2 = that.infinity;
        NumericKey<E> sum = new NumericKey<>();
        if (k1 == k2 && k1 != INF.NONE && k2 != INF.NONE) {
            sum.setKey(k1);
        } else if (k1 == POSITIVE_INFINITY && k2 == NEGATIVE_INFINITY) {
            throw new IllegalArgumentException("Indeterminate");
        } else if (k1 == NEGATIVE_INFINITY && k2 == POSITIVE_INFINITY) {
            throw new IllegalArgumentException("Indeterminate");
        } else if (k1 == POSITIVE_INFINITY || k2 == POSITIVE_INFINITY) {
            sum.setKey(POSITIVE_INFINITY);
        } else if (k1 == NEGATIVE_INFINITY || k2 == NEGATIVE_INFINITY) {
            sum.setKey(NEGATIVE_INFINITY);
        } else if (k2 == ZERO) {
            sum.setKey(this.key);
        } else if (k1 == ZERO) {
            sum.setKey(that.key);
        } else {
            sum.setKey((E) NumberUtility.add(this.key, that.key));
        }
        return sum;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(NumericKey<E> that) {
        INF k1 = this.infinity;
        INF k2 = that.infinity;
        if (k1 != INF.NONE && k2 != INF.NONE && k1 == k2) return 0;
        if (k1 == POSITIVE_INFINITY || k2 == NEGATIVE_INFINITY) return 1;
        if (k1 == NEGATIVE_INFINITY || k2 == POSITIVE_INFINITY) return -1;
        E a = this.key;
        E b = that.key;
        if (k1 == ZERO) {
            a = (E) NumberUtility.zero(b);
        } else if (k2 == ZERO) {
            b = (E) NumberUtility.zero(a);
        }
        if (a == null || b == null) {
            throw new IllegalArgumentException("keys cannot be null");
        }
        return NumberUtility.compare(a, b);
    }

    @Override
    public String toString() {
        String str;
        if (infinity == POSITIVE_INFINITY) str = "positive infinity";
        else if (infinity == NEGATIVE_INFINITY) str = "negative infinity";
        else if (infinity == ZERO) str = "zero";
        else if (key == null) str = "null";
        else str = key.toString();
        return str;
    }
}

