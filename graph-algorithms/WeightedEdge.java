/**
 * A weighted directed graph-edge class.
 */
public class WeightedEdge<T extends VertexInterface, E extends Number> 
        implements Comparable<WeightedEdge<T, E>> {
    private final int u; // incident from
    private final int v; // incident to
    private E w; // weight
    private NumericKey<E> weight;
    private final T sourceVertex;
    private final T dstVertex;

    public WeightedEdge(T sourceVertex, T dstVertex, E w) {
        this(sourceVertex, dstVertex, new NumericKey<E>(w));
    }

    public WeightedEdge(T sourceVertex, T dstVertex, NumericKey<E> weight) {
        this.sourceVertex = sourceVertex;
        this.dstVertex = dstVertex;
        this.u = sourceVertex.getVertex();
        this.v = dstVertex.getVertex();
        this.weight = weight;
    }

    public int incidentFrom() {
        return u;
    }

    public int incidentTo() {
        return v;
    }

    public T getSourceVertex() {
        return sourceVertex;
    }

    public T getDstVertex() {
        return dstVertex;
    }

    public E getWeight() {
        return weight.getKey();
    }

    public void setWeight(E w) {
        this.weight.setKey(w);
    }

    public NumericKey<E> getKey() {
        return weight;
    }

    public void setKey(NumericKey<E> weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(WeightedEdge<T, E> that) {
        return NumberUtility.compare(this.getWeight(), that.getWeight());
    }

    @Override
    public String toString() {
        return "[" + u + "->" + v + "]:[" + w + "]";
    }
}
