public interface GraphInterface <T extends VertexInterface> {
    public Iterable<T> adj(T x);
    public void addEdge(T x, T y);
    public void addEdge(int x, int y);
    public void validateVertex(int v);
    public T getVertex(int v);
    public Iterable<T> getVertices();
    public String toString();
}
