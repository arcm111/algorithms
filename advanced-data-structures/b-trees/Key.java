public class Key<T extends Comparable<T>, V> implements Comparable<Key<T, V>>{
    public T key;
    public V val;

    public Key(T key, V val) {
        this.key = key;
        this.val = val;
    }

    public boolean lessThan(Key<T, V> that) {
        if (this.key == null || that == null || that.key == null) {
            throw new IllegalArgumentException("Invalid keys");
        }
        return this.key.compareTo(that.key) < 0;
    }

    public boolean greaterThan(Key<T, V> that) {
        if (this.key == null || that == null || that.key == null) {
            throw new IllegalArgumentException("Invalid keys");
        }
        return this.key.compareTo(that.key) > 0;
    }

    public boolean equals(Key<T, V> that) {
        if (that == null) return false;
        return this.key.compareTo(that.key) == 0;
    }

    @Override
    public int compareTo(Key<T, V> that) {
        if (this.lessThan(that)) return -1;
        if (this.greaterThan(that)) return 1;
        return 0;
    }
    
    @Override
    public String toString() {
        return key + ":" + val;
    }
}

