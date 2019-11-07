public class PushRelabelVertex <E extends Number> implements VertexInterface {
    private final int index;
    private long h;
    private NumericKey<E> e;

    public PushRelabelVertex(int index) {
        this.index = index;
    }

    public int getVertex() {
        return index;
    }

    public long getHeight() {
        return h;
    }

    public void setHeight(long h) {
        this.h = h;
    }

    public NumericKey<E> getExcessFlow() {
        return e;
    }

    public void setExcessFlow(NumericKey<E> e) {
        this.e = e;
    }

    public boolean isOverflowing() {
        return e.compareTo(new NumericKey<E>(NumericKey.ZERO)) == 1;
    }

    @Override
    public String toString() {
        return "(" + index + ")[" + e + "][" + h + "]";
    }
}
