public class FlowNetworkEdge<T extends VertexInterface, E extends Number> {
    private final T sourceVertex;
    private final T destinationVertex;
    private final int incidentFrom;
    private final int incidentTo;
    private NumericKey<E> capacity;
    private NumericKey<E> flow;

    public FlowNetworkEdge(T sourceVertex, T destinationVertex) {
        this.incidentFrom = sourceVertex.getVertex();
        this.incidentTo = destinationVertex.getVertex();
        this.sourceVertex = sourceVertex;
        this.destinationVertex = destinationVertex;
        this.capacity = new NumericKey<>();
        this.flow = new NumericKey<>(NumericKey.ZERO);
    }

    public FlowNetworkEdge(FlowNetworkEdge<T, E> e) {
        this(e.sourceVertex(), e.destinationVertex());
    }

    public FlowNetworkEdge(T src, T dst, E capacity) {
        this(src, dst);
        this.setCapacity(capacity);
    }

    public FlowNetworkEdge(T src, T dst, NumericKey<E> capacity) {
        this(src, dst);
        this.setCapacity(capacity);
    }

    public FlowNetworkEdge<T, E> reverse() {
        return new FlowNetworkEdge<T, E>(destinationVertex, sourceVertex);
    }

    public T sourceVertex() {
        return sourceVertex;
    }

    public T destinationVertex() {
        return destinationVertex;
    }

    public int incidentFrom() {
        return incidentFrom;
    }

    public int incidentTo() {
        return incidentTo;
    }

    public NumericKey<E> getCapacity() {
        return capacity;
    }

    public void setCapacity(E capacity) {
        this.capacity.setKey(capacity);
    }

    public void setCapacity(NumericKey<E> capacity) {
        this.capacity = capacity;
    }

    public NumericKey<E> getFlow() {
        return flow;
    }

    public void setFlow(E flow) {
        this.flow.setKey(flow);
    }

    public void setFlow(NumericKey<E> flow) {
        this.flow = flow;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(incidentFrom + "--" + flow + "/" + capacity);
        builder.append("-->" + incidentTo);
        return builder.toString();
    }
}
