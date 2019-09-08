/**
 * A weighted directed graph-edge class.
 */
public class WeightedEdge<T extends VertexInterface, E extends Comparable<E>> 
        implements Comparable<WeightedEdge<T, E>> {
    private final int u; // incident from
    private final int v; // incident to
    private E w; // weight
    private final T sourceVertex;
    private final T dstVertex;

    public WeightedEdge(T sourceVertex, T dstVertex, E w) {
        this.sourceVertex = sourceVertex;
        this.dstVertex = dstVertex;
        this.u = sourceVertex.getVertex();
        this.v = dstVertex.getVertex();
        this.w = w;
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
        return w;
    }

    public void setWeight(E w) {
        this.w = w;
    }

    @Override
    public int compareTo(WeightedEdge<T, E> that) {
        return this.getWeight().compareTo(that.getWeight());
    }

    @Override
    public String toString() {
        return "[" + u + "->" + v + "]:[" + w + "]";
    }
}
